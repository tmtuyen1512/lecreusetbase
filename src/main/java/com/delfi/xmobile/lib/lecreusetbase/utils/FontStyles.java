package com.delfi.xmobile.lib.lecreusetbase.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;

/**
 * Created by USER on 05/21/2019.
 */
public class FontStyles {

    private Context context;

    public FontStyles(@NonNull Context context) {
        this.context = context;
    }

    public Typeface montserrat_regular =
            Typeface.createFromAsset(context.getApplicationContext().getAssets(), "montserrat_regular.otf");


    public Typeface montserrat_semibold =
            Typeface.createFromAsset(context.getApplicationContext().getAssets(), "montserrat_semibold.otf");

}
