package com.delfi.xmobile.lib.lecreusetbase.view.customerview;

import android.content.Context;
import android.graphics.ColorFilter;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by USER on 05/07/2019.
 */
public class ElementWrapper extends TextInputLayout {
    public ElementWrapper(Context context) {
        super(context);
    }

    public ElementWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ElementWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setError(@Nullable CharSequence error) {
        ColorFilter defaultColorFilter = getBackgroundDefaultColorFilter();
        super.setError(error);
        //Reset EditText's background color to default.
        updateBackgroundColorFilter(defaultColorFilter);
    }

    @Override
    public void setErrorEnabled(boolean enabled) {
        super.setErrorEnabled(enabled);
        if (!enabled) {
            return;
        }

        try {
            Field errorViewField = TextInputLayout.class.getDeclaredField("mErrorView");
            errorViewField.setAccessible(true);
            TextView errorView = (TextView) errorViewField.get(this);
            if (errorView != null) {
                errorView.setGravity(Gravity.END);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.END;
                errorView.setLayoutParams(params);
            }
        } catch (Exception e) {
            // At least log what went wrong
            e.printStackTrace();
        }
    }

    @Override
    protected void drawableStateChanged() {
        ColorFilter defaultColorFilter = getBackgroundDefaultColorFilter();
        super.drawableStateChanged();
        //Reset EditText's background color to default.
        updateBackgroundColorFilter(defaultColorFilter);
    }

    private void updateBackgroundColorFilter(ColorFilter colorFilter) {
        if (getEditText() != null && getEditText().getBackground() != null)
            getEditText().getBackground().setColorFilter(colorFilter);
    }

    @Nullable
    private ColorFilter getBackgroundDefaultColorFilter() {
        ColorFilter defaultColorFilter = null;
        if (getEditText() != null && getEditText().getBackground() != null)
            defaultColorFilter = DrawableCompat.getColorFilter(getEditText().getBackground());
        return defaultColorFilter;
    }

    public void resetError() {
        super.setError(null);
    }
}