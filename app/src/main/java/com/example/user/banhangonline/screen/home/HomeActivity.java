package com.example.user.banhangonline.screen.home;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.widget.TextView;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.screen.home.adapter.HomePagerAdapter;
import com.example.user.banhangonline.screen.home.fragment.PayFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class HomeActivity extends BaseActivity implements
         HomeContact.View {

    @BindView(R.id.tv_title_buy_sell)
    TextView tvTitle;

    @BindView(R.id.tl_home)
    TabLayout tableLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private Unbinder unbinder;
    private HomePresenter mPresenter;

    @Override
    public boolean isTransparentStatusBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        unbinder = ButterKnife.bind(this);
        mPresenter = new HomePresenter();
        mPresenter.onCreate();
        mPresenter.setContext(this);
        mPresenter.attachView(this);
        initFontTitle();

        viewPager.setAdapter(new HomePagerAdapter(this, getSupportFragmentManager(), mPresenter.getListCategories()));
        tableLayout.setupWithViewPager(viewPager);
    }

    private void initFontTitle() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Pacifico-Regular.ttf");
        tvTitle.setTypeface(typeface);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        mPresenter.detach();
        unbinder.unbind();
        unbinder = null;
        super.onDestroy();

    }

    @Override
    public void loadPaySuccess() {

    }

    @Override
    public void loadPayError() {

    }
}
