package com.naughtyzombie.calypso.maven.ui;

import com.naughtyzombie.calypso.maven.process.CalypsoProcess;
import com.naughtyzombie.calypso.maven.process.CalypsoProcessDefinition;
import org.apache.commons.lang3.math.NumberUtils;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.codehaus.plexus.logging.AbstractLogEnabled;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Pram Attale
 * Date: 11/12/12
 * Time: 10:39
 */
@Component( role = CalypsoProcessSelectionQueryer.class)
public class DefaultCalypsoProcessSelectionQueryer extends AbstractLogEnabled implements CalypsoProcessSelectionQueryer{

    @Requirement
    private Prompter prompter;

    @Override
    public CalypsoProcess selectCalypsoProcess(Map<String, List<CalypsoProcess>> processes) throws PrompterException {
        return selectCalypsoProcess(processes, null);
    }

    @Override
    public boolean confirmSelection(CalypsoProcessDefinition definition) throws PrompterException {
        String query = "Confirm process selection :\n " + definition.getName() + "(" + definition.getClassName() + ")";

        String answer = prompter.prompt(query, Arrays.asList(new String[]{"Y", "N"}), "Y");

        return "Y".equalsIgnoreCase( answer );
    }

    @Override
    public CalypsoProcess selectCalypsoProcess(Map<String, List<CalypsoProcess>> processes, CalypsoProcessDefinition defaultDefinition) throws PrompterException {
        CalypsoProcess selection = null;

        Map<String, List<CalypsoProcess>> filteredProcesses = processes;

        do {
            StringBuilder query = new StringBuilder("Choose Process:\n");
            Set<String> processKeys = new HashSet<String>();
            List<String> answers = new ArrayList<String>();
            Map<String, CalypsoProcess> processAnswerMap = new HashMap<String, CalypsoProcess>();

            int counter = 0;
            int defaultSelection = 0;

            for (Map.Entry<String, List<CalypsoProcess>> entry : filteredProcesses.entrySet()) {
                String catalog = entry.getKey();
                for (CalypsoProcess process : entry.getValue()) {
                    String processKey = process.getName() + ":" + process.getClassName();

                    if (!processKeys.add(processKey)) {
                        continue;
                    }

                    counter++;

                    StringBuilder description = new StringBuilder();
                    //Instance information takes precedence over description

                    SortedMap<CalypsoProcessInstance, CalypsoProcess> processInstances = getProcessInstances(processes, process.getName(), process.getClassName());
                    if (processInstances.size() > 1) { //There should only be single instances for types like the DS and ES
                        description = new StringBuilder();
                        for (CalypsoProcessInstance instanceEntry : processInstances.keySet()) {
                            description.append(instanceEntry.getId());
                            if (instanceEntry.getQualifier() != null && instanceEntry.getQualifier().length() > 0) {
                                description.append("(").append(instanceEntry.getQualifier()).append(")");
                            }
                            description.append(" ");
                        }
                    } else {
                        if (process.getDescription() == null) {
                            description.append("-");
                        } else {
                            description.append(process.getDescription());
                        }
                    }

                    String answer = String.valueOf(counter);

                    query.append(answer + ":" + process.getAlias() + " -> " + process.getName() + ":" + process.getClassName() + " (" + description.toString().trim() + ")\n");
                    answers.add(answer);

                    processAnswerMap.put(answer, process);

                    if (defaultDefinition != null && process.getName().equals(defaultDefinition.getName()) &&
                            process.getClassName().equals(defaultDefinition.getClassName())) {
                        defaultSelection = counter;
                    }
                }
            }

            if (counter==0) {
                query.append(" Your filter doesn't match any Process (hint: Hit Enter to return to initial list)\n");
            }

            query.append("Choose a number to apply a filter (format: name[:fully qualified class] case sensitive");

            String answer;
            if ( defaultSelection == 0 ) {
                answer = prompter.prompt(query.toString());
            } else {
                answer = prompter.prompt(query.toString(), Integer.toString(defaultSelection));
            }

            getLogger().debug("Chosen Answer " + answer);

            if (NumberUtils.isNumber(answer)) {
                selection = processAnswerMap.get(answer);
            } else {
                //TODO work on filtering later - Not debugging properly in Intellij
                filteredProcesses = CalypsoProcessSelectorUtils.getFilteredProcessesByCatalog(processes, answer);
            }

        } while (selection == null);

        return selectInstance(processes, selection.getName(), selection.getClassName());

    }

    private CalypsoProcess selectInstance(Map<String, List<CalypsoProcess>> processes, String name, String className) throws PrompterException{
        SortedMap<CalypsoProcessInstance, CalypsoProcess> processInstanceMap = getProcessInstances(processes, name, className);

        if (processInstanceMap.size() == 1) {
            return processInstanceMap.values().iterator().next();
        }

        StringBuilder query = new StringBuilder( "Choose " + name + ":" + className + " instance: \n" );

        List<String> answers = new ArrayList<String>();
        Map<String, CalypsoProcess> answerMap = new HashMap<String, CalypsoProcess>();

        int counter = 1;
        String mapKey = null;

        for (Map.Entry<CalypsoProcessInstance, CalypsoProcess> entry : processInstanceMap.entrySet()) {
            CalypsoProcessInstance instance = entry.getKey();
            CalypsoProcess process = entry.getValue();

            mapKey =  String.valueOf(counter);

            query.append( mapKey + ": " + instance + "\n" );
            answers.add( mapKey );
            answerMap.put( mapKey, process );

            counter++;
        }

        query.append("Choose a number: ");

        CalypsoProcess process = null;

        do {
            String answer = prompter.prompt(query.toString(), answers, mapKey);

            process = answerMap.get( answer );
        } while ( process == null );

        return process;
    }

    private SortedMap<CalypsoProcessInstance, CalypsoProcess> getProcessInstances(Map<String, List<CalypsoProcess>> processes, String name, String className) {
        SortedMap<CalypsoProcessInstance, CalypsoProcess> processInstanceMap = new TreeMap<CalypsoProcessInstance, CalypsoProcess>();

        for (Map.Entry<String, List<CalypsoProcess>> entry : processes.entrySet()) {
            for (CalypsoProcess process : entry.getValue()) {
                if (!name.equals(process.getName()) || !className.equals(process.getClassName())) {
                    continue;
                }

                CalypsoProcessInstance instance = new DefaultCalypsoProcessInstance(process.getId());

                if (!processInstanceMap.containsKey(instance)) {
                    processInstanceMap.put(instance, process);
                }
            }
        }
        return processInstanceMap;
    }


    public Prompter getPrompter() {
        return prompter;
    }

    public void setPrompter(Prompter prompter) {
        this.prompter = prompter;
    }
}
