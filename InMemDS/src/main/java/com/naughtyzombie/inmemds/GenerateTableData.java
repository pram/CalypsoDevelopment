package com.naughtyzombie.inmemds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.calypso.tk.core.PersistenceException;
import com.calypso.tk.core.sql.ConnectionManager;
import com.naughtyzombie.inmemds.util.db.InMemDBConnection;
import com.naughtyzombie.inmemds.util.sql.Row;
import com.naughtyzombie.inmemds.util.sql.Select;

/**
 * Created by IntelliJ IDEA.
 * User: attalep
 * Date: 10/11/11
 * Time: 16:43
 */
public class GenerateTableData {

    private static final Logger LOG = Logger.getLogger(GenerateTableData.class);

	private static Properties properties = FileUtil.loadProperties();

    private static List<String> lines = null;

	/** INMEM_DB_FILE_EXTENSION */
	private static final String INMEM_DB_FILE_EXTENSION = properties.getProperty("INMEM_DB_FILE_EXTENSION");
    /** DUMP THESE TABLES in FULL*/
    private static final List<String> fullTableList = Arrays.asList(properties.getProperty("FULL_TABLE_LIST").split(",")); 

	/**
	 * retrieve DB table data
	 * 
	 * @throws Exception
	 */
	public String makeDataFile(String envToUse, String envDBUser, String envDBPassword, String tableName, String condition, String limit) throws Exception {
        FieldDetails fd = readTableDetails(tableName);
        ConnectionManager connectionManager = null;
		Connection connection = null;
        String retVal = "";
        try {
            connectionManager = ConnectionManager.getInstance(getOracleConnection(envToUse, envDBUser, envDBPassword));

            connection = connectionManager.getConnection();
            String colsString = formatColoumnName(fd.getColumnDefinitions());
            String testSQLPrefix = " SELECT " + colsString + " FROM ";
            String testSQL = testSQLPrefix + tableName;
            if (condition != null && condition.trim().length() > 0) {
                testSQL = testSQL + " where " + condition;
            } else {
                
                if (!fullTableList.contains(tableName)) {
                    /* Limit the number of retrieved rows
                       Ref - http://www.oracle.com/technetwork/issue-archive/2006/06-sep/o56asktom-086197.html
                     */
                    testSQL = testSQLPrefix + "(" + testSQL + ") where ROWNUM <= " + limit;
                }
            }
            System.out.println("testSQL :- \n " + testSQL + "\n");
            List<Row> rows = Select.execute(testSQL, connection);
            if (rows == null || rows.isEmpty()) {
                System.out.println("EMPTY");
				return retVal;
			}
			StringBuilder data = new StringBuilder();
			for (Row r : rows) {
				for (Object element : r) {
					if (element != null) {
                        if (element instanceof String) {
                            String x = (String) element;
                            element = x.replace(',','_').replace("\n"," ").replace("\r"," ");//Replace commas and newlines
                        }
						data.append(element);
					}
					data.append(fd.getColumnSeparator());
				}
				String t = data.substring(0, data.toString().lastIndexOf(fd.getColumnSeparator()));
				data = new StringBuilder(t);
				data.append("\n");
			}
			retVal = data.toString();

		} catch (PersistenceException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
            System.out.println(e.getMessage());
		} finally {
			connectionManager.release();
		}

        return retVal;
	}

	/*
	 * This method returns the oracle connection
	 */
	private static Connection getOracleConnection(String env, String user, String password) throws Exception {

//		DriverManager.registerDriver(new oracle.jdbc.OracleDriver()); //TODO redo this in case we use sybase

        try {
            Class.forName(properties.getProperty("SOURCE_DB_DRIVER"));
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: failed to load Oracle JDBC driver : " + e.getMessage());
        }

		/*
		 * try { Class.forName("oracle.jdbc.OracleDriver"); } catch
		 * (ClassNotFoundException e) { e.printStackTrace(System.out); }
		 */

		String url = properties.getProperty("DB_URL_PREFIX") + env;

		return DriverManager.getConnection(url, user, password);
	}

