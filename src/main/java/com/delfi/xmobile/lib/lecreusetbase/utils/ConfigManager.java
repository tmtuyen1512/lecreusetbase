package com.delfi.xmobile.lib.lecreusetbase.utils;


import com.delfi.xmobile.lib.lecreusetbase.model.ApplicationConfiguration;
import com.delfi.xmobile.lib.lecreusetbase.model.BarcodeConfig;

/**
 * Created by USER on 05/10/2019.
 *
 * @deprecated
 */
public class ConfigManager {
    private static final ConfigManager instance = new ConfigManager();
    //private static final String SHARED_REFERS_NAME = "reitan.share_prefs";

    private boolean isFirstTime;
    private BarcodeConfig barcodeConfig;
    private BarcodeConfig temporaryConfig;
    private ApplicationConfiguration appConfig;

    public static ConfigManager getInstance() {
        return instance;
    }

    private ConfigManager() {

    }
    /**
     * @deprecated As of Build_0.0.002
     */
    public void setFirstTime(boolean firstTime) {
        isFirstTime = firstTime;
    }

    /**
     * @deprecated As of Build_0.0.002
     */
    public boolean isFirstTime() {
        return isFirstTime;
    }

    /**
     * @deprecated As of Build_0.0.002
     */
    public BarcodeConfig getBarcodeConfig() {
        return barcodeConfig;
    }

    /**
     * @deprecated As of Build_0.0.002
     */
    public void setBarcodeConfig(BarcodeConfig barcodeConfig) {
        this.barcodeConfig = barcodeConfig;
        this.isFirstTime = false;
    }

    /**
     * @deprecated As of Build_0.0.002
     */
    public BarcodeConfig getTemporaryConfig() {
        return temporaryConfig;
    }

    /**
     * @deprecated As of Build_0.0.002
     */
    public void setTemporaryConfig(BarcodeConfig temporaryConfig) {
        this.temporaryConfig = temporaryConfig;
    }

    /**
     * @deprecated As of Build_0.0.002
     */
    public ApplicationConfiguration getAppConfig() {
        return appConfig;
    }

    /**
     * @deprecated As of Build_0.0.002
     */
    public void setAppConfig(ApplicationConfiguration appConfig) {
        this.appConfig = appConfig;
    }
}
