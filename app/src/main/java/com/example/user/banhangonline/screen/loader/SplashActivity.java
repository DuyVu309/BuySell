package com.example.user.banhangonline.screen.loader;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.screen.home.HomeActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);
        ButterKnife.bind(this);
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
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
