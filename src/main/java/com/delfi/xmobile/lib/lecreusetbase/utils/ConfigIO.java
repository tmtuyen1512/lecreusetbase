package com.delfi.xmobile.lib.lecreusetbase.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.delfi.xmobile.lib.lecreusetbase.model.ApplicationConfiguration;
import com.delfi.xmobile.lib.lecreusetbase.model.BarcodeConfig;
import com.delfi.xmobile.lib.xcore.logger.LogEventArgs;
import com.delfi.xmobile.lib.xcore.logger.LogLevel;
import com.delfi.xmobile.lib.xcore.logger.Logger;
import com.delfi.xmobile.lib.xcore.xmltojson.JsonToXml;
import com.delfi.xmobile.lib.xcore.xmltojson.XmlToJson;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by USER on 05/14/2019.
 *
 * @deprecated
 */
public class ConfigIO {

    private ConfigIO() {
    }

    /**
     * @deprecated As of Build_0.0.002
     */
    public static boolean createBarcodeConfigXml(@NonNull Context context, BarcodeConfig config) {

        File file = new File(context.getFilesDir(),
                Constant.BARCODE_CONFIGURATION_XML);

        return writeXml(file, config);
    }

    /**
     * @deprecated As of Build_0.0.002
     */
    public static boolean createAppConfigXml(ApplicationConfiguration config) {

        File file = new File(Constant.IMPORT_PATH,
                Constant.APPLICATION_CONFIGURATION_XML);

        return writeXml(file, config);
    }

    /**
     * @deprecated As of Build_0.0.002
     */
    @Nullable
    public static BarcodeConfig readBarcodeConfigXml(@NonNull Context context) {
        File file = new File(context.getFilesDir(), Constant.BARCODE_CONFIGURATION_XML);
        String content = readTextFile(file);

        try {
            XmlToJson xmlToJson = new XmlToJson.Builder(content).build();
            JSONObject jsObj = new JSONObject(xmlToJson.toString());

            return new Gson().fromJson(jsObj.toString(), BarcodeConfig.class);

        } catch (Exception e) {
            e.printStackTrace();
            Logger.getInstance().logMessage(new LogEventArgs(LogLevel.ERROR, e.getMessage(), e));
        }
        return null;
    }


    /**
     * @deprecated As of Build_0.0.002
     */
    @Nullable
    public static ApplicationConfiguration readAppConfigXml(@NonNull Context context) {
        File file = new File(Constant.IMPORT_PATH, Constant.APPLICATION_CONFIGURATION_XML);
        String content;

        if (file.exists()) {
            content = readTextFile(file);
        } else {
            content = readAssetsFile(context,
                    Constant.APPLICATION_CONFIGURATION_XML.replace("/", ""));
        }

        try {
            XmlToJson xmlToJson = new XmlToJson.Builder(content).build();
            JSONObject jsObj = new JSONObject(xmlToJson.toString());

            ApplicationConfiguration configuration =
                    new Gson().fromJson(jsObj.toString(), ApplicationConfiguration.class);

            return configuration;

        } catch (Exception e) {
            e.printStackTrace();
            Logger.getInstance().logMessage(new LogEventArgs(LogLevel.ERROR, e.getMessage(), e));
        }
        return null;
    }

    private static boolean writeXml(File file, Object obj) {
        FileOutputStream outputStream = null;

        try {
            String jsString = new Gson().toJson(obj);
            JSONObject jsObj = new JSONObject(jsString);
            JsonToXml jsXml = new JsonToXml.Builder(jsObj).build();

            String text = jsXml.toFormattedString();

            outputStream = new FileOutputStream(file);
            outputStream.write(text.getBytes());
            outputStream.write(String.valueOf("\r\n").getBytes());
            outputStream.close();

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    private static String readTextFile(@NonNull File file) {
        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }

    private static String readAssetsFile(@NonNull Context context, String fileName) {
        //Read text from file
        StringBuilder text = new StringBuilder();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(fileName), "UTF-8"));

            // do reading, usually loop until end of file reading
            String line;
            while ((line = reader.readLine()) != null) {
                //process line
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return text.toString();
    }
}
