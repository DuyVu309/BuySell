package com.example.user.banhangonline.screen.home;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.interactor.prefer.PreferManager;
import com.example.user.banhangonline.model.Account;
import com.example.user.banhangonline.screen.home.adapter.HomePagerAdapter;
import com.example.user.banhangonline.screen.mySanPham.MySanPhamActivity;
import com.example.user.banhangonline.screen.search.SearchActivity;
import com.example.user.banhangonline.screen.sell.SellActivity;
import com.example.user.banhangonline.untils.NetworkUtils;
import com.example.user.banhangonline.widget.dialog.DialogPositiveNegative;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.user.banhangonline.untils.KeyUntils.keyAccountBuy;
import static com.example.user.banhangonline.untils.KeyUntils.keyAccountSell;

public class HomeActivity extends BaseActivity implements
         HomeContact.View {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.tv_title_buy_sell)
    TextView tvTitle;

    @BindView(R.id.img_sell)
    ImageView imgSell;

    @BindView(R.id.tl_home)
    TabLayout tableLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    //open drawer
    @BindView(R.id.ln_open_drawer)
    LinearLayout lnOpenDrawer;

    @BindView(R.id.img_account)
    ImageView imgAccount;

    @BindView(R.id.tv_account_name)
    TextView tvAccountName;

    @BindView(R.id.tv_trang_chu)
    TextView tvTrangChu;

    @BindView(R.id.tv_thong_bao)
    TextView tvNotify;

    @BindView(R.id.tv_cai_dat)
    TextView tvSetting;

    @BindView(R.id.tv_my_account)
    TextView tvMyAccount;

    @BindView(R.id.ln_my_account)
    LinearLayout lnMyAccount;

    @BindView(R.id.tv_dang_xuat)
    TextView tvDangXuat;

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
        getInfomationAccount();
        initFontTitle();

        viewPager.setAdapter(new HomePagerAdapter(this, getSupportFragmentManager(), mPresenter.getListCategories()));
        tableLayout.setupWithViewPager(viewPager);
        if (NetworkUtils.isConnected(this)) {
            mPresenter.getInfomationAccount(mDataBase);
        } else {
            showNoInternet();
        }
    }

    private void getInfomationAccount() {
        tvTrangChu.setSelected(true);
        String idBuySell = PreferManager.getIDBuySell(this);
        String email = PreferManager.getEmailID(this);
        String name = PreferManager.getNameAccount(this);
        String phoneNumber = PreferManager.getPhoneNumber(this);

        if (!PreferManager.getIsLogin(this)) {
            imgSell.setVisibility(View.GONE);
        } else {
            if (idBuySell != null) {
                if (idBuySell.equals(keyAccountBuy)) {
                    imgSell.setVisibility(View.GONE);
                }

                if (idBuySell.equals(keyAccountSell)) {
                    imgSell.setVisibility(View.VISIBLE);
                }
            }
        }

        if (name != null) {
            tvAccountName.setText(name + " (" + idBuySell + ")");
        }

        if (email != null) {
            tvDangXuat.append("(" + email + ")");
        }
    }

    private void initFontTitle() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Pacifico-Regular.ttf");
        tvTitle.setTypeface(typeface);
    }

    @OnClick(R.id.img_drawer)
    public void onClickOpenDrawer() {
        drawerLayout.openDrawer(lnOpenDrawer);
    }

    @OnClick(R.id.img_sell)
    public void onClickSell() {
        startActivity(new Intent(HomeActivity.this, SellActivity.class));
    }

    @OnClick(R.id.img_cart)
    public void onClickCart() {

    }

    @OnClick(R.id.edt_search)
    public void searchSanPham(){
        startActivity(new Intent(HomeActivity.this, SearchActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @OnClick(R.id.ln_account)
    public void onClickAccount() {
        if (PreferManager.getIsLogin(this)) {
            if (PreferManager.getEmailID(this) != null) {
                startActivity(new Intent(HomeActivity.this, MySanPhamActivity.class));
            }
        }
    }

    @OnClick(R.id.tv_trang_chu)
    public void onClickTrangchu() {
        tvTrangChu.setSelected(true);
        tvTrangChu.setBackgroundColor(getResources().getColor(R.color.greyish_divider));
        disableSelected(tvNotify, tvSetting, tvMyAccount);
    }

    @OnClick(R.id.tv_thong_bao)
    public void onClickThongBao() {
        tvNotify.setSelected(true);
        tvNotify.setBackgroundColor(getResources().getColor(R.color.greyish_divider));
        disableSelected(tvTrangChu, tvSetting, tvMyAccount);
    }

    @OnClick(R.id.tv_cai_dat)
    public void onClickSetting() {
        tvSetting.setSelected(true);
        tvSetting.setBackgroundColor(getResources().getColor(R.color.greyish_divider));
        disableSelected(tvTrangChu, tvNotify, tvMyAccount);
    }

    @OnClick(R.id.tv_my_account)
    public void onClickMyAccount() {
        tvMyAccount.setSelected(true);
        tvMyAccount.setBackgroundColor(getResources().getColor(R.color.greyish_divider));
        disableSelected(tvTrangChu, tvNotify, tvSetting);
        if (lnMyAccount.getVisibility() == View.GONE) {
            lnMyAccount.setVisibility(View.VISIBLE);
            tvMyAccount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_face, 0, R.drawable.ic_expand_less, 0);
        } else {
            lnMyAccount.setVisibility(View.GONE);
            tvMyAccount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_face, 0, R.drawable.ic_expand_more, 0);
        }
    }

    @OnClick(R.id.tv_trang_ca_nhan)
    public void onTrangCaNhan() {
        if (PreferManager.getIsLogin(this)) {
            if (PreferManager.getEmailID(this) != null) {
                startActivity(new Intent(HomeActivity.this, MySanPhamActivity.class));
            }
        } else {
            showSnackbar("Chưa đăng nhập");
            showConfirmDialog("Bạn có đăng nhập (đăng kí) không?", new DialogPositiveNegative.IPositiveNegativeDialogListener() {
                @Override
                public void onClickAnswerPositive(DialogPositiveNegative dialog) {
                    PreferManager.setIsLogin(HomeActivity.this, false);
                    PreferManager.setIDBuySell(HomeActivity.this, null);
                    PreferManager.setEmail(HomeActivity.this, null);
                    PreferManager.setEmailID(HomeActivity.this, null);
                    PreferManager.setNameAccount(HomeActivity.this, null);
                    PreferManager.setPhoneNumber(HomeActivity.this, null);
                    logoutUser();
                }

                @Override
                public void onClickAnswerNegative(DialogPositiveNegative dialog) {
                    dialog.dismiss();
                }
            });
        }
    }

    @OnClick(R.id.tv_dang_xuat)
    public void onClickDangXuat() {
        if (PreferManager.getIsLogin(this)) {
            showConfirmDialog("Bạn có chắc chắn muốn đăng xuất hay không?", new DialogPositiveNegative.IPositiveNegativeDialogListener() {
                @Override
                public void onClickAnswerPositive(DialogPositiveNegative dialog) {
                    logoutUser();
                }

                @Override
                public void onClickAnswerNegative(DialogPositiveNegative dialog) {
                    dialog.dismiss();
                }
            });
        }

    }

    private void disableSelected(View... views) {
        for (View view : views) {
            view.setSelected(false);
            view.setBackgroundColor(getResources().getColor(R.color.white));
        }
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
    public void getInfoSuccess(Account account) {
        imgAccount.setClipToOutline(true);
        Glide.with(this).load(account.getUrlAvt()).error(R.drawable.ic_product).into(imgAccount);
    }

    @Override
    public void getInfoError() {

    }
}
