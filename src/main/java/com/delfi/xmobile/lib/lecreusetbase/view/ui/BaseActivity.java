package com.delfi.xmobile.lib.lecreusetbase.view.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.delfi.xmobile.lib.lecreusetbase.R;
import com.delfi.xmobile.lib.lecreusetbase.utils.Constant;
import com.delfi.xmobile.lib.lecreusetbase.utils.DateTime;
import com.delfi.xmobile.lib.lecreusetbase.utils.IDialogEventConfirm;
import com.delfi.xmobile.lib.lecreusetbase.utils.IDialogEventConfirm2;
import com.delfi.xmobile.lib.lecreusetbase.utils.IDialogEventError;
import com.delfi.xmobile.lib.lecreusetbase.utils.IDialogEventErrorRetry;
import com.delfi.xmobile.lib.lecreusetbase.utils.IDialogEventInfo;
import com.delfi.xmobile.lib.lecreusetbase.utils.IEditTextClearedListener;
import com.delfi.xmobile.lib.lecreusetbase.utils.INetworkChangedCallback;
import com.delfi.xmobile.lib.lecreusetbase.utils.NetworkStateReceiver;
import com.delfi.xmobile.lib.lecreusetbase.view.customerview.ElementEditText;
import com.delfi.xmobile.lib.lecreusetbase.view.customerview.ElementWrapper;
import com.delfi.xmobile.lib.lecreusetbase.view.customerview.StatusBar;
import com.delfi.xmobile.lib.lecreusetbase.view.customerview.StatusState;
import com.delfi.xmobile.lib.lecreusetbase.view.ui.common.FullScreenDialog;
import com.delfi.xmobile.lib.xcore.common.SharedManager;
import com.delfi.xmobile.lib.xcore.common.SoundManager;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


/**
 * Created by DANHPC on 10/01/2018.
 */

public class BaseActivity<T> extends AppCompatActivity implements INetworkChangedCallback {
    private ProgressDialog mProgress;
    protected Button btnBack;
    protected TextView tvMainTitle;
    protected ArrayList<View> views = new ArrayList<>();
    protected boolean isKeyboardShow = false;
    protected boolean isAutoShowKeyboard = false;

    protected Locale mCurrentLocale;
    protected float mCurrentFontScale;

    public static final int QUANTITY_INPUT = 1;
    public static final int SCAN_IN_QUANTITY = 2;

    private static final long INTERVAL = TimeUnit.SECONDS.toMillis(2);
    private Thread thread;

    private Locale getLocale() {

        return new Locale(SharedManager.getInstance(this).getString(Constant.Language));
    }

    private boolean keepRunning;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mProgress = new ProgressDialog(this);

        this.keepRunning = true;

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView text = (TextView) findViewById(R.id.tvDate);
                                if(text != null)
                                    text.setText(DateTime.getCurrentTimeOnTopBar(getBaseContext()));

