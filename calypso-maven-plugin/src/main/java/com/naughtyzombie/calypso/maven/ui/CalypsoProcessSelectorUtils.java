package com.naughtyzombie.calypso.maven.ui;

import com.naughtyzombie.calypso.maven.process.CalypsoProcess;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Pram Attale
 * Date: 11/12/12
 * Time: 09:12
 */
public class CalypsoProcessSelectorUtils {

	private CalypsoProcessSelectorUtils() {
		// No constructor needed for Util class
	}

	// TODO rework filters
	private static String extractNameFromFilter(String filter) {
		return StringUtils.contains(filter, ':') ? StringUtils.substringBefore(filter, ":") : filter;
	}

	private static String extractClassNameFromFilter(String filter) {
		return StringUtils.contains(filter, ':') ? StringUtils.substringAfter(filter, ":") : null;
	}

    private static String extractAliasFromFilter(String filter) {
        return StringUtils.contains(filter,'!') ? StringUtils.substringAfter(filter,"!") : null;
    }

	public static Map<String, List<CalypsoProcess>> getFilteredProcessesByCatalog(Map<String, List<CalypsoProcess>> processes, String filter) {

		Map<String, List<CalypsoProcess>> retVal = Collections.emptyMap();

		if (processes != null || !processes.isEmpty()) {
			Map<String, List<CalypsoProcess>> filtered = new LinkedHashMap<String, List<CalypsoProcess>>(processes.size());

			for (Map.Entry<String, List<CalypsoProcess>> entry : processes.entrySet()) {
				List<CalypsoProcess> filteredProcess = new ArrayList<CalypsoProcess>();

				for (CalypsoProcess process : entry.getValue()) {
					String name = CalypsoProcessSelectorUtils.extractNameFromFilter(filter);
					String className = CalypsoProcessSelectorUtils.extractClassNameFromFilter(filter);
                    String alias = CalypsoProcessSelectorUtils.extractAliasFromFilter(filter);

					/*
					 * if (((name == null) ||
					 * StringUtils.contains(process.getName(), name)) &&
					 * StringUtils.contains(process.getClassName(),
					 * className)) {
					 */
					if (((name == null) || StringUtils.contains(process.getName(), name)) ||
                            StringUtils.contains(process.getAlias(), alias)) {
						filteredProcess.add(process);
					}
				}

				if (!filteredProcess.isEmpty()) {
                    filtered.put(entry.getKey(), filteredProcess);
				}
			}

            if (filtered.size() > 0) {
                retVal  = filtered;
            }
		}

        return retVal;
    }
}
