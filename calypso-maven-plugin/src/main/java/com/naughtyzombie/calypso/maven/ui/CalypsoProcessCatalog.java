package com.naughtyzombie.calypso.maven.ui;

import com.naughtyzombie.calypso.maven.process.CalypsoProcess;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pram Attale
 * Date: 07/12/12
 * Time: 10:09
 */
public class CalypsoProcessCatalog {

    private String modelEncoding = "UTF-8";

    private List<CalypsoProcess> processes;

    public List<CalypsoProcess> getProcesses() {
        return this.processes;
    }

    /**
     * Field modelEncoding.
     */
    public String getModelEncoding() {
        return modelEncoding;
    }

    public void setModelEncoding(String modelEncoding) {
        this.modelEncoding = modelEncoding;
    }

    public void setProcesses(List<CalypsoProcess> processes) {
        this.processes = processes;
    }
}
