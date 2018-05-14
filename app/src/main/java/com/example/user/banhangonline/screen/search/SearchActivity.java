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
import com.example.user.banhangonline.model.search.SearchAccount;
import com.example.user.banhangonline.model.search.SearchSP;
import com.example.user.banhangonline.screen.search.account.SearchAccountActivity;
import com.example.user.banhangonline.screen.search.allSanPham.AllSanPhamSearchedActivity;
import com.example.user.banhangonline.screen.sanphamWithIdCate.adapter.SearchAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.user.banhangonline.untils.KeyPreferUntils.keyStartAccount;
import static com.example.user.banhangonline.untils.KeyPreferUntils.keyStartFilter;

public class SearchActivity extends BaseActivity implements SearchContact.View {

    @BindView(R.id.edt_search)
    EditText edtSearch;

    @BindView(R.id.edt_search_account)
    EditText edtSearchAccount;

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
        mAdapter = new SearchAdapter(SearchActivity.this, mPresenter.getListSearchs(), new SearchAdapter.IOnClickSearch() {
            @Override
            public void onClickSanPham(Object search) {
                if (search != null) {
                    if (search instanceof SearchSP) {
                        Intent intent = new Intent(SearchActivity.this, AllSanPhamSearchedActivity.class);
                        intent.putExtra(keyStartFilter, ((SearchSP) search).getHeaderSp());
                        startActivity(intent);
                    } else if (search instanceof SearchAccount) {
                        Intent intent = new Intent(SearchActivity.this, SearchAccountActivity.class);
                        intent.putExtra(keyStartAccount, ((SearchAccount) search).getNameAc());
                        startActivity(intent);
                    }
                }
            }
        });
        rvSearch.setAdapter(mAdapter);

    }

    @OnClick(R.id.img_arrow_back)
    public void backActivity() {
        onBackPressed();
    }

    @OnClick(R.id.img_search)
    public void searchSanPham() {
        if (!edtSearch.getText().toString().trim().isEmpty()) {
            Intent intent = new Intent(SearchActivity.this, AllSanPhamSearchedActivity.class);
            intent.putExtra(keyStartFilter, edtSearch.getText().toString());
            startActivity(intent);
        }
    }

    @OnClick(R.id.img_search_account)
    public void searchAccount() {
        if (!edtSearchAccount.getText().toString().trim().isEmpty()) {
            Intent intent = new Intent(SearchActivity.this, SearchAccountActivity.class);
            intent.putExtra(keyStartAccount, edtSearchAccount.getText().toString());
            startActivity(intent);
        }
    }

    private void initSearch() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mPresenter.getTextSearchProduct(mDataBase, edtSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtSearchAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mPresenter.getTextSearchAccount(mDataBase, edtSearchAccount.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void getFilterSuccess(List<Object> searchSPList) {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
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
