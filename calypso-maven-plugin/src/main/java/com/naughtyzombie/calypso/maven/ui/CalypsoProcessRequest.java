package com.naughtyzombie.calypso.maven.ui;

/**
 * Created with IntelliJ IDEA.
 * User: c935533
 * Date: 05/12/12
 * Time: 12:00
 */
public class CalypsoProcessRequest {
    private String filter;
    private String name;
    private String className;


    public String getFilter() {
        return filter;
    }

    public CalypsoProcessRequest setFilter(String filter) {
        this.filter = filter;

        return this;
    }

    public String getName() {
        return name;
    }

    public CalypsoProcessRequest setName(String name) {
        this.name = name;

        return this;
    }

    public String getClassName() {
        return className;
    }

    public CalypsoProcessRequest setClassName(String className) {
        this.className = className;

        return this;
    }
}
