package com.example.user.banhangonline.screen.sanphamWithIdCate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.model.Part;
import com.example.user.banhangonline.model.SanPham;
import com.example.user.banhangonline.model.search.SearchSP;
import com.example.user.banhangonline.screen.detail.SanPhamDetailActivity;
import com.example.user.banhangonline.screen.home.fragment.adapter.SanPhamAdapter;
import com.example.user.banhangonline.screen.sanphamWithIdCate.adapter.SearchAdapter;
import com.example.user.banhangonline.screen.search.allSanPham.AllSanPhamSearchedActivity;
import com.example.user.banhangonline.utils.NetworkUtils;
import com.example.user.banhangonline.utils.SortPlacesUtils;
import com.google.android.gms.maps.model.LatLng;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.user.banhangonline.utils.KeyPreferUntils.keyIdSanPham;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyStartDetail;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyStartFilter;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyStartIdCategory;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyStartIdPart;
import static com.example.user.banhangonline.utils.KeyUntils.keyIdCateThoiTrang;

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

    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;

    SanPhamAdapter mAdapter;
    SearchAdapter mAdapterSr;

    SanPhamWithIdPresenter mPresenter;
    GridLayoutManager manager;

    @Override
    public boolean isTransparentStatusBar() {
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_san_pham);
        ButterKnife.bind(this);
        mPresenter = new SanPhamWithIdPresenter();
        mPresenter.attachView(this);

        part = (Part) getIntent().getSerializableExtra(keyIdSanPham);
        if (part != null) {
            if (part.getIDCategory().equals(keyIdCateThoiTrang)) {
                if (part.getIDPart() == null) {
                    return;
                }
            }
            initData();
        }

    }

    private void initData() {
        tvTitle.setText(part.getTitle());
        mPresenter.getIdSanPhamFromFireBase(mDataBase, part.getIDCategory(), part.getIDPart());
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
        mAdapterSr = new SearchAdapter(this, mPresenter.getSearchList(), new SearchAdapter.IOnClickSearch() {
            @Override
            public void onClickSanPham(Object searchSP) {
                if (searchSP instanceof SearchSP) {
                    SearchSP sp = (SearchSP) searchSP;
                    edtSearch.setText(sp.getHeaderSp());
                    rvSearch.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(SanPhamWithIDActivity.this, AllSanPhamSearchedActivity.class);
                    intent.putExtra(keyStartFilter, sp.getHeaderSp());
                    intent.putExtra(keyStartIdCategory, part.getIDCategory());
                    intent.putExtra(keyStartIdPart, part.getIDPart());
                    startActivity(intent);
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
                mPresenter.getListSearchFromFirebase(mDataBase, edtSearch.getText().toString(), part.getIDCategory(), part.getIDPart());
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
        mAdapter = new SanPhamAdapter(recyclerViewSp, this, getMyLocation(), mPresenter.getSanPhamList(), new SanPhamAdapter.ISelectPayAdapter() {
            @Override
            public void onSelectedSanPham(SanPham sanPham) {
                if (sanPham != null) {
                    Intent intent = new Intent(SanPhamWithIDActivity.this, SanPhamDetailActivity.class);
                    intent.putExtra(keyStartDetail, sanPham);
                    startActivity(intent);
                }
            }
        });

        mAdapter.setmOnLoadMore(new SanPhamAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                pbLoading.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pbLoading.setVisibility(View.GONE);
                        int position = mPresenter.getTotal();
                        if (mPresenter.getSanPhamList().size() > position) {
                            mPresenter.setTotal(position + 10);
                        }
                        mPresenter.getSanPhamFromFirebase(mDataBase, part.getIDCategory(), part.getIDPart());
                        mAdapter.setLoaded();
                    }
                }, 1000);
            }
        });
        recyclerViewSp.setAdapter(mAdapter);
    }

    @OnClick(R.id.img_arrow_back)
    public void backActivity() {
        onBackPressed();
    }

    @OnClick(R.id.img_search)
    public void searchSpWithFilter() {
        if (NetworkUtils.isConnected(this)) {
            if (edtSearch.getText() != null) {
                Intent intent = new Intent(SanPhamWithIDActivity.this, AllSanPhamSearchedActivity.class);
                intent.putExtra(keyStartFilter, edtSearch.getText().toString().trim());
                intent.putExtra(keyStartIdCategory, part.getIDCategory());
                intent.putExtra(keyStartIdPart, part.getIDPart());
                startActivity(intent);
            }
        } else {
            showNoInternet();
        }
    }

    @Override
    public void getKeySuccess() {
        if (mPresenter.getKeyList() != null) {
            if (mPresenter.getKeyList().size() != 0) {
                mPresenter.getSanPhamFromFirebase(mDataBase, part.getIDCategory(), part.getIDPart());
            } else if (mPresenter.getKeyList().size() == 0) {
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
            dismissDialog();
        }
        dismissDialog();
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
    public void getRecentSpSuccess() {
        if (mAdapter != null) {
            try {
                Collections.sort(mPresenter.getRecentSpList(), new SortPlacesUtils(new LatLng(getMyLocation().getLatitude(), getMyLocation().getLongitude())));
            } catch (NullPointerException e) {
            }
            mAdapter.notifyDataSetChanged();
        }
        dismissDialog();
    }

    @OnClick(R.id.tv_scan)
    public void recentScan() {
        showDialog();
        mPresenter.getRecentSpFromFirebase(mDataBase, part.getIDCategory(), part.getIDPart(), getMyLocation());
        mAdapter = new SanPhamAdapter(recyclerViewSp, this, getMyLocation(), mPresenter.getRecentSpList(), new SanPhamAdapter.ISelectPayAdapter() {
            @Override
            public void onSelectedSanPham(SanPham sanPham) {
                if (sanPham != null) {
                    Intent intent = new Intent(SanPhamWithIDActivity.this, SanPhamDetailActivity.class);
                    intent.putExtra(keyStartDetail, sanPham);
                    startActivity(intent);
                }
            }
        });
        recyclerViewSp.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detach();
        super.onDestroy();
    }
}
