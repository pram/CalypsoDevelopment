package com.naughtyzombie.inmemds;

/**
 * Created by IntelliJ IDEA.
 * User: attalep
 * Date: 10/11/11
 * Time: 15:43
 */
import com.calypso.tk.refdata.User;
import com.naughtyzombie.inmemds.calypso.InMemDSConnection;
import com.naughtyzombie.inmemds.util.sql.Select;
import org.junit.*;
import static org.junit.Assert.*;
import com.calypso.tk.service.DSConnection;
import com.calypso.tk.service.RemoteTrade;
import com.calypso.tk.service.LocalCache;
import com.calypso.tk.service.RemoteAccess;
import com.calypso.tk.core.*;
import com.calypso.tk.core.sql.ConnectionManager;
import com.calypso.tk.core.sql.SeedAllocSQL;
import com.calypso.tk.bo.BOCache;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:defaultcontext.xml", "classpath:testcontext.xml"})
public class TestInMemDSConnection {

    @Autowired
    private DSConnection ds;

    @Test
    public void testDefaultConnection(){
        assertTrue(ds != null);
    }

    @Test
    public void testConnectionManager() throws PersistenceException, SQLException {
        ConnectionManager connectionMgr = ConnectionManager.getInstance();
        assertNotNull(connectionMgr);
        Connection connection = connectionMgr.getConnection();
        assertNotNull(connection);
        // Check the connection works
        assertEquals(123L, Select.getLong("Select 123 from dual", connection));
    }

    @Test
    public void testUserInfoNotNull() {
        User userInfo = ds.getUserInfo();
        assertNotNull(userInfo);
    }

    @Test
    public void testRemoteInterfaces() throws RemoteException {
        assertNotNull(ds.getRemoteTrade());
        assertNotNull(ds.getRemoteReferenceData());
        assertNotNull(ds.getRemoteProduct());
        assertNotNull(ds.getRemoteMarketData());
        assertNotNull(ds.getRemoteFXDataServer());
        assertNotNull(ds.getRemoteBO());
        assertNotNull(ds.getRemoteAccess());
        assertNotNull(ds.getRemoteAccounting());
    }

    @Test
    public void testUserInfo() {
        User userInfo = ds.getUserInfo();
        assertEquals("calypso_user", userInfo.getName());
    }

    @Test
    public void testDomainValuesFromCache() throws RemoteException {
        @SuppressWarnings("unchecked")
        Vector<String> domValues = LocalCache.getDomainValues(ds, "accEventProperty");
        assertNotNull(domValues);
        assertTrue(domValues.contains("PAYMENT"));
    }

    @Test
    public void testDomainValuesFromDS() throws RemoteException {
        @SuppressWarnings("unchecked")
        Vector<String> domValues = ds.getRemoteReferenceData().getDomainValues("accEventProperty");
        assertNotNull(domValues);
        assertTrue(domValues.contains("PAYMENT"));
        System.out.println(domValues);
    }

    @Test
    public void testGetAllUserNames() throws RemoteException {
        @SuppressWarnings("unchecked")
        RemoteAccess access = ds.getRemoteAccess();
        @SuppressWarnings("unchecked")
        Vector<String> names = access.getUserNames();
        assertNotNull(names);
        System.out.println(names);
        assertTrue(names.contains("calypso_user"));
    }

    @Test
    public void testGetDefaultPricingEnvironment() {
        assertEquals("DEFAULT", ds.getDefaultPricingEnv());
    }

    @Test
    public void testGetPricingEnvironment() throws RemoteException {
        assertNotNull(ds.getRemoteMarketData().getPricingEnv(ds.getDefaultPricingEnv()));
    }

    @Test
    public void testRemoteTrade() throws RemoteException {
        RemoteTrade remoteTrade = ds.getRemoteTrade();
        Trade trade = remoteTrade.getTrade(1158);
        assertNotNull(trade);
        assertEquals(1158, trade.getId());
    }

    @Test
    public void testLegalEntityFromCache() throws RemoteException {
        LegalEntity counterparty = BOCache.getLegalEntity(ds, 1201);
        assertNotNull(counterparty);
        assertEquals("Pohjola Group", counterparty.getName());
    }

    @Test
    public void testLegalEntityFromDS() throws RemoteException {
        LegalEntity counterparty = ds.getRemoteReferenceData().getLegalEntity(1201);
        assertNotNull(counterparty);
        assertEquals("Pohjola Group", counterparty.getName());
    }

    @Test
    public void testSequenceNumbers() throws Exception {
        int firstTradeSeq = SeedAllocSQL.nextSeed("trade");
        assertNotNull(firstTradeSeq);
        int secondTradeSeq = SeedAllocSQL.nextSeed("trade");
        assertTrue(secondTradeSeq == firstTradeSeq+1);
    }

    public DSConnection getDs() {
        return ds;
    }

    public void setDs(DSConnection ds) {
        this.ds = ds;
    }
}
