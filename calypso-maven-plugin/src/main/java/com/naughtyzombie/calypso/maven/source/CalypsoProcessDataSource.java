package com.naughtyzombie.calypso.maven.source;

import com.naughtyzombie.calypso.maven.ui.CalypsoDataSourceException;
import com.naughtyzombie.calypso.maven.ui.CalypsoProcessCatalog;

import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Pram Attale
 * Date: 07/12/12
 * Time: 10:54
 */
public interface CalypsoProcessDataSource {

    String ROLE = CalypsoProcessDataSource.class.getName();

    CalypsoProcessCatalog getCalypsoProcessCatalog(Properties properties) throws CalypsoDataSourceException;
}
