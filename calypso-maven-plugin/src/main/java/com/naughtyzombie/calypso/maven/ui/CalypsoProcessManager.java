package com.naughtyzombie.calypso.maven.ui;

/**
 * Created with IntelliJ IDEA.
 * User: c935533
 * Date: 07/12/12
 * Time: 09:59
 */
public interface CalypsoProcessManager {

    String ROLE = CalypsoProcessManager.class.getName();

    public CalypsoProcessCatalog getAllCatalog();

    public CalypsoProcessCatalog getFullCatalog();

    public CalypsoProcessCatalog getEnvCatalog();

    public CalypsoProcessCatalog getServerCatalog();

    public CalypsoProcessCatalog getEngineCatalog();

    public CalypsoProcessCatalog getLocalCatalog(String path);

    public CalypsoProcessCatalog getRemoteCatalog(String catalog);
}