                                ImageView imgWifi = (ImageView) findViewById(R.id.imgWifi);
                                if (imgWifi != null) {
                                    int numberOfLevels = 5;
                                    int level = 0;
                                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                                    if(wifiManager != null) {
                                        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                                        if (wifiInfo != null)
                                            level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);

                                        if (wifiManager.isWifiEnabled()) {
                                            if (level == 0)
                                                imgWifi.setImageResource(R.drawable.ic_wifi_signal_level_0);
                                            else if (level == 1)
                                                imgWifi.setImageResource(R.drawable.ic_wifi_signal_level_1);
                                            else if (level == 2)
                                                imgWifi.setImageResource(R.drawable.ic_wifi_signal_level_2);
                                            else if (level == 3)
                                                imgWifi.setImageResource(R.drawable.ic_wifi_signal_level_3);
                                            else if (level == 4)
                                                imgWifi.setImageResource(R.drawable.ic_wifi_signal_level_4);
                                        }
                                        else
                                            imgWifi.setImageResource(R.drawable.ic_wifi_disable);
                                    }
                                    else
                                        imgWifi.setImageResource(R.drawable.ic_wifi_disable);
                                }

                                ImageView imgEthernet = (ImageView) findViewById(R.id.imgEthernet);
                                if (imgEthernet != null) {
                                    if(isEthernetConnected())
                                        imgEthernet.setVisibility(View.VISIBLE);
                                    else
                                        imgEthernet.setVisibility(View.GONE);
                                }

                                //Log.i("Runnable", BaseActivity.this.getClass().getSimpleName());
                            }
                        });
                        Thread.sleep(INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        //Log.i("thread check time", e.getMessage());
                    }

                }while(keepRunning);

            }
        });
        thread.start();
    }

    public boolean isEthernetConnected(){
        ConnectivityManager connectivityManager  = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()){
            return (activeNetworkInfo.getType() == ConnectivityManager.TYPE_ETHERNET);
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCurrentLocale = getResources().getConfiguration().locale;
        mCurrentFontScale = getResources().getConfiguration().fontScale;
        try {
            StatusBar.addStatusBar(this);
        }
        catch (Exception e){}
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Locale locale = getLocale();
        float fontScale = SharedManager.getInstance(getBaseContext()).getFloat("Font-Scale");

        Log.d("TAG", "mCurrentLocale = " + mCurrentLocale + " mCurrentFontScale = " + mCurrentFontScale);
        if (!locale.equals(mCurrentLocale) || fontScale != mCurrentFontScale) {
            mCurrentLocale = locale;
            mCurrentFontScale = fontScale;
            Log.d("Configuration", "Configuration  Changed!!!");
            recreateActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.d(this.getClass().getSimpleName(), "onResume!!! " + getTaskId());
        btnBack = (Button) findViewById(R.id.btnBack);
        tvMainTitle = (TextView) findViewById(R.id.mainTitle);

        Window w = getWindow();
        if (w != null) {
            w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }

        if (btnBack != null)
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BaseActivity.this.finish();
                }
            });

        NetworkStateReceiver.callback = this;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);

        registerReceiver(mBatInfoReceiver, intentFilter);

        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mBatInfoReceiver);
        unregisterReceiver(wifiReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.keepRunning = false;
    }

    protected void changeFragment(Fragment page, @IdRes int resId) {
        final String TAG_FRAGMENT = "TAG_FRAGMENT";
        FragmentManager fManager = getSupportFragmentManager();
        Fragment fragment = fManager.findFragmentByTag(TAG_FRAGMENT);
        if (fragment != null) {
            fManager.beginTransaction()
                    .remove(fragment)
                    .replace(resId, page, TAG_FRAGMENT).commit();
        } else {
            fManager.beginTransaction()
                    .replace(resId, page, TAG_FRAGMENT).commit();
        }
    }

    protected void removeFragment(Fragment page, @IdRes int resId) {
        final String TAG_FRAGMENT = "TAG_FRAGMENT";
        FragmentManager fManager = getSupportFragmentManager();
        Fragment fragment = fManager.findFragmentByTag(TAG_FRAGMENT);
        if (fragment != null) {
            fManager.beginTransaction()
                    .remove(fragment).commit();
        }
    }

    protected void mShowProgressDialog(boolean show) {
        if (show) {
            hideSoftKeyboard(getCurrentFocus());
            mProgress.setMessage(getResources().getString(R.string.please_wait));
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.setCancelable(false);
            mProgress.show();
        } else {
            try {
                mProgress.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void mShowProgressDialog(boolean show, String mess) {
        if (show) {
            hideSoftKeyboard(getCurrentFocus());
            mProgress.setMessage(mess);
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.setCancelable(false);
            mProgress.show();
        } else {
            try {
                mProgress.dismiss();
            } catch (Exception e) {
            }
        }
    }

    public void mShowDialogInfo(String mess, final IDialogEventInfo callback) {
        mShowProgressDialog(false);

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(View.inflate(getApplicationContext(), R.layout.dialog_info, null));

        TextView editText = dialog.findViewById(R.id.message);
        Button buttonOk = dialog.findViewById(R.id.buttonOk);

        editText.setText(mess);
        setSpanHelpText(mess, editText);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null)
                    callback.onOk();
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog1, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_ESCAPE || keyCode == KeyEvent.KEYCODE_BACK)) {
                    if (callback != null)
                        callback.onOk();
                    dialog1.dismiss();
                }

                return false;
            }
        });
        Window w = dialog.getWindow();
        if (w != null) {
            w.setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.show();
        /*Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);*/
    }

    public void mShowDialogInfo(@NonNull String mess, String buttonText, final IDialogEventInfo callback) {
        mShowProgressDialog(false);

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(View.inflate(getApplicationContext(), R.layout.dialog_info, null));
        TextView editText = dialog.findViewById(R.id.message);
        Button buttonOk = dialog.findViewById(R.id.buttonOk);

        editText.setText(mess);
        setSpanHelpText(mess, editText);

        buttonOk.setText(buttonText);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null)
                    callback.onOk();
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog1, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_ESCAPE || keyCode == KeyEvent.KEYCODE_BACK)) {
                    if (callback != null)
                        callback.onOk();
                    dialog1.dismiss();
                }

                return false;
            }
        });
        Window w = dialog.getWindow();
        if (w != null) {
            w.setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.show();
        /*Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);*/
    }

    public void mShowDialogErrorRetry(@NonNull String mess, final IDialogEventErrorRetry callback) {
        mShowProgressDialog(false);

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(View.inflate(getApplicationContext(), R.layout.dialog_error_retry, null));
        TextView editText = dialog.findViewById(R.id.message);
        Button buttonCancel = dialog.findViewById(R.id.buttonCancel);
        Button buttonRetry = dialog.findViewById(R.id.buttonRetry);

        editText.setText(mess);
        setSpanHelpText(mess, editText);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null)
                    callback.onCancel();
                dialog.dismiss();
            }
        });
        buttonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null)
                    callback.onRetry();
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog1, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && (keyCode == KeyEvent.KEYCODE_ESCAPE || keyCode == KeyEvent.KEYCODE_BACK)) {
                    if (callback != null)
                        callback.onCancel();
                    dialog1.dismiss();
                }
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (callback != null)
                        callback.onRetry();
                    dialog1.dismiss();
                }

                return false;
            }
        });
        Window w = dialog.getWindow();
        if (w != null) {
            w.setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.show();

        SoundManager.getInstance().PlayError(getApplicationContext());
    }

    public void mShowDialogError(@NonNull String mess, final IDialogEventError callback) {
        mShowProgressDialog(false);

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(View.inflate(getApplicationContext(), R.layout.dialog_error, null));
        TextView editText = dialog.findViewById(R.id.message);
        Button buttonCancel = dialog.findViewById(R.id.buttonOk);

        editText.setText(mess);
        setSpanHelpText(mess, editText);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null)
                    callback.onOk();
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog1, int keyCode, KeyEvent mEvent) {

                if (mEvent.getAction() == KeyEvent.ACTION_UP && (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_ESCAPE || keyCode == KeyEvent.KEYCODE_BACK)) {
                    if (callback != null)
                        callback.onOk();
                    dialog1.dismiss();
                }

                return false;
            }
        });
        Window w = dialog.getWindow();
        if (w != null) {
            w.setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.show();

        SoundManager.getInstance().PlayError(getApplicationContext());
    }

    public void mShowDialogError(@StringRes int messId, final IDialogEventError callback) {
        mShowProgressDialog(false);

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(View.inflate(getApplicationContext(), R.layout.dialog_error, null));
        TextView editText = dialog.findViewById(R.id.message);
        Button buttonCancel = dialog.findViewById(R.id.buttonOk);

        String mess = getResources().getString(messId);
        editText.setText(mess);
        setSpanHelpText(mess, editText);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null)
                    callback.onOk();
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog1, int keyCode, KeyEvent mEvent) {

                if (mEvent.getAction() == KeyEvent.ACTION_UP && (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_ESCAPE || keyCode == KeyEvent.KEYCODE_BACK)) {
                    if (callback != null)
                        callback.onOk();
                    dialog1.dismiss();
                }

                return false;
            }
        });
        Window w = dialog.getWindow();
        if (w != null) {
            w.setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.show();

        SoundManager.getInstance().PlayError(getApplicationContext());
    }

    public void mShowDialogConfirm(@NonNull String mess, final IDialogEventConfirm callback) {
        mShowProgressDialog(false);

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(View.inflate(getApplicationContext(), R.layout.dialog_confirm, null));
        TextView editText = dialog.findViewById(R.id.message);
        Button buttonNo = dialog.findViewById(R.id.buttonNo);
        Button buttonYes = dialog.findViewById(R.id.buttonYes);

        editText.setText(mess);
        setSpanHelpText(mess, editText);

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null)
                    callback.onNo();
                dialog.dismiss();
            }
        });
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null)
                    callback.onYes();
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog1, int keyCode, KeyEvent mEvent) {
                if (mEvent.getAction() == KeyEvent.ACTION_UP && (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE)) {
                    if (callback != null)
                        callback.onNo();
                    dialog1.dismiss();
                }
                if (mEvent.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (callback != null)
                        callback.onYes();
                    dialog1.dismiss();
                }

                return false;
            }
        });
        Window w = dialog.getWindow();
        if (w != null) {
            w.setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.show();
        /*Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);*/
    }

    /**
     * Added on Build_0.0.004
     *
     * @deprecated Consider using ModuleDialog instead
     */
    public void mShowDialogConfirm(String title, @NonNull String message, final IDialogEventConfirm callback) {
        mShowProgressDialog(false);

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(View.inflate(getApplicationContext(), R.layout.dialog_confirm, null));

        Button buttonNo = dialog.findViewById(R.id.buttonNo);
        Button buttonYes = dialog.findViewById(R.id.buttonYes);

        TextView tvTitle = dialog.findViewById(R.id.header);
        TextView tvMsg = dialog.findViewById(R.id.message);

        tvTitle.setText(title);
        tvMsg.setText(message);
        setSpanHelpText(message, tvMsg);

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null)
                    callback.onNo();
                dialog.dismiss();
            }
        });
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null)
                    callback.onYes();
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog1, int keyCode, KeyEvent mEvent) {
                if (mEvent.getAction() == KeyEvent.ACTION_UP && (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE)) {
                    if (callback != null)
                        callback.onNo();
                    dialog1.dismiss();
                }
                if (mEvent.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (callback != null)
                        callback.onYes();
                    dialog1.dismiss();
                }

                return false;
            }
        });
        Window w = dialog.getWindow();
        if (w != null) {
            w.setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.show();
        /*Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);*/
    }

    public void mShowDialogConfirm(String title, @NonNull String message, final IDialogEventConfirm2 callback) {
        mShowProgressDialog(false);

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(View.inflate(getApplicationContext(), R.layout.dialog_confirm_ok_cancel, null));

        TextView tvTitle = dialog.findViewById(R.id.header);
        TextView tvMsg = dialog.findViewById(R.id.message);

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnOK = dialog.findViewById(R.id.btnOK);

        tvTitle.setText(title);
        tvMsg.setText(message);
        setSpanHelpText(message, tvMsg);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null)
                    callback.onCancel();
                dialog.dismiss();
            }
        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null)
                    callback.onOK();
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog1, int keyCode, KeyEvent mEvent) {
                if (mEvent.getAction() == KeyEvent.ACTION_UP && (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE)) {
                    if (callback != null)
                        callback.onCancel();
                    dialog1.dismiss();
                }
                if (mEvent.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (callback != null)
                        callback.onOK();
                    dialog1.dismiss();
                }

                return false;
            }
        });
        Window w = dialog.getWindow();
        if (w != null) {
            w.setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.show();
        /*Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);*/
    }

    private class OnNeedHelpListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            FullScreenDialog.getInstance().showNeedHelp(BaseActivity.this);
        }
    }

    private void setSpanHelpText(@NonNull String message, TextView tvMessage) {
        String help = getResources().getString(R.string.click_for_help);
        if (message.contains(help)) {
            SpannableString s = new SpannableString(message);

            s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),
                    message.indexOf(help), message.length(), 0);
            s.setSpan(new UnderlineSpan(),
                    message.indexOf(help), message.length(), 0);
            tvMessage.setText(s);

            tvMessage.setOnClickListener(new OnNeedHelpListener());
        }
    }

    protected void showSoftKeyboard(final View v) {
        View view = null;
        if (v instanceof EditText && v.isEnabled())
            view = v;
        else {
            for (View edt : views)
                if (edt instanceof EditText && edt.isEnabled())
                    view = edt;
        }
        if (view != null) {
            final View finalView = view;
            finalView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    BaseActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    InputMethodManager imm = (InputMethodManager) BaseActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(finalView, 0);
                }
            }, 100);
        }

    }

    protected void hideSoftKeyboard(final View v) {
        if (v != null) {
            v.postDelayed(new Runnable() {
                @Override
                public void run() {
                    BaseActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    InputMethodManager inputMethodManager = (InputMethodManager) BaseActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }, 100);
        }
    }

    protected void autoShowHideKeyboard(View view) {
        if (isAutoShowKeyboard)
            showSoftKeyboard(view);
        else
            hideSoftKeyboard(view);
    }

    protected void setEditTextEnable(final EditText view, boolean editable) {
        if (editable) {
            view.setEnabled(true);
            view.setFocusable(true);
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.requestFocus();
                    view.selectAll();
                }
            }, 100);

            autoShowHideKeyboard(view);
        } else {
            view.setEnabled(false);

        }
    }

    protected void selectAllEditText(EditText editText) {
        editText.setSelectAllOnFocus(true);
        editText.clearFocus();
        editText.requestFocus();
        editText.selectAll();
    }

    private ElementWrapper getElementWrapper(ElementEditText edt) {
        ViewParent grandParent = edt.getParent().getParent();

        if (grandParent instanceof TextInputLayout) {
            return (ElementWrapper) grandParent;
        }
        return null;
    }

    protected void setElementErrorText(ElementEditText edt, String message) {
        ElementWrapper wrapper = getElementWrapper(edt);
        if (wrapper != null) {
            wrapper.setError(message);
            edt.error(null);
        } else {
            edt.error(message);
        }
    }

    protected void resetWrapperError(ElementEditText edtCurrent) {
        ElementWrapper wrapper = getElementWrapper(edtCurrent);
        if (wrapper != null) {
            wrapper.resetError();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (views.size() > 0) {
                View currFocus;
                try {
                    currFocus = getViewEnable();
                } catch (Exception ex) {
                    currFocus = views.get(views.size() - 1);
                }

                if (currFocus == null) {
                    finish();
                }

                if (currFocus instanceof ElementEditText) {
                    ElementEditText edt = (ElementEditText) currFocus;
                    if (edt.getText().length() > 0) {
                        edt.getText().clear();
                        resetWrapperError(edt);
                        edt.focus();
                        return;
                    }
                } else if (currFocus instanceof EditText) {
                    EditText edt = (EditText) currFocus;
                    if (edt.getText().length() > 0) {
                        edt.getText().clear();
                        edt.requestFocus();
                        return;
                    }
                }

                int tabIndex = Integer.valueOf(currFocus.getTag().toString()); // get tag as tab index
                tabIndex = tabIndex - 1;
                View prevEdit = findViewByTag(tabIndex);
                if (prevEdit != null) {

                    //Handle previous input field
                    if (prevEdit instanceof ElementEditText) {
                        ElementEditText edt = (ElementEditText) prevEdit;
                        edt.focus();

                    } else if (prevEdit instanceof EditText) {
                        EditText edt = (EditText) prevEdit;
                        setEditTextEnable(edt, true);
                        edt.requestFocus();
                        edt.selectAll();
                    }

                    //Handle current input field
                    if (currFocus instanceof ElementEditText) {
                        ElementEditText edtv = (ElementEditText) currFocus;
                        edtv.lock();
                        resetWrapperError(edtv);

                    } else if (currFocus instanceof EditText) {
                        EditText edtv = (EditText) currFocus;
                        setEditTextEnable(edtv, false);
                    }

                } else {
                    finish();
                }
            } else {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View findViewByTag(Object tabIndex) {
        for (int i = 0; i < views.size(); i++) {
            if (views.get(i).getTag().equals(tabIndex))
                return views.get(i);
        }
        return null;
    }

    private View getViewEnable() {
        for (View v : views)
            if (v.isEnabled())
                return v;
        return null;
    }

    protected void detectInput(final EditText editText, final int length, final boolean isNumber, final IEditTextClearedListener clearedListener) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                resetErrorOnRetype(editText);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if (isKeyboardShow && !isNumber) {
                    if (s.length() > length) {
                        editText.setText(s.substring(0, length));
                        editText.setSelection(editText.getText().toString().length());
                    }
                }
                if (isNumber) {
                    if (s.length() > 0) {
                        if (s.substring(0, 1).equals("-") && s.length() > length + 1) {
                            editText.setText(s.substring(0, length + 1));
                        }
                        if (!s.substring(0, 1).equals("-") && s.length() > length) {
                            editText.setText(s.substring(0, length));
                        }
                        /**
                         * detect numberDecimal with . and -
                         */
                        if (s.length() >= 2 && s.indexOf(".") == 0) {
                            editText.setText("0" + s);
                        }
                        if (isContains(s, '.') || isContains(s, '-')) {
                            editText.setText(s.substring(0, s.length() - 1));
                        }
                        if (s.length() > 1 && s.indexOf("-") != -1 && s.indexOf("-") != 0)
                            editText.setText(s.substring(0, s.length() - 1));
                        if (s.indexOf("-") != -1 && s.indexOf("-") == 0 && s.indexOf(".") != -1 && s.indexOf(".") == 1)
                            editText.setText(s.substring(0, s.length() - 1));
                        editText.setSelection(editText.getText().toString().length());
                    }
                }
                if (s.length() == 0 && clearedListener != null)
                    clearedListener.clear(editText);
            }

            private boolean isContains(String value, Character ch) {
                int count = 0;
                for (int i = 0; i < value.length(); i++)
                    if (value.charAt(i) == ch)
                        count++;
                if (count > 1)
                    return true;
                else
                    return false;
            }
        });
    }

    protected void resetErrorOnRetype(EditText editText) {
        if (editText instanceof ElementEditText) {
            ElementEditText edt = (ElementEditText) editText;
            if (edt.getStatus() == StatusState.ERROR) {
                edt.focus();
                resetWrapperError(edt);
            }
        }
    }

    protected void exitApp() {
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        System.exit(0);
    }

    protected void setEditTextError(EditText editTextError, TextView errorView) {
        errorView.setVisibility(View.VISIBLE);
        editTextError.setBackgroundResource(R.drawable.drawable_edittext_error);
    }

    @Override
    public void networkChanged(boolean connected) {

    }

    private void recreateActivity() {
        //Delaying activity recreate by 1 millisecond. If the recreate is not delayed and is done
        // immediately in onResume() you will get RuntimeException: Performing pause of activity that is not resumed
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recreate();
            }
        }, 1);

    }

    protected boolean isNullOrEmpty(String value) {
        return value == null || value.length() == 0;
    }

    protected boolean isNumber(String value) {
        try {
            Float.parseFloat(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

            ProgressBar progressBattery = (ProgressBar) BaseActivity.this.findViewById(R.id.progressBattery);

            if (level != -1 && scale != -1) {
                int batteryPct = (int) ((level / (float) scale) * 100f);
                TextView tvBatteryPercent = (TextView) BaseActivity.this.findViewById(R.id.tvBatteryPercent);
                if(tvBatteryPercent != null){
                    tvBatteryPercent.setText(batteryPct + "%");
                }
                if(progressBattery != null)
                    progressBattery.setProgress(batteryPct);
            }
            if(status == BatteryManager.BATTERY_STATUS_CHARGING){
                if(progressBattery != null)
                    progressBattery.setProgressDrawable(getResources().getDrawable(R.drawable.battery_percent_charge));
            }
            else {
                if(progressBattery != null)
                    progressBattery.setProgressDrawable(getResources().getDrawable(R.drawable.battery_percent));
            }
            if(intent.getAction() == Intent.ACTION_POWER_CONNECTED && progressBattery != null) {
                progressBattery.setProgressDrawable(getResources().getDrawable(R.drawable.battery_percent_charge));
            }
            else if(intent.getAction() == Intent.ACTION_POWER_DISCONNECTED && progressBattery != null)
                progressBattery.setProgressDrawable(getResources().getDrawable(R.drawable.battery_percent));

            //Log.i("mBatInfoReceiver", level + " " + scale + " " + plugged + "\n" + intent.getAction());
        }
    };

    private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);

            switch (wifiState) {
                case WifiManager.WIFI_STATE_ENABLED: {
                    break;
                }

                case WifiManager.WIFI_STATE_ENABLING: {
                    break;
                }
                case WifiManager.WIFI_STATE_DISABLED: {
                    ImageView imgWifi = (ImageView) findViewById(R.id.imgWifi);
                    if(imgWifi != null){
                        imgWifi.setImageResource(R.drawable.ic_wifi_disable);
                    }
                    break;
                }

                case WifiManager.WIFI_STATE_DISABLING: {
                    break;
                }

                case WifiManager.WIFI_STATE_UNKNOWN: {
                    ImageView imgWifi = (ImageView) findViewById(R.id.imgWifi);
                    if(imgWifi != null){
                        imgWifi.setImageResource(R.drawable.ic_wifi_disable);
                    }
                    break;
                }
            }
        } };
}
