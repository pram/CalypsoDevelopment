package com.naughtyzombie.inmemds.calypso;

/**
 * Created by IntelliJ IDEA.
 * User: attalep
 * Date: 09/11/11
 * Time: 10:31
 */

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.security.auth.Subject;

import com.calypso.tk.refdata.sql.UserAccessPermissionSQL;
import com.naughtyzombie.inmemds.util.db.InMemDBConnection;
import org.hsqldb.jdbc.jdbcDataSource;

import com.calypso.tk.core.Book;
import com.calypso.tk.core.Defaults;
import com.calypso.tk.core.JDatetime;
import com.calypso.tk.core.PersistenceException;
import com.calypso.tk.core.sql.*;
import com.calypso.tk.refdata.User;
import com.calypso.tk.refdata.sql.LEContactSQL;
import com.calypso.tk.service.*;
import com.calypso.tk.transaction.TransactionManager;
import com.calypso.tk.util.CurrencyUtil;
import com.calypso.tk.util.VersionMismatchException;
import com.naughtyzombie.inmemds.util.sql.Select;
import com.naughtyzombie.inmemds.util.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class InMemDSConnection extends DSConnection {

    private boolean activeConnection;
    private static String connectionString;

    private static InMemDSConnection instance = null;
    private static final String DEFAULT_PRICING_ENVIRONMENT = "DEFAULT";
    private static final String DATA_SERVER_NAME = "InMemDS";
    private static final String APPLICATION_NAME = "Test_"+DATA_SERVER_NAME;

    private static final String USER_NAME = "calypso_user";
    @Autowired
    private RemoteTrade tradeServer = null;
    @Autowired
    private RemoteMarketData marketDataServer = null;
    @Autowired
    private RemoteAccess accessServer = null;
    //    @Autowired /* removing the autowiring as there is a circular reference in the constructor */
    private RemoteBackOffice backOfficeServer = null;
    @Autowired
    private RemoteReferenceData referenceDataServer = null;
    @Autowired
    private RemoteProduct productServer = null;
    @Autowired
    private RemoteAccounting accountingServer = null;
    @Autowired
    private RemoteFXDataServer fxDataServer = null;

    private InMemDSConnection(){
        synchronized (InMemDSConnection.class){
            this._appName = APPLICATION_NAME;
            Properties props = Defaults.getProperties();
            if (props == null){
                props = new Properties();
            }
            props.setProperty(Defaults.DEFAULT_SOCKET_THREADPOOL_SIZE, "2");
            Defaults.setProperties(props);
            this._defaults = getUserDefaults();
            DataServer dataServer = DataServer.get();
            try {
                Field field = dataServer.getClass().getDeclaredField("_ds");
                field.setAccessible(true);
                field.set(dataServer, this);
              } catch (NoSuchFieldException e) {
                System.out.println("Error getting the _ds field");
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                System.out.println("Error setting the _ds field");
                e.printStackTrace();
            }
            this.activeConnection = true;
        }
    }

    public InMemDSConnection(String dummy) {
        this._appName = APPLICATION_NAME;
        Properties props = Defaults.getProperties();
        if (props == null){
            props = new Properties();
        }
        props.setProperty(Defaults.DEFAULT_SOCKET_THREADPOOL_SIZE, "2");
        Defaults.setProperties(props);
        //TODO set default properties
        DataServer dataServer = DataServer.get();
        try {
            Field field = dataServer.getClass().getDeclaredField("_ds");
            field.setAccessible(true);
            field.set(dataServer, this);
        } catch (NoSuchFieldException e) {
            System.out.println("Error getting the _ds field");
        } catch (IllegalAccessException e) {
            System.out.println("Error setting the _ds field");
        }
        this.activeConnection = true;

    }

    public InMemDSConnection(int rmiPort, String dsName, String hostName, String appName, String user, String passwd){

    }

    public static InMemDSConnection initialize(InMemDSConnection newInstance){
        synchronized (InMemDSConnection.class){
            if (instance == null){
                try {
                    setupDatabaseConnection();

                    if (instance == null && newInstance == null ) {
                	    instance = new InMemDSConnection();
                    } else {
                        instance = newInstance;
                    }

                    DSConnection.setDefault(instance);

                    HolidaySQL.refresh();

                    updateSeeds();
                    disableCaches();
                    // Disable auditing
                    instance.getRemoteAccess().setAllowAudit(false, "calypso_user"/*UserContext.getUserDetails().getUsername()*//* instance.getUserInfo().getName()*/);
                    CurrencyUtil.setCurrencyDefaults(instance.getRemoteReferenceData().getCurrencyDefaultsHash());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return instance;
        }
    }

    @Override
    public String getAppName() {
        return APPLICATION_NAME;
    }

    @Override
    public RemoteTrade getRemoteTrade(){
        return tradeServer;
    }

    public void setRemoteTrade(RemoteTrade remoteTrade) {
        this.tradeServer = remoteTrade;
    }

    @Override
    public RemoteMarketData getRemoteMarketData(){
        return marketDataServer;
    }

    public void setRemoteMarketData(RemoteMarketData remoteMarketData) {
        this.marketDataServer = remoteMarketData;
    }

    @Override
    public RemoteAccess getRemoteAccess(){
        return accessServer;
    }

    public void setRemoteAccess(RemoteAccess remoteAccess){
        this.accessServer = remoteAccess;
    }

    @Override
    public RemoteBackOffice getRemoteBO(){
        synchronized(InMemDSConnection.class){
            if (backOfficeServer == null){
                backOfficeServer = new BackOfficeServerImpl();
            }
        }
        return backOfficeServer;
    }

    public void setRemoteBO(RemoteBackOffice remoteBackOffice) {
        this.backOfficeServer = remoteBackOffice;
    }

    @Override
    public RemoteReferenceData getRemoteReferenceData(){
        return referenceDataServer;
    }

    public void setRemoteReferenceData(RemoteReferenceData remoteReferenceData) {
        this.referenceDataServer = remoteReferenceData;
    }

    @Override
    public RemoteProduct getRemoteProduct(){
        return productServer;
    }

    public void setRemoteProduct(RemoteProduct remoteProduct) {
        this.productServer = remoteProduct;
    }

    @Override
    public RemoteAccounting getRemoteAccounting(){
        return accountingServer;
    }

    public void setRemoteAccounting(RemoteAccounting remoteAccounting) {
        this.accountingServer = remoteAccounting;
    }

    @Override
    public RemoteFXDataServer getRemoteFXDataServer(){
        return fxDataServer;
    }

    public void setRemoteFXDataServer(RemoteFXDataServer remoteFXDataServer) {
        this.fxDataServer = remoteFXDataServer;
    }

    @Override
    public String getDefaultPricingEnv(){
        return DEFAULT_PRICING_ENVIRONMENT;
    }

    @Override
    public String getUser(){
        return /*UserContext.getUserDetails().getUsername()*/"calypso_user";
    }

    @Override
    public void disconnect(){
        synchronized (InMemDSConnection.class){

            if (activeConnection && instance == this){
                TransactionManager.release();

                ConnectionManager connectionManager = ConnectionManager.getInstance();
                if (connectionManager != null){
                    connectionManager.release();
                }
                //Empty out the connection pool
                ioSQL._dbServer.disconnect();
                ioSQL._dbServer = null;

                try {
                    Field field = ioSQL.class.getDeclaredField("_connectDone");
                    field.setAccessible(true);
                    field.set(ioSQL.class, false);
                } catch (NoSuchFieldException e) {
                    System.out.println("Error getting the _connectDone field");
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    System.out.println("Error setting the _connectDone field");
                    e.printStackTrace();
                }
                activeConnection = false;

                instance = null;
                DSConnection.setDefault(null);
            }
        }
    }


    public InMemDSConnection(int rmiPort,
                             String dsName,
                             String hostName,
                             String appName,
                             Subject userCredentials) throws com.calypso.tk.util.ConnectException{}
    public InMemDSConnection(int rmiPort,
                             String dsName,
                             String hostName, String appName,
                             String user, String passwd,
                             boolean keepLink,
                             boolean wait) throws com.calypso.tk.util.ConnectException{}
    public InMemDSConnection(int rmiPort,
                             String dsName,
                             String hostName, String appName,
                             Subject userCredentials,
                             boolean keepLink,
                             boolean wait) throws com.calypso.tk.util.ConnectException{}
    public InMemDSConnection(int rmiPort,
                             String dsName,
                             String hostName, String appName,
                             String user, String passwd,
                             boolean keepLink) throws com.calypso.tk.util.ConnectException{}
    public InMemDSConnection(int rmiPort,
                             String dsName,
                             String hostName, String appName,
                             Subject userCredentials,
                             boolean keepLink) throws com.calypso.tk.util.ConnectException{}

    public DSConnection getReadOnlyConnection() throws com.calypso.tk.util.ConnectException{
        return initialize(null);
    }
    public DSConnection getReadWriteConnection() throws com.calypso.tk.util.ConnectException{
        return initialize(null);
    }
    protected void setClosed(boolean b){}

    protected void startMainOrBackup(String dsName, String hostName, boolean wait) throws com.calypso.tk.util.ConnectException{
    }

    public User getUserInfo(){
        try {
            return getRemoteAccess().getUser(this.getUser());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void   setUser(String user){}
    public String getPasswd(){
        System.out.println("!!!!! Returning null instead of password");
        return null;
    }
    public String getHostName(){
        System.out.println("!!!!! Returning null instead of hostname");
        return null;
    }
    public String getDataServerName(){
        return DATA_SERVER_NAME;
    }
    protected boolean start(String dsName,String hostName,boolean wait) throws com.calypso.tk.util.ConnectException{
        return false;
    }
    public void displayClientStarted(){}
    public void checkVersion() throws VersionMismatchException {}
    protected boolean connect(String user, String passwd) throws com.calypso.tk.util.ConnectException{
        return false;
    }
    public boolean connect(String applicationName, String username, String password, Subject userCredentials) throws com.calypso.tk.util.ConnectException{
        return false;
    }
    public void disconnect(String appName){}
    public boolean getLocal(){
        return false;
    }
    protected void initData(){}
    public void refreshHolidays(){}
    public void refreshCurrencyUtil(){}
    public void refreshMimeTypes(){}
    public void refreshFeeDefinition(){}
    public void reconnect() throws com.calypso.tk.util.ConnectException{}
    protected void stopConnection(){}
    /*public void setAccessServerMessage(AccessServerMessage handler){}
    public AccessServerMessage getAccessServerMessage(){
        System.out.println("!!!!! Returning null instead of AccessServerMessage");
        return null;
    }*/
    public static String getAccessToken(){
        System.out.println("!!!!! Returning null instead of AccessToken");
        return null;
    }
    public Vector getListeners(){
        System.out.println("!!!!! Returning null instead of Listeners");
        return null;
    }
    public void addListener(ConnectionListener l){}
    public void removeListener(ConnectionListener l){}
    public String getDefaultCurrency(){
        System.out.println("!!!!! Returning null instead of default currency");
        return null;
    }
    public Book getDefaultBook(){
        System.out.println("!!!!! Returning null instead of default book");
        return null;
    }
    public String getDefaultTradeFilter(){
        System.out.println("!!!!! Returning null instead of default trade filter");
        return null;
    }
    public void changePassword(String newp){}
    public JDatetime getServerCurrentDatetime(){
        System.out.println("!!!!! Returning null instead of server datetime");
        return null;
    }
    public void setRMIService(String serverName, Remote service){}
    public Remote getEJBService(String serverName) throws RemoteException{
        System.out.println("!!!!! Returning null instead of EJBService");
        return null;
    }
    public void setApplicationName(String appName)throws IOException {}
    public boolean isApplicationRunning(String appName)throws RemoteException{
        return false;
    }
    public Vector getApplicationNames() throws RemoteException{
        System.out.println("!!!!! Returning null instead of application names");
        return null;
    }
    protected void startEJB() throws com.calypso.tk.util.ConnectException{}
    protected void reconnectEJB() throws com.calypso.tk.util.ConnectException{}
    public void eventServerStarted(DSConnection ds, int port) throws RemoteException{}
    protected void startConnectionEJB() throws com.calypso.tk.util.ConnectException{}
    protected void initEJB() throws com.calypso.tk.util.ConnectException{}
    public void initEJB(int     rmiPort,            String  dsName,            String  hostName,            String  appName,            String  user,            String  passwd,            boolean keepLink) throws com.calypso.tk.util.ConnectException{}
    protected void removeEJB(){}
    public void refreshUserDefaults(){}
    public boolean isReadOnly(){
        return false;
    }

    private static void updateSeeds(){
        try{
            System.out.println("Trade Seed");
            updateSeed("trade", "trade", "trade_id");
            System.out.println("Message Seed");
            updateSeed("message", "bo_message", "message_id");
            System.out.println("Product Seed");
            updateSeed("product", "product_desc", "product_id");
            System.out.println("Transfer Seed");
            updateSeed("transfer", "bo_transfer", "transfer_id");
            SeedAllocSQL.initialize();
            Field field = SeedAllocSQL.class.getDeclaredField("_seeds");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, SeedItem> seedMap = (Map<String, SeedItem>) field.get(SeedAllocSQL.class);
            for (SeedItem seed : seedMap.values()){
                seed._max += 1000000;
            }
        }
        catch (Exception e){
            System.out.println("Error updating seeds " + e.getMessage());
        }
    }

	private static void updateSeed(String seedName, String table, String column) throws SQLException {
		Connection connection = InMemDBConnection.getConnection();
		long tableCount = Select.getLong("SELECT COUNT(*) FROM " + table, connection);
		if (tableCount > 0) {
			long maxValueInUse = Select.getLong("SELECT MAX(" + column + ") FROM " + table, connection);
			Update.execute("UPDATE calypso_seed SET last_id = ? WHERE seed_name = ?", connection, maxValueInUse + 1000, seedName);
			connection.commit();
		}
	}

    private static void disableCaches(){
        try {
            Field field = LegalEntitySQL.class.getDeclaredField("_cache");
            field.setAccessible(true);
            field.set(LegalEntitySQL.class, new InMemCache());

            field = LEContactSQL.class.getDeclaredField("_cache");
            field.setAccessible(true);
            field.set(LEContactSQL.class, new InMemCache());

            field = ProductSQL.class.getDeclaredField("_cache");
            field.setAccessible(true);
            field.set(ProductSQL.class, new InMemCache());

            field = BookSQL.class.getDeclaredField("_books");
            field.setAccessible(true);
            field.set(BookSQL.class, new InMemCache());

            field = UserAccessPermissionSQL.class.getDeclaredField("_cache");
            field.setAccessible(true);
            changeModifier(field);
            field.set(UserAccessPermissionSQL.class, new InMemCache());

        } catch (Exception e) {
            System.out.println("Error disabling the caches " + e.getMessage());
        }
    }

    private static void changeModifier(Field field) throws NoSuchFieldException, IllegalAccessException {
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    }


    @Autowired
    @Qualifier("connectionString")
    private void setConnectionString(String connString) {
        InMemDSConnection.connectionString = connString;
    }

    private static void setupDatabaseConnection() throws SQLException, PersistenceException, ClassNotFoundException {

        jdbcDataSource dataSource = new jdbcDataSource();
        dataSource.setDatabase(connectionString);
        dataSource.setUser("sa");
        dataSource.setPassword("");

        StandAloneDBSessionHandler sessionHandler = new StandAloneDBSessionHandler();
        sessionHandler.startSession(dataSource.getConnection());


        CalypsoDataSource tx = InMemDataSource.getDataSource(dataSource);
        InMemDatabaseServer t = new InMemDatabaseServer();
        ioSQL._dbServer = t;
        ioSQL._dbServer.setDataSources(tx, tx, tx);
        long timeout = Defaults.getIntProperty("JDBC_NEW_CON_TIMEOUT", 60000);
        ioSQL._dbServer.getConnection();
        boolean x = ioSQL._dbServer.isDatabaseRunning();
        TransactionManager.begin();



        InMemDBConnection.addSQLIntercept("FOR UPDATE", " ");
        InMemDBConnection.addSQLIntercept("FROM \\(product_desc LEFT OUTER JOIN product_sec_code ON product_desc.product_id = product_sec_code.product_id\\) WHERE",
                "FROM product_desc, product_sec_code WHERE product_sec_code.product_id = product_desc.product_id AND");
        InMemDBConnection.addSQLIntercept("DELETE entity_state WHERE", "DELETE FROM entity_state WHERE");
        InMemDBConnection.addSQLIntercept("notional_schedule .product_id", "notional_schedule.product_id");
        InMemDBConnection.addSQLIntercept("rate_schedule .product_id", "rate_schedule.product_id");
    }
}
