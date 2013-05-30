package calypsox.jnlp;

/**
 * Created with IntelliJ IDEA.
 * User: pramithattale
 * Date: 20/02/2013
 * Time: 16:31
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class JNLPLauncher extends HttpServlet {
    private Map<String, Object> props = new HashMap<String, Object>();
    private Map<String, String> supportedApps = new HashMap<String, String>();
    private Map<String, Object> supportedConfigs = new HashMap<String, Object>();
    private List<String> envNames = new ArrayList<String>();
    private static final String PROP_FILE = "jnlp.properties";
    private static final String CONFIGURATION_KEYWORD = "configs";
    private static final String APPEND_KEYWORD = "append";
    private static final String DEFAULT_CONFIG = "default";


    public void init(ServletConfig config) throws ServletException {
        InputStream is = null;

        try {
            debug("loading jnlp.properties");


            Pattern pattern = Pattern.compile(".*jnlp\\.properties.*");

            Collection<String> jnlpProperties = ResourceList.getResources(pattern, config.getServletContext());

            for (String propfileLoc : jnlpProperties) {
                String propertyFile = propfileLoc.substring(propfileLoc.indexOf("jnlp"), propfileLoc.length());
                String envName = propertyFile.replace("jnlp.properties", "").trim();
                if (envName == null || envName.length() == 0) {
                    envName = "DEFAULT";
                } else {
                    envName = envName.substring(1, envName.length());
                    this.envNames.add(envName);
                }

                storePropertyDetails(propertyFile, envName);
            }
        } catch (Exception e) {
            throw new ServletException("Error reading settings from jnlp.properties: " + e.getMessage(), e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
        }
    }

    private void storePropertyDetails(String propFile, String envName) throws ServletException, IOException {
        Properties localRef = new Properties();
        InputStream is;
        is = Thread.currentThread().getContextClassLoader().getResourceAsStream(propFile);

        if (is == null) {
            throw new ServletException("Unable to find " + propFile);
        }

        localRef.load(is);

        for (Iterator it = localRef.keySet().iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            String valueStr = (String) localRef.get(key);
            Object value = null;
            int subPropertyIndex = key.indexOf('.');

            debug("processing " + key + " value=" + valueStr);

            if ((valueStr != null) && (valueStr.trim().length() != 0)) {
                if (valueStr.contains(";")) {
                    value = valueStr.split(";");

                    if (key.equals("apps")) {
                        List<String> appsList = Arrays.asList((String[]) value);
                        for (String app : appsList) {
                            this.supportedApps.put(envName + "_" + app, app);
                        }
                    }
                    if ((subPropertyIndex > 0) && (this.supportedApps.keySet().contains(envName + "_" + key.substring(0, subPropertyIndex))) && (key.substring(subPropertyIndex + 1).equals("configs"))) {

                        this.supportedConfigs.put(envName + "_" + key.substring(0, subPropertyIndex), Arrays.asList((String[]) value));
                    }
                } else {
                    if (key.equals("apps")) {
                        this.supportedApps.put(envName + "_" + valueStr, valueStr);
                    }
                    if ((subPropertyIndex > 0) && (this.supportedApps.keySet().contains(envName + "_" + key.substring(0, subPropertyIndex))) && (key.substring(subPropertyIndex + 1).equals("configs"))) {

                        this.supportedConfigs.put(envName + "_" + key.substring(0, subPropertyIndex), valueStr);
                    }
                    value = valueStr;
                }

                this.props.put(envName + "_" + key, value);
            }
        }
    }


    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String app = req.getParameter("app");
        String config = req.getParameter("config");
        String envName = req.getParameter("envName");


        for (String key : this.props.keySet()) {
            if ((config == null) || (config.equals("")) || (config.equals("default"))) {
                req.setAttribute(key, this.props.get(key));
            } else {
                String[] splitkey = key.split("\\.");
                if (splitkey.length > 1) {
                    String baseProp = splitkey[0];

                    if (key.contains(config)) {
                        if (key.contains("append")) {
                            Object baseObject = this.props.get(baseProp);

                            Object appendObject = this.props.get(key);


                            if (((baseObject instanceof String[])) || ((appendObject instanceof String[]))) {
                                List append = new ArrayList();

                                if ((baseObject instanceof String[])) {
                                    append.addAll(Arrays.asList((String[]) baseObject));
                                } else {
                                    append.add((String) baseObject);
                                }

                                if ((appendObject instanceof String[])) {
                                    append.addAll(Arrays.asList((String[]) appendObject));
                                } else {
                                    append.add((String) appendObject);
                                }

                                req.setAttribute(baseProp, append.toArray(new String[0]));
                            } else {
                                String append = baseObject.toString().trim() + " " + appendObject.toString().trim();

                                req.setAttribute(baseProp, append);
                            }
                        } else {
                            req.setAttribute(baseProp, this.props.get(key));
                        }
                    }
                } else if ((!key.contains("configs")) &&


                        (!this.props.containsKey(key.concat("." + config))) && (!this.props.containsKey(key.concat("." + config + "." + "append")))) {


                    req.setAttribute(key, this.props.get(key));
                }
            }
        }

        StringBuffer baseURL = req.getRequestURL();

        if (envName == null) {
            envName = this.envNames.get(0);

            //Attempt to set the env using the current context path of the web app
            String envContext = req.getContextPath().substring(1, req.getContextPath().length()).trim();
            if (envContext!= null && envContext.length() > 0) {
                if (this.envNames.contains(envContext)) {
                    envName = envContext;
                }
            }

        }

        List<String> envApps = new ArrayList<String>();

        Set<String> sortedApps = new TreeSet<String>(this.supportedApps.keySet());
        for (String key : sortedApps) {
            if (key.startsWith(envName)) {
                envApps.add(this.supportedApps.get(key));
            }
        }

        req.setAttribute("app", app);
        req.setAttribute("supportedApps", envApps);
        req.setAttribute("config", config);
        req.setAttribute("supportedConfigs", this.supportedConfigs);
        req.setAttribute("envName",envName);
        req.setAttribute("envNames", this.envNames);

        if ((app == null) || (app.trim().length() == 0)) {
            req.getRequestDispatcher("launchlist.jsp").forward(req, res);
        } else if ((baseURL == null) || (baseURL.toString().trim().length() == 0)) {
            req.setAttribute("errorTitle", "Unable to deterime base url");
            req.setAttribute("errorDetail", "Base URL was not found");
            req.getRequestDispatcher("error.jsp").forward(req, res);
        } else if (!this.supportedApps.keySet().contains(envName + "_" + app)) {
            req.setAttribute("errorTitle", "Invalid Application Name: [" + app + "] for environment " + envName);
            req.setAttribute("errorDetail", "Please be sure to provide a suppported application name.  Currently known applications are " + this.supportedApps);
            req.getRequestDispatcher("error.jsp").forward(req, res);
        } else {
            if ((config != null) && (!config.equals("default"))) {
                Object configurationValues = this.supportedConfigs.get(envName + "_" + app);
                if ((configurationValues instanceof List)) {
                    List appConfigurations = (List) configurationValues;
                    if (!appConfigurations.contains(config)) {
                        req.setAttribute("errorTitle", "Invalid Configuration Name for Application: [" + config + "]");
                        req.setAttribute("errorDetail", "Please be sure to provide a suppported configuration name for this application.  Currently known configurations are " + appConfigurations);
                        req.getRequestDispatcher("error.jsp").forward(req, res);
                    }
                } else if (!configurationValues.equals(config)) {
                    req.setAttribute("errorTitle", "Invalid Configuration Name for Application: [" + config + "]");
                    req.setAttribute("errorDetail", "Please be sure to provide a suppported configuration name for this application.  Currently known configurations are " + configurationValues);
                    req.getRequestDispatcher("error.jsp").forward(req, res);
                }
            }
            req.setAttribute("baseURL", getBaseURL(baseURL.toString()));
            req.setAttribute("jars", req.getAttribute(envName + "_jars"));
            req.setAttribute("args", req.getAttribute(envName + "_args"));
            req.setAttribute("vmargs", req.getAttribute(envName + "_vmargs"));
            req.getRequestDispatcher("launchjnlp.jsp").forward(req, res);
        }
    }

    private String getBaseURL(String string) {
        int index = string.lastIndexOf("/");
        if (index < 0) {
            index = string.length();
        }

        return string.substring(0, index);
    }

    private void debug(String msg) {
    }
}