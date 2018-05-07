package com.example.user.banhangonline.screen.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.model.SearchSP;
import com.example.user.banhangonline.screen.allSanPham.AllSanPhamSearchedActivity;
import com.example.user.banhangonline.screen.home.HomeActivity;
import com.example.user.banhangonline.screen.sanphamWithId.adapter.SearchAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.user.banhangonline.untils.KeyPreferUntils.keyStartFilter;

public class SearchActivity extends BaseActivity implements SearchContact.View {

    @BindView(R.id.edt_search)
    EditText edtSearch;

    @BindView(R.id.rv_search)
    RecyclerView rvSearch;

    SearchAdapter mAdapter;
    Unbinder unbinder;
    SearchPresenter mPresenter;

    @Override
    public boolean isTransparentStatusBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        unbinder = ButterKnife.bind(this);
        mPresenter = new SearchPresenter();
        mPresenter.attachView(this);
        initAdapter();
        initSearch();
    }

    private void initAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvSearch.setLayoutManager(manager);
        rvSearch.setHasFixedSize(true);
        mAdapter = new SearchAdapter(this, mPresenter.getListSearchs(), new SearchAdapter.IOnClickSearch() {
            @Override
            public void onClickSanPham(SearchSP searchSP) {
                if (searchSP != null) {
                    edtSearch.setText(searchSP.getHeaderSp().toString());
                    Intent intent = new Intent(SearchActivity.this, AllSanPhamSearchedActivity.class);
                    intent.putExtra(keyStartFilter, searchSP.getHeaderSp());
                    startActivity(intent);
                }
            }
        });
        rvSearch.setAdapter(mAdapter);
    }

    @OnClick(R.id.img_arrow_back)
    public void backActivity() {
        startActivity(new Intent(SearchActivity.this, HomeActivity.class));
        finish();
    }

    @OnClick(R.id.img_search)
    public void searchSanPham() {
        if (!edtSearch.getText().toString().isEmpty()) {
            Intent intent = new Intent(SearchActivity.this, AllSanPhamSearchedActivity.class);
            intent.putExtra(keyStartFilter, edtSearch.getText().toString());
            startActivity(intent);
            finish();
        }
    }

    private void initSearch() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mPresenter.getTextSearch(mDataBase, edtSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public void getFilterSuccess(List<SearchSP> searchSPList) {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getFilterError() {
        showSnackbar(getString(R.string.error));
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        mPresenter.detach();
        super.onDestroy();
    }
}
