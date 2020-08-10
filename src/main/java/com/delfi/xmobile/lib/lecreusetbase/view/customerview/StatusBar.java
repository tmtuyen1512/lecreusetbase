package com.delfi.xmobile.lib.lecreusetbase.view.customerview;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.delfi.xmobile.lib.lecreusetbase.R;

/**
 * Created by USER on 26/10/2017.
 */

public class StatusBar {

    private static View topBar;

    public static void addStatusBar(final Activity context) {

        if(context.findViewById(R.id.status_bar) == null) {
            Log.i(context.getClass().getSimpleName(), "addStatusBar");

            LayoutInflater inflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            topBar = inflater.inflate(R.layout.layout_status_bar, null);

            ViewGroup manager = (ViewGroup) ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);

            int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            int result = 0;
            if (resId > 0) {
                result = context.getResources().getDimensionPixelSize(resId);
            } else {
                // Use Fallback size:
                result = 60; // 60px Fallback
            }


            try {
                RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, result);
                manager.addView(topBar, 0, localLayoutParams);

                ViewGroup header = context.findViewById(R.id.header);
                if(header != null) {
                    RelativeLayout.LayoutParams headerLayout = (RelativeLayout.LayoutParams) header.getLayoutParams();
                    headerLayout.addRule(RelativeLayout.BELOW, R.id.status_bar);
                    header.setLayoutParams(headerLayout);
                }
                else {
                    header = context.findViewById(R.id.blueLinearLayout); //for edit page
                    if(header != null) {
                        RelativeLayout.LayoutParams headerLayout = (RelativeLayout.LayoutParams) header.getLayoutParams();
                        headerLayout.addRule(RelativeLayout.BELOW, R.id.status_bar);
                        header.setLayoutParams(headerLayout);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static void removeStatusBar(Context context, View overview){
        WindowManager manager = ((WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
        try {
            Log.e("StatusBar", "Clear");
            manager.removeView(overview);
        }
        catch (Exception e){

        }
    }
}
