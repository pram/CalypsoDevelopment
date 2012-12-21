package com.naughtyzombie.calypso.maven.process;

import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: c935533
 * Date: 05/12/12
 * Time: 16:22
 */
public class CalypsoProcess implements java.io.Serializable {
    private String name;
    private String className;
    private String description;
    private String id;
    private String alias;
    private String env;
    private String commandLine;
    private int pid;
    private String jvmArgs;
    private String jvmFlags;
    private List<String> dependencies;
    private CalypsoProcess parent;
    private List<CalypsoProcess> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalypsoProcess that = (CalypsoProcess) o;

        if (className != null ? !className.equals(that.className) : that.className != null) return false;
        if (id != null ? !id.equals(that.id) : that.id!= null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (className != null ? className.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        String retVal = name + '\'' +
                "{ className='" + className + '\'';
        if (this.id != null) {
         retVal = retVal + ", id='" + id + '\'';
        }

        retVal = retVal + "}";

        return retVal;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getCommandLine() {
        return commandLine;
    }

    public void setCommandLine(String commandLine) {
        this.commandLine = commandLine;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getJvmArgs() {
        return jvmArgs;
    }

    public void setJvmArgs(String jvmArgs) {
        this.jvmArgs = jvmArgs;
    }

    public String getJvmFlags() {
        return jvmFlags;
    }

    public void setJvmFlags(String jvmFlags) {
        this.jvmFlags = jvmFlags;
    }

    public void parseMainArgs(String mainArgs) throws Exception {//TODO Replace this exception with something more meaningful
        String[] args = mainArgs.split(" ");

        Options options = new Options();

        options.addOption(createOption("env", true));
        options.addOption(createOption("user",true));
        options.addOption(createOption("username",true));
        options.addOption(createOption("password",true));
        options.addOption(createOption("log",false));
        options.addOption(createOption("logdir",true));
        options.addOption(createOption("config",true));
        options.addOption(createOption("nologingui",false));
        options.addOption(createOption("nogui",false));
        options.addOption(createOption("uid",true));
        options.addOption(createOption("server",true));



        CommandLineParser x = new BasicParser();
        try {
            CommandLine cmd = x.parse(options, args);

            this.env = cmd.getOptionValue("env");
            this.id = cmd.getOptionValue("config");

        } catch (ParseException e) {
            throw new Exception(e);
        }
    }

    private Option createOption(String argName, boolean hasArg) {
        Option option = new Option(argName,hasArg," ");
        option.setRequired(false);
        return option;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }

    public void addDependency(String dependency) {
        if (this.dependencies==null) {
            this.dependencies = new ArrayList<String>();
        }

        this.dependencies.add(dependency);
    }

    public CalypsoProcess getParent() {
        return parent;
    }

    public void setParent(CalypsoProcess parent) {
        this.parent = parent;
    }

    public List<CalypsoProcess> getChildren() {
        return children;
    }

    public void setChildren(List<CalypsoProcess> children) {
        this.children = children;
    }

    public void addChild(CalypsoProcess child) {
        if (this.children==null) {
            this.children = new ArrayList<CalypsoProcess>();
        }

        this.children.add(child);
    }
}
