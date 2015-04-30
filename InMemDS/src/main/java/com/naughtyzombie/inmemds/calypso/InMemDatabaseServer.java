package com.naughtyzombie.inmemds.calypso;

/**
 * Created by IntelliJ IDEA.
 * User: attalep
 * Date: 09/11/11
 * Time: 10:51
 */
import com.calypso.tk.core.sql.DatabaseServerOracle;

import java.sql.SQLException;

public class InMemDatabaseServer extends DatabaseServerOracle {

    public InMemDatabaseServer(String driver, String url, String user, String password, boolean isUsingEJB) throws SQLException, ClassNotFoundException {
        super();
    }

    public InMemDatabaseServer() throws SQLException, ClassNotFoundException {
        super();
    }

    @Override
    public boolean isUsingBlobs(){
        //Prevent failures when dealing with Blobs
        return false;
    }

    public static byte[] emptyBlob(){
        return new byte[0];
    }

    public static String getName() {
        return "com.naughtyzombie.inmemds.InMemDatabaseServer";
    }
}
