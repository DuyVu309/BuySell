package com.example.user.banhangonline.screen.allSanPham;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.model.SanPham;
import com.example.user.banhangonline.screen.detail.SanPhamDetailActivity;
import com.example.user.banhangonline.screen.home.fragment.adapter.SanPhamAdapter;
import com.example.user.banhangonline.screen.search.SearchActivity;
import com.example.user.banhangonline.untils.NetworkUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.user.banhangonline.untils.KeyPreferUntils.keyStartDetail;
import static com.example.user.banhangonline.untils.KeyPreferUntils.keyStartFilter;

public class AllSanPhamSearchedActivity extends BaseActivity implements AllSanPhamSearchedContact.View {
    @BindView(R.id.recycerview_sp)
    RecyclerView recyclerViewSp;

    String filter;

    Unbinder unbinder;
    AllSanPhamSearchPresenter mPresenter;
    SanPhamAdapter mAdapter;

    @Override
    public boolean isTransparentStatusBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_san_pham);
        unbinder = ButterKnife.bind(this);
        mPresenter = new AllSanPhamSearchPresenter();
        mPresenter.attachView(this);
        filter = getIntent().getStringExtra(keyStartFilter);
        if (filter != null) {
            if (NetworkUtils.isConnected(this)) {
                showDialog();
                initAdapter();
            } else {
                showNoInternet();
            }
        }
    }

    private void initAdapter() {
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recyclerViewSp.setLayoutManager(manager);
        mAdapter = new SanPhamAdapter(recyclerViewSp, this, mPresenter.getSanPhamList(), new SanPhamAdapter.ISelectPayAdapter() {
            @Override
            public void onSelectedSanPham(SanPham sanPham) {
                if (sanPham != null) {
                    Intent intent = new Intent(AllSanPhamSearchedActivity.this, SanPhamDetailActivity.class);
                    intent.putExtra(keyStartDetail, sanPham);
                    startActivity(intent);
                    finish();
                }
            }
        });
        mAdapter.setmOnLoadMore(new SanPhamAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int position = mPresenter.getTotal();
                        if (mPresenter.getSanPhamList().size() > position) {
                            mPresenter.setTotal(position + 10);
                        }
                        mPresenter.getAllSpWithFilterFromFirebase(mDataBase, filter);
                        mAdapter.setLoaded();
                    }
                }, 1000);

            }
        });
        recyclerViewSp.setAdapter(mAdapter);
        mPresenter.getAllKeySanPham(mDataBase, filter);
    }

    @Override
    public void getKeySuccess() {
        if (NetworkUtils.isConnected(this)) {
            if (mPresenter.getKeyList() != null) {
                if (mPresenter.getKeyList().size() != 0) {
                    mPresenter.getAllSpWithFilterFromFirebase(mDataBase, filter);
                } else if (mPresenter.getSanPhamList().size() == 0) {
                    showSnackbar(getString(R.string.dont_have_product));
                    dismissDialog();
                }
            }
        } else {
            showNoInternet();
        }
    }

    @Override
    public void getKeyError() {
        showSnackbar(getString(R.string.error_retry));
    }

    @Override
    public void getSpSuccess(List<SanPham> searchSPS) {
        if (searchSPS.size() == 0) {
            showSnackbar("Không có sản phẩm nào");

        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
            dismissDialog();
        }
    }

    @Override
    public void getSpError() {
        dismissDialog();
    }

    @OnClick(R.id.img_arrow_back)
    public void backActivity() {
        startActivity(new Intent(AllSanPhamSearchedActivity.this, SearchActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        mPresenter.detach();
        unbinder.unbind();
        super.onDestroy();
    }
}
