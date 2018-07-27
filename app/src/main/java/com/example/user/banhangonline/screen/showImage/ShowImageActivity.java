package com.example.user.banhangonline.screen.showImage;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
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

    private final String KEY_SAVE_IMAGE_ROTATION = "rotation";
    Bitmap bitmap;

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
                    imgShow.buildDrawingCache();
                    if (imgShow.getDrawingCache() != null) {
                        bitmap = imgShow.getDrawingCache();
                    }
                    return false;
                }
            }).into(imgShow);
            PhotoViewAttacher pAttacher;
            pAttacher = new PhotoViewAttacher(imgShow);
            pAttacher.update();
        }

        if (savedInstanceState != null) {
            imgShow.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putParcelable(KEY_SAVE_IMAGE_ROTATION, bitmap);
        super.onSaveInstanceState(outState, outPersistentState);
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
