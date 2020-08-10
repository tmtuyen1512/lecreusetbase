package com.delfi.xmobile.lib.lecreusetbase.utils;

import android.content.Context;

import com.delfi.xmobile.lib.xcore.common.SharedManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by DANHPC on 6/9/2017.
 */

public class DateTime {

    public static String getCurrentTime(){
        DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
        //DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

    public static String convertToServer(String datetime){
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date date = df.parse(datetime);
            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            return df2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "0001-01-01T00:00:00";
    }

    public static String getCurrentTimeOnTopBar(Context context){
        String lang = SharedManager.getInstance(context).getString(Constant.Language);
        if(lang == null || lang.length() == 0)
            lang = "da";
        DateFormat df = new SimpleDateFormat("dd MMM HH:mm", new Locale(lang));
        String date = df.format(Calendar.getInstance(new Locale(lang)).getTime());
        return date;
    }
}
