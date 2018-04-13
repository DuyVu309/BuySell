package com.example.user.banhangonline.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.example.user.banhangonline.R;

public class DialogProgress extends Dialog {
    public DialogProgress(@NonNull Context context) {
        super(context, R.style.FullscreenDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (getWindow() != null && getWindow().getDecorView() != null) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.dialog_progress);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frm_container);
        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
        frameLayout.startAnimation(fadeIn);
    }
}
