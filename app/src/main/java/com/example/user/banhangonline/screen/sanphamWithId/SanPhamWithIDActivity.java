package com.example.user.banhangonline.screen.sanphamWithId;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.model.Part;
import com.example.user.banhangonline.model.SanPham;
import com.example.user.banhangonline.model.search.SearchSP;
import com.example.user.banhangonline.screen.allSanPham.AllSanPhamSearchedActivity;
import com.example.user.banhangonline.screen.detail.SanPhamDetailActivity;
import com.example.user.banhangonline.screen.home.fragment.adapter.SanPhamAdapter;
import com.example.user.banhangonline.screen.sanphamWithId.adapter.SearchAdapter;
import com.example.user.banhangonline.untils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.user.banhangonline.untils.KeyPreferUntils.keyIdSanPham;
import static com.example.user.banhangonline.untils.KeyPreferUntils.keyStartDetail;
import static com.example.user.banhangonline.untils.KeyPreferUntils.keyStartFilter;

public class SanPhamWithIDActivity extends BaseActivity implements SanPhamWithIdContact.View {
    private Part part;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.edt_search)
    EditText edtSearch;

    @BindView(R.id.rv_sanpham)
    RecyclerView recyclerViewSp;

    @BindView(R.id.rv_search)
    RecyclerView rvSearch;

    SanPhamAdapter mAdapter;
    SearchAdapter mAdapterSr;

    Unbinder unbinder;
    SanPhamWithIdPresenter mPresenter;
    GridLayoutManager manager;
    List<Object> filteredList;

    @Override
    public boolean isTransparentStatusBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_san_pham);
        unbinder = ButterKnife.bind(this);
        mPresenter = new SanPhamWithIdPresenter();
        mPresenter.attachView(this);
        part = (Part) getIntent().getSerializableExtra(keyIdSanPham);
        if (part != null) {
            initData();
        }
    }

    private void initData() {
        tvTitle.setText(part.getTitle());
        mPresenter.getIdSanPhamFromFireBase(mDataBase, part.getIDCategory(), part.getIDPay());
        if (NetworkUtils.isConnected(this)) {
            showDialog();
            initAdapterSp();
            initAdapterSearch();
        } else {
            showNoInternet();
        }
    }

    private void initAdapterSearch() {
        rvSearch.setLayoutManager(new LinearLayoutManager(this));
        filteredList = new ArrayList<>();
        mAdapterSr = new SearchAdapter(this, mPresenter.getSearchList(), new SearchAdapter.IOnClickSearch() {
            @Override
            public void onClickSanPham(Object searchSP) {
                if (searchSP instanceof SearchSP) {
                    SearchSP sp = (SearchSP) searchSP;
                    edtSearch.setText(sp.getHeaderSp());
                    rvSearch.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(SanPhamWithIDActivity.this, AllSanPhamSearchedActivity.class);
                    intent.putExtra(keyStartFilter, sp.getHeaderSp());
                    startActivity(intent);
                    finish();
                }
            }
        });
        rvSearch.setAdapter(mAdapterSr);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int x, int i1, int i2) {
                mPresenter.getListSearchFromFirebase(mDataBase, edtSearch.getText().toString(), part.getIDCategory(), part.getIDPay());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) {
                    rvSearch.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void initAdapterSp() {
        manager = new GridLayoutManager(this, 2);
        recyclerViewSp.setLayoutManager(manager);
        mAdapter = new SanPhamAdapter(recyclerViewSp, this, mPresenter.getSanPhamList(), new SanPhamAdapter.ISelectPayAdapter() {
            @Override
            public void onSelectedSanPham(SanPham sanPham) {
                if (sanPham != null) {
                    Intent intent = new Intent(SanPhamWithIDActivity.this, SanPhamDetailActivity.class);
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
                        mPresenter.getSanPhamFromFirebase(mDataBase, part.getIDCategory(), part.getIDPay());
                        mAdapter.setLoaded();
                    }
                }, 1000);
            }
        });
        recyclerViewSp.setAdapter(mAdapter);
    }

    @OnClick(R.id.img_arrow_back)
    public void backActivity() {
        finish();
    }

    @OnClick(R.id.img_search)
    public void searchSpWithFilter() {
        if (NetworkUtils.isConnected(this)) {
            if (edtSearch.getText() != null) {
                Intent intent = new Intent(SanPhamWithIDActivity.this, AllSanPhamSearchedActivity.class);
                intent.putExtra(keyStartFilter, edtSearch.getText().toString().trim());
                startActivity(intent);
                finish();
            }
        } else {
            showNoInternet();
        }

    }

    @Override
    public void getKeySuccess() {
        if (mPresenter.getKeyList() != null) {
            if (mPresenter.getKeyList().size() != 0) {
                mPresenter.getSanPhamFromFirebase(mDataBase, part.getIDCategory(), part.getIDPay());
            } else if (mPresenter.getKeyList().size() == 0) {
                showSnackbar(getString(R.string.dont_have_product));
                dismissDialog();
            }
        }
    }

    @Override
    public void getKeyError() {
        showSnackbar(getString(R.string.error_retry));
    }

    @Override
    public void getSpSuccess() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
            mAdapterSr.notifyDataSetChanged();
            dismissDialog();
        }

    }

    @Override
    public void getSpError() {
        dismissDialog();
    }

    @Override
    public void getSearchSuccess() {
        if (mAdapterSr != null) {
            mAdapterSr.notifyDataSetChanged();
        }
    }

    @Override
    public void getSearchError() {
        showSnackbar(getString(R.string.error));
    }

    @Override
    protected void onDestroy() {
        mPresenter.detach();
        unbinder.unbind();
        super.onDestroy();
    }
}
