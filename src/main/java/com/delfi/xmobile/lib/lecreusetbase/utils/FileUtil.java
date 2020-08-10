package com.delfi.xmobile.lib.lecreusetbase.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hdadmin on 2/7/2017.
 */

public class FileUtil implements FileFilter {

    /**
     * Get a usable cache directory (external if available, internal otherwise).
     *
     * @param context The context to use
     * @param uniqueName A unique directory name to append to the cache dir
     * @return The cache dir
     */

    public  static  final String TAG = FileUtil.class.getSimpleName();
    private static final String[] okFileExtensions =  new String[] {"jpg", "png", "gif","jpeg"};
    public static final String USER_BEEPS_PATH = (Environment.getExternalStorageDirectory().getAbsolutePath() + "/ScannerBeep");
    private static FileUtil instance ;
    private Context context ;
    public static String exported = "";
    public static String imported = "";
    private static final String ENCODING = "windows-1252";

    public enum MODE{
        APPEND,
        OVERWRITE
    }

    public static FileUtil getInstance(Context context){
        if (instance==null){
            instance = new FileUtil();
            instance.context = context;
        }
        return instance;
    }


    private static final String ALGO = "AES";
    private static final byte[] keyValue = new byte[] { 'T', 'h', 'e', 'B', 'e', 's', 't','S', 'e', 'c', 'r','e', 't', 'K', 'e', 'y' };
    public static ArrayList<File> showFiles(String path) {

        if (path==null || path.equals("")) {
            return null;
        }
        ArrayList<File> list = new ArrayList<>();
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++)
        {
            if (listOfFiles[i].isFile())
            {
                if (!listOfFiles[i].getName().equals(Constant.TAG_SETTING) && !listOfFiles[i].getName().equals(Constant.TAG_IP_AND_PORT) && !listOfFiles[i].getName().equals(Constant.TAG_EXPORT) && !listOfFiles[i].getName().equals(Constant.TAG_RECEIVE_LOG) && !listOfFiles[i].getName().equals(Constant.TAG_IMPORT) && !listOfFiles[i].getName().equals(Constant.TAG_IMPORT_PARAMETER) && !listOfFiles[i].getName().equals(Constant.TAG_EXPORT_PARAMETER)) {
                    list.add(listOfFiles[i]);
                }
            }
        }
        if (list.size()==0){
            list =null;
        }
        return list ;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    public static File getExternalCacheDir(Context context) {
        if (hasFroyo()) {
            return context.getExternalCacheDir();
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static boolean isExternalStorageRemovable() {
        if (hasGingerbread()) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }


    @Override
    public boolean accept(File file) {
        return false;
    }

    public static String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString().replace(":","");
            }
        } catch (Exception ex) {
        }
        return "020000000000";
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public void write(File file, String text, MODE mode) throws IOException {
        if(mode == MODE.OVERWRITE) {
            write(file, text);
        }
        else {
            append(file, text);

        }
    }

    public void write(File file, String[] text, MODE mode) throws IOException {
        if(text.length == 0){
            delete(file);
            return;
        }
        if(mode == MODE.OVERWRITE) {
            write(file, text[0]);
            for (int i = 1; i < text.length; i++) {
                append(file, text[i]);
            }
        }
        else {
            for (int i = 0; i < text.length; i++) {
                append(file, text[i]);
            }
        }
    }

    public String[] read(File file) throws IOException {
        ArrayList<String> list = readArr(file);
        return list.toArray(new String[list.size()]);
    }

    public ArrayList<String> readArr(File file) throws IOException {
        ArrayList<String> text = new ArrayList<String>();
        if(!file.exists())
            return text;
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(inputStream, ENCODING);
            BufferedReader bufferedReader = new BufferedReader(isr);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                text.add(line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return text;
    }

    private boolean delete(File file){
        try {
            return file.delete();
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    private void write(File file, String text) throws IOException {
        FileOutputStream outputStream = null;
        try {
            addFolder(file);
            outputStream = new FileOutputStream(file);
            /*outputStream.write(text.getBytes());
            String line = "\r\n";
            outputStream.write(line.getBytes());
            outputStream.close();*/

            Writer out = new BufferedWriter(new OutputStreamWriter(outputStream, ENCODING));
            out.write(text + "\r\n");

            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private boolean addFolder(File file){
        File f = new File(file.getParent());
        if(!f.exists())
            return f.mkdirs();
        else
            return true;
    }

    private void append(File file, String text) throws IOException {
        FileOutputStream outputStream;
        try {
            addFolder(file);
            outputStream = new FileOutputStream(file, true);
            /*outputStream.write(text.getBytes());
            String line = "\r\n";
            outputStream.write(line.getBytes());
            outputStream.close();*/
            Writer out = new BufferedWriter(new OutputStreamWriter(outputStream, ENCODING));
            out.append(text);
            out.append("\r\n");
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void remove(File file1, File file2) throws Exception {
        String[] arr1 = read(file1);
        write(file2, arr1, MODE.OVERWRITE);
        delete(file1);
    }
}