	/**
	 * Format the Column Name
	 * String.
	 * 
	 * @param s
	 * @return
	 */
	private static String formatColoumnName(String s) {
		StringBuilder queryText = new StringBuilder();
		for (String str : s.split(",")) {
			queryText.append(str.trim().split(" ")[0]).append(",");
		}
		return queryText.substring(0, queryText.lastIndexOf(","));
	}

	/**
	 * This method read the table details.
	 * 
	 */
	private static FieldDetails readTableDetails(String tableName) {
		String datFileName = tableName.toLowerCase() + properties.getProperty("OUTPUT_SUFFIX");

        FieldDetails fd = new FieldDetails();

		try {
            if (lines == null || lines.size() == 0)  {
                lines = readFileAsLines(InMemDBConnection.getDBFilePath() + INMEM_DB_FILE_EXTENSION);
            }
			for (int i = 0; i < lines.size() && (fd.getColumnSeparator() == null || fd.getColumnDefinitions() == null); i++) {
				String line = lines.get(i);
				if (line.contains(datFileName)) {
					int index = line.lastIndexOf("=");
					if (index != -1) {
                        fd.setColumnSeparator(line.substring(index + 1, index + 2));
					}
				} else if (line.matches("CREATE.+TABLE " + tableName + "\\s*\\(.+")) {//TODO replace this with a regex that accounts for the space between tablename and ( **line.contains("TABLE " + tableName + " (")**
					int startIndex = line.indexOf("(");
					int endIndex = line.lastIndexOf(")");
					if (startIndex != -1) {
                        fd.setColumnDefinitions(line.substring(startIndex + 1, endIndex));
					}
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

        return fd;
	}

	public static void main(String[] args) {
		try {

			Args args_in = new Args(args);
			String tableName = args_in.stringArg("table", properties.getProperty("DEFAULT_CALYPSO_TABLE"));
			String condition = args_in.stringArg("sqlCondition", properties.getProperty("DEFAULT_CALYPSO_TABLE_WHERE_CLAUSE"));
			String envToUse = args_in.stringArg("env", properties.getProperty("SOURCE_DB_URL"));
			String envDBUser = args_in.stringArg("user", properties.getProperty("SOURCE_DB_USER"));
			String envDBPassword = args_in.stringArg("envdbpass", properties.getProperty("SOURCE_DB_PASSWORD"));
            String limit = args_in.stringArg("limit",properties.getProperty("SOURCE_DB_ROW_FETCH_LIMIT"));
            String outputDir = args_in.stringArg("out",properties.getProperty("OUTPUT_FOLDER"));

            GenerateTableData gtd = new GenerateTableData();

            String s = gtd.makeDataFile(envToUse, envDBUser, envDBPassword, tableName, condition, limit);

            FileUtil.writeFile(tableName.toLowerCase() + properties.getProperty("OUTPUT_SUFFIX"), outputDir, s);

            System.out.println(s);
        } catch (Exception e) {
			System.out.println("Error in main method " + e.getMessage());
		}
	}

    /**
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	private static List<String> readFileAsLines(String fileName) throws IOException {
		List<String> fileContent = new ArrayList<String>();
		File fIn = FileUtil.getFile(fileName);
		BufferedReader in = new BufferedReader(new FileReader(fIn));
		String line;
		while ((line = in.readLine()) != null) {
			fileContent.add(line);
		}
		return fileContent;
	}

    private static class FieldDetails {
        private String columnSeparator;
        private String columnDefinitions;

        private FieldDetails() {
            this(",");
        }

        private FieldDetails(String columnSeparator) {
            this.columnSeparator = columnSeparator;
        }

        public String getColumnSeparator() {
            return columnSeparator;
        }

        public void setColumnSeparator(String columnSeparator) {
            this.columnSeparator = columnSeparator;
        }

        public String getColumnDefinitions() {
            return columnDefinitions;
        }

        public void setColumnDefinitions(String columnDefinitions) {
            this.columnDefinitions = columnDefinitions;
        }
    }

}
