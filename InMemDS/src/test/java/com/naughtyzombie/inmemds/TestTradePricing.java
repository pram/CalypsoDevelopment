package com.naughtyzombie.inmemds;

/**
 * Created by IntelliJ IDEA.
 * User: attalep
 * Date: 10/11/11
 * Time: 15:43
 */

import com.calypso.tk.bo.BOCache;
import com.calypso.tk.core.LegalEntity;
import com.calypso.tk.core.PersistenceException;
import com.calypso.tk.core.Trade;
import com.calypso.tk.core.sql.ConnectionManager;
import com.calypso.tk.core.sql.SeedAllocSQL;
import com.calypso.tk.refdata.User;
import com.calypso.tk.service.DSConnection;
import com.calypso.tk.service.LocalCache;
import com.calypso.tk.service.RemoteAccess;
import com.calypso.tk.service.RemoteTrade;
import com.naughtyzombie.inmemds.util.sql.Select;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:defaultcontext.xml", "classpath:testcontext.xml"})
public class TestTradePricing {


    private DSConnection ds;
    private String peName;

    @Test
    public void testRemoteTrade() throws RemoteException {
        RemoteTrade remoteTrade = ds.getRemoteTrade();
        Trade trade = remoteTrade.getTrade(1158);
        assertNotNull(trade);
        assertEquals(1158, trade.getId());

        assertNotNull(ds.getRemoteMarketData().getPricingEnv(peName));
    }

    public DSConnection getDs() {
        return ds;
    }

    public void setDs(DSConnection ds) {
        this.ds = ds;
    }

    public String getPeName() {
        return peName;
    }

    public void setPeName(String peName) {
        this.peName = peName;
    }
}
