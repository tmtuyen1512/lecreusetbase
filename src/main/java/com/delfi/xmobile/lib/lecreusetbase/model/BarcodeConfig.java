package com.delfi.xmobile.lib.lecreusetbase.model;

import java.io.Serializable;

/**
 * Created by USER on 05/10/2019.
 *
 * @deprecated As of Build_0.0.002
 */
public class BarcodeConfig implements Serializable {
    public Generelt Generelt;
    public Network Network;
    public Config Config;

    public static class Config implements Serializable {
        public Generelt Generelt;
        public Network Network;
    }

    /**
     * @deprecated As of Build_0.0.002
     */
    public static class Network implements Serializable {
        public String WifiPassword2;
        public String EAPType;
        public String UserPassword;
        public String Encryption;
        public String StaticTerminalIp;
        public String DHCP;
        public String StaticTerminalSubnet;
        public String WifiEnabled;
        public String WifiPassword;
        public String Username;
        public String StaticTerminalDns;
        public String StaticTerminalGateway;
        public String SSID;
    }

    /**
     * @deprecated As of Build_0.0.002
     */
    public static class Generelt implements Serializable {
        public String PolicyServerPassword;
        public String PolicyServerIp;
        public String ConfigVersion;
        public String ButiksID;
        public String Kæde;
        public String PolicyServerPort;
        public String PolicyServerConfigDirectory;
        public String PolicyServerUser;
    }
}

