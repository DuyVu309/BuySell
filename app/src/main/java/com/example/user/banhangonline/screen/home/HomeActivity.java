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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.interactor.prefer.PreferManager;
import com.example.user.banhangonline.model.Account;
import com.example.user.banhangonline.screen.feedback.FeedBackActivity;
import com.example.user.banhangonline.screen.home.adapter.HomePagerAdapter;
import com.example.user.banhangonline.screen.myGioHang.MyGioHangActivity;
import com.example.user.banhangonline.screen.mySanPham.MySanPhamActivity;
import com.example.user.banhangonline.screen.purchased.MyPurchasedActivity;
import com.example.user.banhangonline.screen.search.SearchActivity;
import com.example.user.banhangonline.screen.sell.SellActivity;
import com.example.user.banhangonline.screen.setting.SettingActivity;
import com.example.user.banhangonline.utils.NetworkUtils;
import com.example.user.banhangonline.widget.dialog.DialogPositiveNegative;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.user.banhangonline.utils.KeyUntils.keyAccountBuy;
import static com.example.user.banhangonline.utils.KeyUntils.keyAccountSell;

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

    @BindView(R.id.tv_tinh_nang_vip)
    TextView tvTinhNangVip;

    @BindView(R.id.tv_feedback)
    TextView tvFeedback;

    @BindView(R.id.tv_trang_ca_nhan)
    TextView tvMyAccount;

    @BindView(R.id.tv_cai_dat)
    TextView tvCaidat;

    @BindView(R.id.tv_version_app)
    TextView tvVersionApp;

    @BindView(R.id.tv_dang_xuat)
    TextView tvDangXuat;

    private HomePresenter mPresenter;

    @Override
    public boolean isTransparentStatusBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
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
        String email = PreferManager.getEmail(this);
        String name = PreferManager.getNameAccount(this);

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
        tvVersionApp.setText(getString(R.string.phien_ban) + " " + getVersionApp());
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
        if (!PreferManager.getIsLogin(this)) {
            showSnackbar(getString(R.string.chua_dang_nhap));
            showConfirmDialog(getString(R.string.ban_co_muon_dn_dk_khong), new DialogPositiveNegative.IPositiveNegativeDialogListener() {
                @Override
                public void onClickAnswerPositive(DialogPositiveNegative dialog) {
                    PreferManager.setIsLogin(HomeActivity.this, false);
                    PreferManager.setIDBuySell(HomeActivity.this, null);
                    PreferManager.setEmail(HomeActivity.this, null);
                    PreferManager.setUserID(HomeActivity.this, null);
                    PreferManager.setNameAccount(HomeActivity.this, null);
                    PreferManager.setPhoneNumber(HomeActivity.this, null);
                    logoutUser();
                }

                @Override
                public void onClickAnswerNegative(DialogPositiveNegative dialog) {
                    dialog.dismiss();
                }
            });
        } else if (PreferManager.getIDBuySell(this) != null) {
            if (PreferManager.getIDBuySell(this).equals(keyAccountSell)) {
                startActivity(new Intent(HomeActivity.this, MyGioHangActivity.class));
            } else if (PreferManager.getIDBuySell(this).equals(keyAccountBuy)) {
                startActivity(new Intent(HomeActivity.this, MyPurchasedActivity.class));
            }
        }
    }

    @OnClick(R.id.edt_search)
    public void searchSanPham() {
        startActivity(new Intent(HomeActivity.this, SearchActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @OnClick(R.id.ln_account)
    public void onClickAccount() {
        if (PreferManager.getIsLogin(this)) {
            if (PreferManager.getUserID(this) != null) {
                startActivity(new Intent(HomeActivity.this, MySanPhamActivity.class));
            }
        }
    }

    @OnClick(R.id.tv_trang_chu)
    public void onClickTrangchu() {
        tvTrangChu.setSelected(true);
        disableSelected(tvMyAccount, tvCaidat, tvTinhNangVip, tvFeedback);
        drawerLayout.closeDrawer(lnOpenDrawer);
    }

    @OnClick(R.id.tv_cai_dat)
    public void onClickCaiDat() {
        tvCaidat.setSelected(true);
        disableSelected(tvTrangChu, tvMyAccount, tvTinhNangVip, tvFeedback);
        drawerLayout.closeDrawer(lnOpenDrawer);
        startActivity(new Intent(HomeActivity.this, SettingActivity.class));
    }

    @OnClick(R.id.tv_tinh_nang_vip)
    public void onClickVIP() {
        tvTinhNangVip.setSelected(true);
        disableSelected(tvTrangChu, tvMyAccount, tvCaidat, tvFeedback);
        drawerLayout.closeDrawer(lnOpenDrawer);
        Toast.makeText(this, R.string.vip_dang_phat_trien, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.tv_feedback)
    public void feedback() {
        tvFeedback.setSelected(true);
        disableSelected(tvTrangChu, tvMyAccount, tvCaidat, tvTinhNangVip);
        drawerLayout.closeDrawer(lnOpenDrawer);
        startActivity(new Intent(HomeActivity.this, FeedBackActivity.class));
    }

    @OnClick(R.id.tv_trang_ca_nhan)
    public void onTrangCaNhan() {
        tvMyAccount.setSelected(true);
        disableSelected(tvTrangChu, tvTinhNangVip, tvCaidat, tvFeedback);
        drawerLayout.closeDrawer(lnOpenDrawer);
        if (PreferManager.getIsLogin(this)) {
            if (PreferManager.getUserID(this) != null) {
                startActivity(new Intent(HomeActivity.this, MySanPhamActivity.class));
            }
        } else {
            showSnackbar(getString(R.string.chua_dang_nhap));
            showConfirmDialog(getString(R.string.ban_co_muon_dn_dk_khong), new DialogPositiveNegative.IPositiveNegativeDialogListener() {
                @Override
                public void onClickAnswerPositive(DialogPositiveNegative dialog) {
                    PreferManager.setIsLogin(HomeActivity.this, false);
                    PreferManager.setIDBuySell(HomeActivity.this, null);
                    PreferManager.setEmail(HomeActivity.this, null);
                    PreferManager.setUserID(HomeActivity.this, null);
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
            showConfirmDialog(getString(R.string.ban_muon_dang_xuat_khong), new DialogPositiveNegative.IPositiveNegativeDialogListener() {
                @Override
                public void onClickAnswerPositive(DialogPositiveNegative dialog) {
                    PreferManager.setIsLogin(HomeActivity.this, false);
                    PreferManager.setIDBuySell(HomeActivity.this, null);
                    PreferManager.setEmail(HomeActivity.this, null);
                    PreferManager.setUserID(HomeActivity.this, null);
                    PreferManager.setNameAccount(HomeActivity.this, null);
                    PreferManager.setPhoneNumber(HomeActivity.this, null);
                    PreferManager.setMyAddress(HomeActivity.this, null);
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
            if (view.isSelected()) {
                view.setSelected(false);
                view.setBackgroundColor(getResources().getColor(R.color.white));
            }

        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        mPresenter.detach();
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
