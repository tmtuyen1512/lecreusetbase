package com.delfi.xmobile.lib.lecreusetbase.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @deprecated As of Build_0.0.008
 */
public class ApplicationConfiguration implements Serializable {

    public Configuration Configuration;

    public static class Configuration implements Serializable {
        public Global Global;
        public Menuer Menuer;

        public static class Global implements Serializable {
            public AppConfig AppConfig;
            public Policies Policies;
            public DeviceLogFile DeviceLogFile;
            public DeviceInfoFile DeviceInfoFile;
            public AutoUpdate AutoUpdate;
            public NetworkInformation NetworkInformation;
            public Databases Databases;
            public FTPServers FTPServers;
            public DeviceSettings DeviceSettings;

            public static class AppConfig implements Serializable {
                public String EnableAlphaKey;
            }

            public static class Policies implements Serializable {
                public String PolicyServerIp;
                public String PolicyServerPort;
                public String PolicyServerUser;
                public String PolicyServerPassword;
                public String PolicyServerConfigDirectory;
            }

            public static class DeviceLogFile implements Serializable {
                public String DeviceLogFileServer;
                public String DeviceLogFilePort;
                public String DeviceLogFileUser;
                public String DeviceLogFilePassword;
                public String DeviceLogFileDirectory;
            }

            public static class DeviceInfoFile implements Serializable {
                public String DeviceInfoFileServer;
                public String DeviceInfoFilePort;
                public String DeviceInfoFileUser;
                public String DeviceInfoFilePassword;
                public String DeviceInfoFileDirectory;
            }

            public static class AutoUpdate implements Serializable {
                public String AutoUpdateServer;
                public String AutoUpdateServerPort;
                public String AutoUpdateServerDirectory;
                public String AutoUpdateServerUser;
                public String AutoUpdateServerPassword;
            }

            public static class NetworkInformation implements Serializable {
                public String SSID;
                public String WifiPassword;
                public String WifiPassword2;
                public String Encryption;
                public String EAPType;
                public String Username;
                public String UserPassword;
                public String CCXFeatures;
                public String RoamPeriod;
                public String RoamTrigger;
                public String RoamDelta;
                public String NoCredsPrompt;
                public String RadioMode;

                public Boolean DHCP;
                public String StaticTerminalIp;
                public String StaticTerminalGateway;
                @Deprecated
                public String StaticTerminalSubnet;
                public String StaticTerminalDns;
                public String StaticTerminalDns2;
            }

            public static class Databases implements Serializable {
                public RDIDatabase RDIDatabase;
                public PartnerDatabase PartnerDatabase;

                public static class RDIDatabase implements Serializable {
                    public String DatabaseFilesPrimary;
                    public String DatabaseFilesPrimaryPort;
                    public String DatabaseFilesPrimaryDirectory;
                    public String DatabaseFilesPrimaryUser;
                    public String DatabaseFilesPrimaryPassword;
                    public String DatabaseFilesSecondary;
                    public String DatabaseFilesSecondaryPort;
                    public String DatabaseFilesSecondaryDirectory;
                    public String DatabaseFilesSecondaryUser;
                    public String DatabaseFilesSecondaryPassword;
                }

                public static class PartnerDatabase implements Serializable {
                    public String DatabaseFilesPrimary;
                    public String DatabaseFilesPrimaryPort;
                    public String DatabaseFilesPrimaryDirectory;
                    public String DatabaseFilesPrimaryUser;
                    public String DatabaseFilesPrimaryPassword;
                    public String DatabaseFilesSecondary;
                    public String DatabaseFilesSecondaryPort;
                    public String DatabaseFilesSecondaryDirectory;
                    public String DatabaseFilesSecondaryUser;
                    public String DatabaseFilesSecondaryPassword;
                }
            }

            public static class FTPServers implements Serializable {
                public List<FTPServer> FTPServer;

                public static class FTPServer {
                    public String Server;
                    public String Port;
                    public String User;
                    public String Password;
                    public String Key;
                }
            }

            public static class DeviceSettings implements Serializable {
                public float StandbyTimeout;
            }
        }

        public static class Menuer implements Serializable {
            public List<Menu> Menu;

            public static class Menu implements Serializable {
                @SerializedName("WMS-Colli")
                public String WMSColli;
                public String UsePartnerPrices;
                public String IsVisible;
                public String IsDetailedOrder;
                public String IsImmediateOrder;
                public Integer MaxQuantity;
                public Integer MaxItemnumber;
                public String Filename;
                public String FTPSentServer;
                public String FTPSentServer_Backup;
                public String FTPSentDirectory;
                public String FTPSentDirectory_Backup;
                public String Name;
                public Integer MaxInitials;
                public Integer MaxLocation;
            }
        }
    }
}