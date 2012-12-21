package com.naughtyzombie.calypso.maven.mojo;

/**
 * Created with IntelliJ IDEA.
 * User: c935533
 * Date: 12/12/12
 * Time: 11:46
 */

import com.brsanthu.dataexporter.DataExporter;
import com.brsanthu.dataexporter.model.AlignType;
import com.brsanthu.dataexporter.model.Row;
import com.brsanthu.dataexporter.model.StringColumn;
import com.brsanthu.dataexporter.output.texttable.TextTableExporter;
import com.naughtyzombie.calypso.maven.model.CalypsoProcessModel;
import com.naughtyzombie.calypso.maven.process.CalypsoProcess;
import com.naughtyzombie.calypso.maven.ui.CalypsoProcessManager;
import com.naughtyzombie.calypso.maven.ui.CalypsoProcessRequest;
import com.naughtyzombie.calypso.maven.ui.DefaultCalypsoProcessSelector;
import com.naughtyzombie.calypso.maven.util.CalypsoMonitor;
import dnl.utils.text.tree.TextTree;
import groovy.inspect.TextTreeNodeMaker;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import javax.swing.tree.TreeModel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Goal which shows a dependency tree of the Calypso processes
 *
 * @goal tree
 * @configurator include-project-dependencies
 */
public class TreeMojo extends AbstractMojo {

    /** @component */
    CalypsoMonitor cm;

    /**
     * The process catalogs to use to build a list and let the user choose from.
     * Should be defined at compile time by the calling project
     *
     * To override use -DprocessCatalog="blah"
     *
     * @parameter property="processCatalog" default-value="FULL"
     */
    private String processCatalog;

    /**
     * Display full information about running processes
     *
     * @parameter property="verbose" default-value="false"
     */
    private boolean verbose;

    /** @component */
    private CalypsoProcessManager calypsoProcessManager;

    /**
     * The maven project.
     *
     * @parameter property="project"
     * @readonly
     */
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        //Only run against parent project.
        if (this.project.getParent() == null) {

//            CalypsoProcessRequest cpRequest = new CalypsoProcessRequest();

            Map<String, List<CalypsoProcess>> expectedProcesses = DefaultCalypsoProcessSelector.getProcessCatalog(processCatalog, calypsoProcessManager, null);

            for (Map.Entry<String, List<CalypsoProcess>> entry : expectedProcesses.entrySet()) {

                Map<String,CalypsoProcess> depMap = new HashMap<String, CalypsoProcess>(expectedProcesses.entrySet().size());

                //First pass to place all processes in the dependency map
                for (CalypsoProcess process : entry.getValue()) {

                    getLog().debug("Found process " + process.getName() + "::" + process.getId());

                    String key = process.getName();
                    if (process.getId() != null && process.getId().trim().length() > 0) {
                        key = key + "::" + process.getId();
                    }

                    depMap.put(key,process);
                }

                //Now assign dependencies
                for (CalypsoProcess process : entry.getValue()) {
                    List<String> dependencies = process.getDependencies();
                    if (dependencies != null ) {
                        for (String parent : dependencies) {
                            if (depMap.containsKey(parent)) {
                                CalypsoProcess parentProcess = depMap.get(parent);
                                parentProcess.addChild(process);
                            }
                        }
                    }
                }

                //Make an assumption that EventServer will be the overall parent process
                TreeModel tm = new CalypsoProcessModel(depMap.get("EventServer"));
                TextTree tt = new TextTree(tm);
                System.out.println(tt);
            }
        }
    }
}

