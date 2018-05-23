package com.example.user.banhangonline.screen.myGioHang;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.caches.SaveMyCart;
import com.example.user.banhangonline.interactor.prefer.PreferManager;
import com.example.user.banhangonline.model.DonHang;
import com.example.user.banhangonline.screen.maps.MapsTotalMyCartActivity;
import com.example.user.banhangonline.screen.myGioHang.adapter.MyGioHangAdapter;
import com.example.user.banhangonline.screen.purchased.MyPurchasedActivity;
import com.example.user.banhangonline.screen.spAccount.SanPhamAccountActivity;
import com.example.user.banhangonline.utils.DialogUntils;
import com.example.user.banhangonline.utils.NetworkUtils;
import com.example.user.banhangonline.widget.dialog.DialogPositiveNegative;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.user.banhangonline.utils.KeyPreferUntils.keyStartListAddress;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyStartSPAccount;

public class MyGioHangActivity extends BaseActivity implements MyGioHangContact.View {

    @BindView(R.id.rv_my_cart)
    RecyclerView rvMyCart;

    @BindView(R.id.tv_title_my_cart)
    TextView tvTitle;

    MyGioHangPresenter mPresenter;
    private MyGioHangAdapter mAdapter;
    private int positonDelete;

    @Override
    public boolean isTransparentStatusBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gio_hang);
        ButterKnife.bind(this);
        mPresenter = new MyGioHangPresenter();
        mPresenter.attachView(this);
        mPresenter.setContext(this);

        if (PreferManager.getUserID(this) != null) {
            if (NetworkUtils.isConnected(this)) {
                if (mPresenter.getmList() != null) {
                    mPresenter.getListCartFromFirebase(mDataBase);
                    initAdapter();
                }
            } else {
                mPresenter.setmList(SaveMyCart.readMyCartFile(this, mPresenter.getmList()));
                initAdapter();
                tvTitle.setText(getString(R.string.don_hang_cua_ban) + " (" + mPresenter.getmList().size() + ")");
            }
        }

    }

    private void initAdapter() {
        rvMyCart.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyGioHangAdapter(this, getMyLocation(), mPresenter.getmList(), new MyGioHangAdapter.IOnClickMyCart() {
            @Override
            public void onClickMyCart(DonHang donHang) {
                DialogUntils.showDonHangInfo(MyGioHangActivity.this, donHang);
            }

            @Override
            public void onOptionSelectedMyCart(final int postion, final DonHang donHang) {
                positonDelete = postion;
                showConfirmDialog(getString(R.string.xoa_don_hang),
                         getString(R.string.xac_nhan_xoa_don_hang),
                         getString(R.string.xac_nhan),
                         getString(R.string.huy),
                         new DialogPositiveNegative.IPositiveNegativeDialogListener() {
                             @Override
                             public void onClickAnswerPositive(DialogPositiveNegative dialog) {
                                 if (NetworkUtils.isConnected(MyGioHangActivity.this)) {
                                     mPresenter.deleteDonHang(mDataBase, donHang);
                                 } else {
                                     showNoInternet();
                                 }
                                 dialog.dismiss();
                             }

                             @Override
                             public void onClickAnswerNegative(DialogPositiveNegative dialog) {
                                 dialog.dismiss();
                             }
                         });
            }

            @Override
            public void onClickInfoAccount(DonHang donHang) {
                if (donHang.getIdNguoiBan() != null) {
                    Intent intent = new Intent(MyGioHangActivity.this, SanPhamAccountActivity.class);
                    intent.putExtra(keyStartSPAccount, donHang.getIdNguoiMua());
                    startActivity(intent);
                }
            }
        });
        rvMyCart.setAdapter(mAdapter);
    }

    @OnClick(R.id.img_arrow_back)
    public void backAcivity() {
        finish();
    }

    @OnClick(R.id.img_maps)
    public void startActivtyMaps() {
        List<String> address = new ArrayList<>();
        for (DonHang donHang : mPresenter.getmList()) {
            if (donHang != null) {
                if (donHang.getDiaChi() != null) {
                    address.add(donHang.getDiaChi());
                }
            }
        }

        if (address.size() == mPresenter.getmList().size()) {
            Intent intent = new Intent(MyGioHangActivity.this, MapsTotalMyCartActivity.class);
            intent.putExtra(keyStartListAddress, (Serializable) address);
            startActivity(intent);
        }

    }

    @Override
    public void getCartSuccess() {
        if (mPresenter.getmList().size() == 0) {
            showSnackbar(getString(R.string.dont_have_product));
            dismissDialog();
        }
        if (mAdapter != null) {
            SaveMyCart.saveMyCartFile(this, mPresenter.getmList());
            tvTitle.setText(getString(R.string.don_hang_cua_ban) + " (" + mPresenter.getmList().size() + ")");
            mAdapter.notifyDataSetChanged();
            dismissDialog();
        }
    }

    @Override
    public void getCartError() {
        dismissDialog();
    }

    @Override
    public void deleteDonHangSuccess() {
        showSnackbar(getString(R.string.da_xoa));
        mPresenter.getmList().remove(positonDelete);
        mAdapter.notifyItemRemoved(positonDelete);
        mAdapter.notifyItemRangeChanged(positonDelete, mPresenter.getmList().size());
        tvTitle.setText(getString(R.string.don_hang_cua_ban) + " (" + mPresenter.getmList().size() + ")");
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteDonHangError() {
        showSnackbar(getString(R.string.error_retry));
    }

    @Override
    protected void onDestroy() {
        mPresenter.detach();
        super.onDestroy();
    }

}
