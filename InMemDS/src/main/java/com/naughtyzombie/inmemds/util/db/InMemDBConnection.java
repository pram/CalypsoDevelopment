package com.naughtyzombie.inmemds.util.db;

/**
 * Created by IntelliJ IDEA.
 * User: attalep
 * Date: 09/11/11
 * Time: 10:33
 */
import com.naughtyzombie.inmemds.util.sql.Common;
import org.hsqldb.jdbc.jdbcConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.io.UnsupportedEncodingException;

public class InMemDBConnection {

	public static final String DB_URL = "jdbc:hsqldb:" + getDBFilePath();
	public static final String DB_DRIVER_CLASS = "org.hsqldb.jdbcDriver";
	public static final String DB_USER_NAME = "sa";
	public static final String DB_PASSWORD = "";
    private static final String KNOWN_DB_FILE_EXTENSION = ".script";
    private static final String KNOWN_DB_FILE = "InMemDS/InMemDS"+KNOWN_DB_FILE_EXTENSION;

    public static  Connection getConnection() throws SQLException {
        try {
            Class.forName(DB_DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            System.out.println("Problem connecting to "+DB_URL);
            throw new SQLException("Failed to locate JDBC Driver class: "+DB_DRIVER_CLASS);
        }
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
        connection.setAutoCommit(false);
        return connection;
    }

    public static void closeConnection(Connection connection) {
    	if(connection != null){
            try {
                connection.prepareStatement("shutdown").execute();
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getDBFilePath(){
        ClassLoader loader = Common.class.getClassLoader();
        java.net.URL urlOfKnownDBFile = loader.getResource(KNOWN_DB_FILE);
        String dbPath = "InMemDBDirectoryNotOnClassPath";
        if (urlOfKnownDBFile != null){
            String fileName = urlOfKnownDBFile.getFile();
            dbPath = fileName.replace(KNOWN_DB_FILE_EXTENSION, "");
            if (":".equals(dbPath.substring(2, 3))){
                dbPath = dbPath.substring(1);
            }

            try {
                dbPath = URLDecoder.decode(dbPath, Charset.defaultCharset().name());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return dbPath;
    }

    public static void addSQLIntercept(String source, String replacement){
        jdbcConnection.addSQLIntercept(source, replacement);
    }

}
