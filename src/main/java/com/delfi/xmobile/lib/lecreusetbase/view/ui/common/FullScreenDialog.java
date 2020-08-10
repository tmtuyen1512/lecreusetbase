package com.delfi.xmobile.lib.lecreusetbase.view.ui.common;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.delfi.xmobile.lib.lecreusetbase.R;
import com.delfi.xmobile.lib.lecreusetbase.utils.IScreenEventError;
import com.delfi.xmobile.lib.lecreusetbase.utils.IScreenEventRetry;
import com.delfi.xmobile.lib.lecreusetbase.utils.IScreenEventSuccess;
import com.delfi.xmobile.lib.xcore.common.SoundManager;


/**
 * Created by USER on 05/14/2019.
 */
public class FullScreenDialog {

    private static final FullScreenDialog instance = new FullScreenDialog();

    public static FullScreenDialog getInstance() {
        return instance;
    }

    private FullScreenDialog() {
    }

    public void showErrorRetry(Context context, String message, final IScreenEventRetry callback) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(View.inflate(context.getApplicationContext(), R.layout.fragment_screen_error_retry, null));

        TextView editText = dialog.findViewById(R.id.message);
        editText.setText(message);

        Button btnRetry = dialog.findViewById(R.id.btnRetry);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null)
                    callback.onRetry();
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog1, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && (keyCode == KeyEvent.KEYCODE_ENTER ||
                        keyCode == KeyEvent.KEYCODE_ESCAPE || keyCode == KeyEvent.KEYCODE_BACK)) {
                    if (callback != null)
                        callback.onRetry();
                    dialog1.dismiss();
                }
                return false;
            }
        });
        try {
            dialog.getWindow().getAttributes().windowAnimations = R.style.Fullscreen_DialogTheme;
        } catch (Exception e) {
            //ignore
        }
        dialog.show();

        SoundManager.getInstance().PlayError(context);
    }

    public void showError(Context context, String message, final IScreenEventError callback) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(View.inflate(context.getApplicationContext(), R.layout.fragment_screen_error, null));

        TextView editText = dialog.findViewById(R.id.message);
        editText.setText(message);

        Button btnOK = dialog.findViewById(R.id.btnOK);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null)
                    callback.onOk();
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog1, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && (keyCode == KeyEvent.KEYCODE_ENTER ||
                        keyCode == KeyEvent.KEYCODE_ESCAPE || keyCode == KeyEvent.KEYCODE_BACK)) {
                    if (callback != null)
                        callback.onOk();
                    dialog1.dismiss();
                }
                return false;
            }
        });
        try {
            dialog.getWindow().getAttributes().windowAnimations = R.style.Fullscreen_DialogTheme;
        } catch (Exception e) {
            //ignore
        }
        dialog.show();

        SoundManager.getInstance().PlayError(context);
    }

    public void showSuccess(Context context, String message, final IScreenEventSuccess callback) {
        final Dialog dialog = new Dialog(context, R.style.Fullscreen_DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(View.inflate(context.getApplicationContext(), R.layout.fragment_screen_success, null));

        TextView editText = dialog.findViewById(R.id.message);
        editText.setText(message);

        Button btnOK = dialog.findViewById(R.id.btnOK);


        btnOK.setOnClickListener(new View.OnClickListener() {
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
        try {
            dialog.getWindow().getAttributes().windowAnimations = R.style.Fullscreen_DialogTheme;
        } catch (Exception e) {
            //ignore
        }
        dialog.show();

        SoundManager.getInstance().PlayOK(context.getApplicationContext());
    }

    public void showNeedHelp(final Context context) {
        final Dialog dialog = new Dialog(context, R.style.Fullscreen_DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(View.inflate(context.getApplicationContext(), R.layout.activity_need_help, null));

        final ViewGroup layoutSotiGuide = (ViewGroup) dialog.findViewById(R.id.layoutSotiGuide);
        final ImageView gifSotiGuide = (ImageView) dialog.findViewById(R.id.gifSotiGuide);
        final TextView tvSotiGuide = (TextView) dialog.findViewById(R.id.tvSotiGuide);

        final CountDownTimer myCount = new CountDownTimer(7000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

                if (millisUntilFinished >= 5000) {
                    tvSotiGuide.setText(R.string.msg_soti_admin_access_guide_step_1);
                } else if (millisUntilFinished >= 2900) {
                    tvSotiGuide.setText(R.string.msg_soti_admin_access_guide_step_2);
                } else if (millisUntilFinished >= 1100) {
                    tvSotiGuide.setText(R.string.msg_soti_admin_access_guide_step_3);
                } else {
                    tvSotiGuide.setText(R.string.msg_soti_admin_access_guide_step_4);
                }
            }

            @Override
            public void onFinish() {
                layoutSotiGuide.setVisibility(View.GONE);
            }
        };


        Button btnBack = (Button) dialog.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        View btnSkip = (View) dialog.findViewById(R.id.btnSkip);
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutSotiGuide.setVisibility(View.GONE);

                synchronized (myCount) {
                    stopAnimationDrawable();
                    myCount.cancel();
                }
            }
        });

        Button btnOpenGuide = (Button) dialog.findViewById(R.id.btnOpenGuide);
        btnOpenGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutSotiGuide.setVisibility(View.VISIBLE);
                gifSotiGuide.setBackgroundResource(R.drawable.gif_access_admin_guide_layers_list);

                synchronized (myCount) {
                    startAnimationDrawable(gifSotiGuide);
                    myCount.start();
                }
            }
        });

        final ScrollView scrollView = (ScrollView) dialog.findViewById(R.id.scrollView);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog1, int keyCode, KeyEvent mEvent) {
                if (mEvent.getAction() == KeyEvent.ACTION_UP &&
                        (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_ESCAPE || keyCode == KeyEvent.KEYCODE_BACK)) {
                    dialog1.dismiss();
                }

                return false;
            }
        });
        try {
            dialog.getWindow().getAttributes().windowAnimations = R.style.Fullscreen_DialogTheme;
        } catch (Exception e) {
            //ignore
        }
        dialog.show();
    }

    private AnimationDrawable frameAnimation;

    private synchronized void startAnimationDrawable(ImageView imageView) {
        try {
            //stop current
            stopAnimationDrawable();

            //start new anim
            frameAnimation = (AnimationDrawable) imageView.getBackground();
            frameAnimation.setOneShot(true);
            frameAnimation.start();

        } catch (Exception e) { /* ignore */}
    }

    private synchronized void stopAnimationDrawable() {
        try {
            if (frameAnimation != null && frameAnimation.isRunning()) {
                frameAnimation.stop();
                frameAnimation = null;
            }
        } catch (Exception e) { /* ignore */}
    }
}
