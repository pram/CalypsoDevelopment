package com.naughtyzombie.calypso.maven.util;

import com.naughtyzombie.calypso.maven.process.CalypsoProcess;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import sun.jvmstat.monitor.*;
import sun.tools.jps.Arguments;

import java.net.URISyntaxException;
import java.util.*;

@Component(role = CalypsoMonitor.class)
public class DefaultCalypsoMonitor extends AbstractLogEnabled implements CalypsoMonitor {

    public static final String DEFAULT_PARAMS = "-lm";
    public static void main(String[] args) {
        DefaultCalypsoMonitor dcm = new DefaultCalypsoMonitor();
        dcm.getRunningCalypsoProcesses();
    }

    private List<String> packages = new ArrayList<String>();

    @Override
    public List<CalypsoProcess> getRunningCalypsoProcesses() {
        return getRunningCalypsoProcesses(DEFAULT_PARAMS);
    }

    @Override
    public List<CalypsoProcess> getRunningCalypsoProcesses(String params) {
        packages.add("com.calypso");
        packages.add("calypsox");

        String[] args = {params};

        Arguments arguments;
        List<CalypsoProcess> retVal = new ArrayList<CalypsoProcess>();

        try {
            arguments = new Arguments(args);
        } catch (IllegalArgumentException e) {
            getLogger().error(e.getMessage());
            Arguments.printUsage(System.err);
            return Collections.emptyList();
        }

        try {
            HostIdentifier hostId = arguments.hostId();
            MonitoredHost monitoredHost = MonitoredHost.getMonitoredHost(hostId);

            Set jvms = monitoredHost.activeVms();

            for (Iterator j = jvms.iterator(); j.hasNext(); ) {
                StringBuilder output = new StringBuilder();
                Throwable lastError = null;

                int lvmid = ((Integer) j.next()).intValue();

                output.append(String.valueOf(lvmid));

                if (arguments.isQuiet()) {
                    System.out.println(output);
                    continue;
                }

                MonitoredVm vm = null;
                String vmidString = "//" + lvmid + "?mode=r";

                String errorString = null;

                try {

                    errorString = " -- process information unavailable";
                    VmIdentifier id = new VmIdentifier(vmidString);
                    vm = monitoredHost.getMonitoredVm(id, 0);

                    errorString = " -- main class information unavailable";
                    String mainClass = MonitoredVmUtil.mainClass(vm, arguments.showLongPaths());
                    if (checkClass(mainClass)) {
                        CalypsoProcess process = new CalypsoProcess();
                        process.setPid(lvmid);

                        output.append(" " + mainClass);
                        process.setClassName(mainClass);

                        if (arguments.showMainArgs()) {
                            errorString = " -- main args information unavailable";
                            String mainArgs = MonitoredVmUtil.mainArgs(vm);
                            if (mainArgs != null && mainArgs.length() > 0) {
                                output.append(" " + mainArgs);
                                process.parseMainArgs(mainArgs);
                            }
                        }
                        if (arguments.showVmArgs()) {
                            errorString = " -- jvm args information unavailable";
                            String jvmArgs = MonitoredVmUtil.jvmArgs(vm);
                            if (jvmArgs != null && jvmArgs.length() > 0) {
                                output.append(" " + jvmArgs);
                                process.setJvmArgs(jvmArgs);
                            }
                        }
                        if (arguments.showVmFlags()) {
                            errorString = " -- jvm flags information unavailable";
                            String jvmFlags = MonitoredVmUtil.jvmFlags(vm);
                            if (jvmFlags != null && jvmFlags.length() > 0) {
                                output.append(" " + jvmFlags);
                                process.setJvmFlags(jvmFlags);
                            }
                        }

                        errorString = " -- detach failed";
                        monitoredHost.detach(vm);

                        getLogger().info(output.toString());
                        process.setCommandLine(output.toString());

                        errorString = null;
                        retVal.add(process);
                    }
                } catch (URISyntaxException e) {
                    // unexpected as vmidString is based on a validated hostid
                    lastError = e;
                    assert false;
                } catch (Exception e) {
                    lastError = e;
                } finally {
                    if (errorString != null) {
                        output.append(errorString);
                        if (arguments.isDebug()) {
                            if ((lastError != null)
                                    && (lastError.getMessage() != null)) {
                                output.append("\n\t");
                                output.append(lastError.getMessage());
                            }
                        }
                        getLogger().debug(output.toString());
                        if (arguments.printStackTrace()) {
                            lastError.printStackTrace();
                        }
                        continue;
                    }
                }
            }
        } catch (MonitorException e) {
            if (e.getMessage() != null) {
                getLogger().error(e.getMessage(), e);
            } else {
                Throwable cause = e.getCause();
                if ((cause != null) && (cause.getMessage() != null)) {
                    getLogger().error(e.getMessage(), cause);
                } else {
                    e.printStackTrace();
                }
            }
        }
        return retVal;
    }

    @Override
    public void addPackage(String packageName) {
        packages.add(packageName);
    }

    private boolean checkClass(String mainClass) {
        boolean retVal = false;

        for (String packageName : packages) {
            if (mainClass.startsWith(packageName)) {
                retVal = true;
                break;
            }
        }

        return retVal;
    }
}
