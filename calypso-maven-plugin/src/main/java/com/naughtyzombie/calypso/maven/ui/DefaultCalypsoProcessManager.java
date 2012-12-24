package com.naughtyzombie.calypso.maven.ui;

import com.naughtyzombie.calypso.maven.source.CalypsoProcessDataSource;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Pram Attale
 * Date: 07/12/12
 * Time: 10:20
 */

@Component( role = CalypsoProcessManager.class)
public class DefaultCalypsoProcessManager implements CalypsoProcessManager {

    @Requirement (role = CalypsoProcessDataSource.class)
    private Map<String, CalypsoProcessDataSource> processSources;

    @Override
    public CalypsoProcessCatalog getAllCatalog() {
        CalypsoProcessDataSource cpds = processSources.get("all");
        try {
            return cpds.getCalypsoProcessCatalog(new Properties());
        } catch (CalypsoDataSourceException e) {
            return new CalypsoProcessCatalog();
        }
    }

    @Override
    public CalypsoProcessCatalog getFullCatalog() {
        CalypsoProcessDataSource cpds = processSources.get("full");
        try {
            return cpds.getCalypsoProcessCatalog(new Properties());
        } catch (CalypsoDataSourceException e) {
            return new CalypsoProcessCatalog();
        }
    }

    @Override
    public CalypsoProcessCatalog getEnvCatalog() {
        CalypsoProcessDataSource cpds = processSources.get("env");
        try {
            return cpds.getCalypsoProcessCatalog(new Properties());
        } catch (CalypsoDataSourceException e) {
            return new CalypsoProcessCatalog();
        }
    }

    @Override
    public CalypsoProcessCatalog getServerCatalog() {
        return new CalypsoProcessCatalog();
    }

    @Override
    public CalypsoProcessCatalog getEngineCatalog() {
        return new CalypsoProcessCatalog();
    }

    @Override
    public CalypsoProcessCatalog getLocalCatalog(String path) {
        return new CalypsoProcessCatalog();
    }

    @Override
    public CalypsoProcessCatalog getRemoteCatalog(String catalog) {
        return new CalypsoProcessCatalog();
    }
}
