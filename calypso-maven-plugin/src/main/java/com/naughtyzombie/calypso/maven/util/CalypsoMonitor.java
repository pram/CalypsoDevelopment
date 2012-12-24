package com.naughtyzombie.calypso.maven.util;

import com.naughtyzombie.calypso.maven.process.CalypsoProcess;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pram Attale
 * Date: 12/12/12
 * Time: 09:48
 */
public interface CalypsoMonitor {

    String ROLE = CalypsoMonitor.class.getName();

    List<CalypsoProcess> getRunningCalypsoProcesses();
    List<CalypsoProcess> getRunningCalypsoProcesses(String params);
    void addPackage(String packageName);
}
