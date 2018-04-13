package com.example.user.banhangonline.screen.loader;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.interactor.prefer.PreferManager;
import com.example.user.banhangonline.screen.home.HomeActivity;
import com.example.user.banhangonline.screen.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LoaderActivity extends AppCompatActivity {


    @BindView(R.id.tv_loader)
    TextView tvLoader;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);
        unbinder = ButterKnife.bind(this);
        initFontLoader();
        initProgress();
    }

    private void initProgress() {
        progressBar.setProgress(0);
        startProgress();
    }

    private void startProgress() {
        smoothProgress(80, 1600, new Runnable() {
            @Override
            public void run() {
                finishProgress();
            }
        });
    }

    private void finishProgress() {
        smoothProgress(100, 400, new Runnable() {
            @Override
            public void run() {
                if (PreferManager.getIsLogin(LoaderActivity.this)) {
                    Intent intent = new Intent(LoaderActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(LoaderActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    private void smoothProgress(int toProgress, int inMilliseconds, final Runnable endRunnable) {
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", toProgress);
        animation.setDuration(inMilliseconds);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.removeAllListeners();
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                endRunnable.run();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animation.start();
    }

    private void initFontLoader() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Pacifico-Regular.ttf");
        tvLoader.setTypeface(typeface);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        unbinder = null;
        super.onDestroy();
    }
}
