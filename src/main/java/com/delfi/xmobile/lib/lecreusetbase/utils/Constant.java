package com.delfi.xmobile.lib.lecreusetbase.utils;

import android.os.Environment;

import com.delfi.xmobile.lib.lecreusetbase.R;
import com.delfi.xmobile.lib.lecreusetbase.model.Language;

import java.util.ArrayList;
import java.util.List;


public class Constant {

    public static final String TAG_EXPORT = "export";
    public static final String TAG_IMPORT = "import";
    public static final String TAG_RECEIVE_LOG = "log";
    public static final String TAG_SETTING = "setting";
    public static final String TAG_IP_AND_PORT = "ip_port";
    public static final String TAG_IMPORT_PARAMETER = "import_parameter";
    public static final String TAG_EXPORT_PARAMETER = "export_parameter";
    public static final String KEY_EXTRA_EDIT = "KEY_EXTRA_EDIT";
    public static final String KEY_EXTRA_BARCODE = "KEY_EXTRA_BARCODE";
    public static final String KEY_EXTRA_MESSAGE = "KEY_EXTRA_MESSAGE";

    public static final String IMPORT_PATH = Constant.getExternal() + "/import";
    public static final String EXPORT_PATH = Constant.getExternal() + "/export";
    public static final String BARCODE_CONFIGURATION_XML = "/BarcodeConfiguration.xml";
    public static final String APPLICATION_CONFIGURATION_XML = "/ApplicationConfiguration.xml";

    public static String Language = "LANGUAGE";

    public static String getExternal() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    public static List<com.delfi.xmobile.lib.lecreusetbase.model.Language> LanguageList = new ArrayList<Language>() {
        {
            add(new Language("en", "English", false, R.mipmap.ico_flag_english));
            add(new Language("da", "Dansk", false, R.mipmap.ico_flag_dansk));
        }
    };
}
