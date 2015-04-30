/*
 * Schemamule, a library for automating database schema tasks
 * Copyright (C) 2006, Moses M. Hohman and Rhett Sutphin
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St., 5th Floor, Boston, MA  02110-1301

 * To contact the authors, send email to:
 * { mmhohman OR rsutphin } AT sourceforge DOT net
 */

package com.naughtyzombie.inmemds;

import com.oracle2hsqldb.*;
import org.apache.log4j.Logger;

import com.oracle2hsqldb.dialect.HSQLDialect;
import com.oracle2hsqldb.dialect.Oracle9Dialect;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Adapted from OracleTest in oracle2hsqldb *
 */
public class GenerateScriptFile {
    private static Properties properties = FileUtil.loadProperties();

    private static final Logger LOG = Logger.getLogger(GenerateScriptFile.class);

    private static final List<String> memoryTableList = new ArrayList<String>();

    public static void main(String[] args) throws SQLException, IOException {
        String user = properties.getProperty("SOURCE_DB_USER");
        String password = properties.getProperty("SOURCE_DB_PASSWORD");
        String url = properties.getProperty("DB_URL_PREFIX") + properties.getProperty("SOURCE_DB_URL");
        String sourceDbDriver = properties.getProperty("SOURCE_DB_DRIVER");
        String destDbDriver = properties.getProperty("DEST_DB_DRIVER");

        GenerateScriptFile gsf = new GenerateScriptFile();

        gsf.createScriptFile(user, password, url, sourceDbDriver, destDbDriver);
    }

    private void createScriptFile(String user, String password, String url, String sourceDbDriver, String destDbDriver) throws SQLException, IOException {
        try {
            Class.forName(sourceDbDriver);
        } catch (ClassNotFoundException e) {
            LOG.error("ERROR: failed to load Source JDBC driver", e);
        }

        try {
            Class.forName(destDbDriver);
        } catch (Exception e) {
            LOG.error("ERROR: failed to load Dest JDBC driver", e);
        }

        Connection oracle = DriverManager.getConnection(url, user, password);
        Schema schema = new SchemaReader(new Configuration(true, false, true, new Oracle9Dialect()), oracle).read("CFG_DIST");
        oracle.close();
        Iterator tables = schema.tables().iterator();
        SchemaWriter writer = new SchemaWriter(new Configuration(true, false, true, new HSQLDialect()));

        Connection hsqldb = DriverManager.getConnection(properties.getProperty("DEST_DB_URL"), properties.getProperty("DEST_DB_USER"), properties.getProperty("DEST_DB_PASSWORD"));

        StringBuilder sb = new StringBuilder();

        try {
            while (tables.hasNext()) {

                Table table = (Table) tables.next();

                LOG.info("Creating table " + table.name());
                LOG.debug("Table type: " + table.type());
                LOG.debug("Table class: " + table.getClass().getName());
                LOG.debug("Primary key is " + table.primaryKey());
                if (table.primaryKey() != null) {
                    LOG.debug("Primary key column is " + table.primaryKey().columns());
                }

                String createTableString = "CREATE TEXT TABLE ";

                String outputLine = writer.write(table, createTableString);
                LOG.debug("\n" + outputLine);
                sb.append("\n").append(outputLine);

                String name = table.name();

                if (name.equalsIgnoreCase("position")) {
                    name = "\"" + name + "\"";
                }

                sb.append("\n").append("SET TABLE ").append(name).append(" SOURCE \"").append(table.name().toLowerCase()).append(".data\"");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        hsqldb.close();

        String fileName = properties.getProperty("INMEM_DB_NAME") + properties.getProperty("INMEM_DB_FILE_EXTENSION");
        String tmpFileName = fileName + ".tmp";
        String outputDir = properties.getProperty("OUTPUT_FOLDER");
        FileUtil.writeFile(tmpFileName, outputDir, sb.toString());

        File tmpFile = FileUtil.getFile(outputDir + "/" + tmpFileName);

        File prependFile = FileUtil.getFile(properties.getProperty("INMEM_DB_PREPEND_SCRIPT"));
        File appendFile = FileUtil.getFile(properties.getProperty("INMEM_DB_APPEND_SCRIPT"));

        List<File> scriptsToConcat = new ArrayList<File>();
        scriptsToConcat.add(prependFile);
        scriptsToConcat.add(tmpFile);
        scriptsToConcat.add(appendFile);

        FileUtil.concatenateTextFiles(scriptsToConcat, new File(outputDir + "/" + fileName));

        tables = schema.tables().iterator();

        String envToUse = properties.getProperty("SOURCE_DB_URL");
        String envDBUser = properties.getProperty("SOURCE_DB_USER");
        String envDBPassword = properties.getProperty("SOURCE_DB_PASSWORD");
        String limit = properties.getProperty("SOURCE_DB_ROW_FETCH_LIMIT");

        while (tables.hasNext()) {
            Table table = (Table) tables.next();

            GenerateTableData gtd = new GenerateTableData();
            String s = null;
            try {
                s = gtd.makeDataFile(envToUse, envDBUser, envDBPassword, table.name(), null, limit);
                FileUtil.writeFile(table.name().toLowerCase() + properties.getProperty("OUTPUT_SUFFIX"), outputDir, s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}