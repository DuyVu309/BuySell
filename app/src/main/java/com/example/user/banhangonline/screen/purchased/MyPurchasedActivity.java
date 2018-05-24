package com.example.user.banhangonline.screen.purchased;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.caches.SaveMyCart;
import com.example.user.banhangonline.model.DonHang;
import com.example.user.banhangonline.screen.myGioHang.MyGioHangActivity;
import com.example.user.banhangonline.screen.myGioHang.adapter.MyGioHangAdapter;
import com.example.user.banhangonline.screen.spAccount.SanPhamAccountActivity;
import com.example.user.banhangonline.utils.DialogUntils;
import com.example.user.banhangonline.utils.NetworkUtils;
import com.example.user.banhangonline.widget.dialog.DialogPositiveNegative;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.user.banhangonline.utils.KeyPreferUntils.keyStartSPAccount;


public class MyPurchasedActivity extends BaseActivity implements MyPurchasedContact.View {

    @BindView(R.id.tv_title_my_purchased)
    TextView tvTitle;

    @BindView(R.id.rv_my_purchased)
    RecyclerView rvMyChased;

    @Override
    public boolean isTransparentStatusBar() {
        return false;
    }

    Unbinder unbinder;
    MyGioHangAdapter mAdapter;
    MyPurchasedPresenter mPresenter;
    private int positonDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purchased);
        unbinder = ButterKnife.bind(this);
        mPresenter = new MyPurchasedPresenter();
        mPresenter.attachView(this);
        mPresenter.setContext(this);
        if (NetworkUtils.isConnected(this)) {
            mPresenter.getListPurchsedFromFirebase(mDataBase);
            initAdapter();
        } else {
            showNoInternet();
            mPresenter.setDonHangList(SaveMyCart.readMyCartFile(this, mPresenter.getDonHangList()));
            initAdapter();
            if (mPresenter.getDonHangList() != null) {
                tvTitle.setText(getString(R.string.don_hang_cua_ban) + " (" + mPresenter.getDonHangList().size() + ")");
            }

        }
    }

    private void initAdapter() {
        rvMyChased.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyGioHangAdapter(this, null, mPresenter.getDonHangList(), new MyGioHangAdapter.IOnClickMyCart() {
            @Override
            public void onClickMyCart(DonHang donHang) {
                DialogUntils.showDonHangInfo(MyPurchasedActivity.this, donHang);
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
                                 if (NetworkUtils.isConnected(MyPurchasedActivity.this)) {
                                     mPresenter.deletePurchsed(mDataBase, donHang);
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
                if (donHang.getIdNguoiMua() != null) {
                    Intent intent = new Intent(MyPurchasedActivity.this, SanPhamAccountActivity.class);
                    intent.putExtra(keyStartSPAccount, donHang.getIdNguoiBan());
                    startActivity(intent);
                }
            }
        });
        rvMyChased.setAdapter(mAdapter);
    }

    @OnClick(R.id.img_arrow_back)
    public void backActivity() {
        finish();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        unbinder = null;
        super.onDestroy();
    }

    @Override
    public void getPurchsedSuccess() {
        if (mAdapter != null) {
            SaveMyCart.saveMyCartFile(this, mPresenter.getDonHangList());
            if (mPresenter.getDonHangList() != null) {
                tvTitle.setText(getString(R.string.don_hang_cua_ban) + " (" + mPresenter.getDonHangList().size() + ")");
            }
            mAdapter.notifyDataSetChanged();
            dismissDialog();
        }
    }

    @Override
    public void getPurchsedError() {
        dismissDialog();
    }

    @Override
    public void deletePurchsedSuccess() {
        showSnackbar(getString(R.string.da_xoa));
        mPresenter.getDonHangList().remove(positonDelete);
        mAdapter.notifyItemRemoved(positonDelete);
        mAdapter.notifyItemRangeChanged(positonDelete, mPresenter.getDonHangList().size());
        if (mPresenter.getDonHangList() != null) {
            tvTitle.setText(getString(R.string.don_hang_cua_ban) + " (" + mPresenter.getDonHangList().size() + ")");
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void deletePurchsedError() {
        showSnackbar(getString(R.string.error_retry));
    }
}
