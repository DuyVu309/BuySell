package com.example.user.banhangonline.screen.showImage;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.views.PullBackLayout;
import com.example.user.banhangonline.views.ZoomPhoto;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

import static com.example.user.banhangonline.utils.KeyUntils.keyShowImage;

public class ShowImageActivity extends BaseActivity implements PullBackLayout.Callback {
    @BindView(R.id.img_show)
    ZoomPhoto imgShow;

    @BindView(R.id.pg_loading)
    ProgressBar pbLoading;

    @BindView(R.id.puller)
    PullBackLayout puller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        ButterKnife.bind(this);
        puller.setCallback(this);
        String url = getIntent().getStringExtra(keyShowImage);
        if (url != null) {
            Glide.with(this).load(url).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    pbLoading.setVisibility(View.VISIBLE);
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    pbLoading.setVisibility(View.GONE);
                    return false;
                }
            }).into(imgShow);
            PhotoViewAttacher pAttacher;
            pAttacher = new PhotoViewAttacher(imgShow);
            pAttacher.update();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    //pull
    @Override
    public void onPullStart() {

    }

    @Override
    public void onPull(float v) {

    }

    @Override
    public void onPullCancel() {

    }

    @Override
    public void onPullComplete() {
        supportFinishAfterTransition();
    }
}
