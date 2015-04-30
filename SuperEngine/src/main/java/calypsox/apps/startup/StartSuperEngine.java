package calypsox.apps.startup;

import calypsox.engine.SuperEngine;
import com.calypso.apps.startup.AppStarter;
import com.calypso.apps.util.AppUtil;
import com.calypso.apps.util.CalypsoLoginDialog;
import com.calypso.tk.core.Defaults;
import com.calypso.tk.core.Log;
import com.calypso.tk.service.DSConnection;
import com.calypso.tk.util.ConnectionUtil;

/**
 * Created with IntelliJ IDEA.
 * User: pramithattale
 * Date: 05/03/2013
 * Time: 11:21
 */

public class StartSuperEngine extends AppStarter  {
    static String _args[];
    public static void main(String args[]) {
        _args=args;
        Defaults.setIsEngine(true);
        startLog(args,"SuperEngine");
        if(isGUI(args)) {
            if(isOption(args,"-nologingui"))
                (new StartSuperEngine()).onConnect("SuperEngine",args);
            else (new StartSuperEngine()).start(args);
        }
        else startNOGUI(args);
    }
    public static  void startNOGUI(String args[]) {
        try {
            String envName=getOption(args,"-env");
            String user=getOption(args,"-user");
            String passwd=getOption(args,"-password");
            DSConnection ds=ConnectionUtil.connect(user,passwd,"SuperEngine",envName);
            String host = Defaults.getESHost();
            int port=Defaults.getESPort();
            SuperEngine service = new SuperEngine(ds,host,port);
            if(isOption(args,"-nobatch"))
                service.start(false);
            else service.start(true);
        } catch (Exception e) {
            Log.error("StartSuperEngine", e);
            System.exit(1);
        }
    }
    public void start(String args[]) {
        AppUtil.setStarterLook(getOption(args,"-env"));
        CalypsoLoginDialog w = new CalypsoLoginDialog();
        w.setTitle("Super Engine");
        w.setInterface(this);
        w.start(args,false);
    }
    public void onConnect(CalypsoLoginDialog dialog,
                          String user,String passwd, String envName) {
        try {
            DSConnection ds=ConnectionUtil.connect(user,passwd,"SuperEngine",envName);
            String host = Defaults.getESHost();
            int port=Defaults.getESPort();
            SuperEngine service = new SuperEngine(ds,host,port);
            if(isOption(_args,"-nobatch"))
                service.start(false);
            else service.start();
        }
        catch (Exception e) {
            AppUtil.displayError(e,null);
            if(isOption(_args,"-nologingui")) System.exit(1);
            else if(dialog != null) dialog.setVisible(true);
        }
    }

}
