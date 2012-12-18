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
import com.naughtyzombie.calypso.maven.process.CalypsoProcess;
import com.naughtyzombie.calypso.maven.ui.CalypsoProcessManager;
import com.naughtyzombie.calypso.maven.ui.CalypsoProcessRequest;
import com.naughtyzombie.calypso.maven.ui.DefaultCalypsoProcessSelector;
import com.naughtyzombie.calypso.maven.util.CalypsoMonitor;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Goal which allows you to check the running Calypso processes
 *
 * @goal status
 * @configurator include-project-dependencies
 */
public class StatusMojo extends AbstractMojo {

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

            CalypsoProcessRequest cpRequest = new CalypsoProcessRequest();

            Map<String, List<CalypsoProcess>> expectedProcesses = DefaultCalypsoProcessSelector.getProcessCatalog(processCatalog, calypsoProcessManager, null);

            List<CalypsoProcess> runningProcesses = cm.getRunningCalypsoProcesses();

            DataExporter exporter = new TextTableExporter();
            exporter.addColumns(
                    new StringColumn("pid", "PID", 10, AlignType.MIDDLE_CENTER),
                    new StringColumn("classname", "Class Name", 70, AlignType.MIDDLE_CENTER),
                    new StringColumn("env", "ENV (expected[/running])", 30, AlignType.MIDDLE_CENTER),
                    new StringColumn("id", "Config", 30, AlignType.MIDDLE_CENTER)
            );

            if (verbose) {
                exporter.addColumns(
                        new StringColumn("jvmArgs", "JVM Args", 50, AlignType.MIDDLE_CENTER),
                        new StringColumn("jvmFlags", "JVM Flags", 50, AlignType.MIDDLE_CENTER),
                        new StringColumn("cmdLine", "Command Line", 50, AlignType.MIDDLE_CENTER)
                );
            }

            for (Map.Entry<String, List<CalypsoProcess>> entry : expectedProcesses.entrySet()) {
                for (CalypsoProcess process : entry.getValue()) {

                    String pid = "Inactive";
                    String env = process.getEnv() != null ? process.getEnv() : "-";

                    if (runningProcesses.contains(process)) {
                        CalypsoProcess calypsoProcess = runningProcesses.get(runningProcesses.indexOf(process));
                        pid = String.valueOf(calypsoProcess.getPid());
                        env = env + "/" + calypsoProcess.getEnv();

                        if (verbose) {
                            process.setJvmArgs(calypsoProcess.getJvmArgs());
                            process.setJvmFlags(calypsoProcess.getJvmFlags());
                            process.setCommandLine(calypsoProcess.getCommandLine());
                        }
                    }

                    List l = new LinkedList();
                    l.add(pid);
                    l.add(process.getClassName());
                    l.add(env);
                    l.add(process.getId());

                    if (verbose) {
                        l.add(process.getJvmArgs());
                        l.add(process.getJvmFlags());
                        l.add(process.getCommandLine());
                    }

                    if (verbose) {
                        exporter.addRows(new Row(
                                pid,
                                process.getClassName(),
                                env, process.getId(),
                                process.getJvmArgs(),
                                process.getJvmFlags(),
                                process.getCommandLine()));
                    } else {
                        exporter.addRows(new Row(
                                pid,
                                process.getClassName(),
                                env, process.getId()));
                    }
                }
            }

            exporter.finishExporting();

        }

    }
}

