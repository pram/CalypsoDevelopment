package com.naughtyzombie.calypso.maven.ui;

import com.naughtyzombie.calypso.maven.process.CalypsoProcess;
import com.naughtyzombie.calypso.maven.process.CalypsoProcessDefinition;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: c935533
 * Date: 05/12/12
 * Time: 12:06
 */

@Component( role = CalypsoProcessSelector.class )
public class DefaultCalypsoProcessSelector extends AbstractLogEnabled implements CalypsoProcessSelector {

    @Requirement
    private CalypsoProcessSelectionQueryer calypsoProcessSelectionQueryer;

    @Requirement
    private CalypsoProcessManager calypsoProcessManager;

    @Override
    public void selectProcess(CalypsoProcessRequest cpRequest, Boolean interactiveMode, String processCatalog) throws PrompterException {

        CalypsoProcessDefinition definition = new CalypsoProcessDefinition(cpRequest);

        if (definition.isDefined()) {
            getLogger().info("Process defined by properties");
        }

        Map<String, List<CalypsoProcess>> processes = getProcessCatalog(processCatalog, calypsoProcessManager, getLogger());

        if (StringUtils.isNotBlank(cpRequest.getFilter())) {
            processes = CalypsoProcessSelectorUtils.getFilteredProcessesByCatalog(processes, cpRequest.getFilter());

            if (processes.isEmpty()) {
                getLogger().info("Your filter doesn't match any process. Try again with another value.");
                return;
            }
        }

		if (!definition.isDefined()) {
			Map.Entry<String, CalypsoProcess> found = findProcess(processes, cpRequest.getName(), cpRequest.getClassName());

			if (found != null) {
				String key = found.getKey();
				CalypsoProcess process = found.getValue();

			} else {
				getLogger().warn("Specified process not found.");
				if (interactiveMode.booleanValue()) {
					definition.setName(null);
					definition.setClassName(null);

				}
			}
		}

        if ( !definition.isPartiallyDefined() ) {
            if (interactiveMode.booleanValue() && processes.size() > 0) {
                CalypsoProcess selectedProcess = calypsoProcessSelectionQueryer.selectCalypsoProcess(processes, definition);
            }
        }

    }

	private Map.Entry<String, CalypsoProcess> findProcess(Map<String, List<CalypsoProcess>> processes, String name, String className) {
		CalypsoProcess cp = new CalypsoProcess();
		cp.setName(name);
		cp.setClassName(className);

		for (Map.Entry<String, List<CalypsoProcess>> entry : processes.entrySet()) {
			List<CalypsoProcess> catalog = entry.getValue();

			if (catalog.contains(cp)) {
				CalypsoProcess process = catalog.get(catalog.indexOf(cp));

				return newMapEntry(entry.getKey(), process);
			}
		}

		return null;
	}

	private static <K, V> Map.Entry<K, V> newMapEntry(K key, V value) {
		Map<K, V> map = new HashMap<K, V>(1);
		map.put(key, value);

		return map.entrySet().iterator().next();
	}

    public static Map<String, List<CalypsoProcess>> getProcessCatalog(String processCatalog, CalypsoProcessManager cpm, Logger logger) {
        if ( processCatalog == null ) {
            throw new NullPointerException( "catalogs cannot be null" );
        }

        Map<String, List<CalypsoProcess>> processes = new LinkedHashMap<String, List<CalypsoProcess>>();

        for ( String catalog : StringUtils.split(processCatalog, ",") ) {

            if ("all".equalsIgnoreCase(catalog)) {
                processes.put("all", cpm.getAllCatalog().getProcesses());
            }  else if ("server".equalsIgnoreCase(catalog)) {
                processes.put("server", cpm.getServerCatalog().getProcesses());
            } else if ("full".equalsIgnoreCase(catalog)) {
                processes.put("full", cpm.getFullCatalog().getProcesses());
            } else if ("engine".equalsIgnoreCase(catalog)) {
                processes.put("engine", cpm.getEngineCatalog().getProcesses());
            } else if ("env".equalsIgnoreCase(catalog)) {
                processes.put("env", cpm.getEnvCatalog().getProcesses());
            } else if ( catalog.startsWith( "file://" ) ) {
                String path = catalog.substring( 7 );
                processes.put(catalog,cpm.getLocalCatalog(path).getProcesses());
            } else if ( catalog.startsWith( "http://" ) || catalog.startsWith( "https://" ) ) {
                processes.put(catalog,cpm.getRemoteCatalog(catalog).getProcesses());
            }
        }

        if (processes.size() == 0) {
            if (logger != null) {
                logger.info("No Catalog defined. Using ALL Catalog");
            }
            processes.put("all", cpm.getAllCatalog().getProcesses());
        }
        return processes;
    }
}


