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

public class MyGioHangActivity extends BaseActivity implements MyGioHangContact.View {

    @BindView(R.id.rv_my_cart)
    RecyclerView rvMyCart;

    @BindView(R.id.tv_title_my_cart)
    TextView tvTitle;

    MyGioHangPresenter mPresenter;
    MyGioHangAdapter mAdapter;
    int positonDelete;

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
                mPresenter.getListCartFromFirebase(mDataBase);
                if (mPresenter.getmList() != null) {
                    initAdapter();
                    mPresenter.getAllKeyMyCartFromFirebase(mDataBase, PreferManager.getUserID(this));
                }
            } else {
                mPresenter.setmList(SaveMyCart.readMyCartFile(this, mPresenter.getmList()));
                createAdapter();
                tvTitle.setText(getString(R.string.don_hang_cua_ban) + " " + mPresenter.getmList().size());
            }
        }

    }

    private void initAdapter() {
        createAdapter();

        mAdapter.setmOnLoadMore(new MyGioHangAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPresenter.getmList().add(null);
                mAdapter.notifyItemInserted(mPresenter.getmList().size() - 1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.getmList().remove(mPresenter.getmList().size() - 1);
                        mAdapter.notifyItemRemoved(mPresenter.getmList().size());
                        int position = mPresenter.getTotal();
                        if (mPresenter.getmList().size() > position) {
                            mPresenter.setTotal(position + 10);
                        }
                        mPresenter.getListCartFromFirebase(mDataBase);
                        mAdapter.setLoaded();
                    }
                }, 1000);
            }
        });
    }


    private void createAdapter() {
        rvMyCart.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyGioHangAdapter(rvMyCart, this, getMyLocation(), mPresenter.getmList(), new MyGioHangAdapter.IOnClickMyCart() {
            @Override
            public void onClickMyCart(DonHang donHang) {
                DialogUntils.showDonHangInfo(MyGioHangActivity.this, donHang);
            }

            @Override
            public void onOptionSelectedMyCart(int postion, final DonHang donHang) {
                positonDelete = postion;
                showConfirmDialog(getString(R.string.xoa_don_hang),
                         getString(R.string.xac_nhan_xoa_don_hang),
                         getString(R.string.xac_nhan),
                         getString(R.string.huy),
                         new DialogPositiveNegative.IPositiveNegativeDialogListener() {
                             @Override
                             public void onClickAnswerPositive(DialogPositiveNegative dialog) {
                                 mPresenter.deleteDonHang(mDataBase, donHang);
                                 dialog.dismiss();
                             }

                             @Override
                             public void onClickAnswerNegative(DialogPositiveNegative dialog) {
                                 dialog.dismiss();
                             }
                         });
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

        if (address != null) {
            Intent intent = new Intent(MyGioHangActivity.this, MapsTotalMyCartActivity.class);
            intent.putExtra(keyStartListAddress, (Serializable) address);
            startActivity(intent);
        }

    }

    @Override
    public void getAllKeySuccess() {
        if (NetworkUtils.isConnected(this)) {
            if (mPresenter.getmListKey() != null) {
                if (mPresenter.getmListKey().size() != 0) {
                    mPresenter.getListCartFromFirebase(mDataBase);
                } else if (mPresenter.getmListKey().size() == 0) {
                    dismissDialog();
                }
            }
        } else {
            dismissDialog();
            showNoInternet();
        }
    }

    @Override
    public void getAllKeyError() {
        showSnackbar(getString(R.string.error_retry));
        dismissDialog();
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
