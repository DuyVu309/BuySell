package com.example.user.banhangonline.screen.account;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.model.Account;
import com.example.user.banhangonline.screen.account.adapter.SearchAccountAdapter;
import com.example.user.banhangonline.screen.home.fragment.adapter.SanPhamAdapter;
import com.example.user.banhangonline.screen.search.SearchActivity;
import com.example.user.banhangonline.screen.spAccount.SanPhamAccountActivity;
import com.example.user.banhangonline.untils.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.user.banhangonline.untils.KeyPreferUntils.keyStartAccount;
import static com.example.user.banhangonline.untils.KeyPreferUntils.keyStartSPAccount;

public class SearchAccountActivity extends BaseActivity implements SearchAccountContact.View {
    @BindView(R.id.rv_account)
    RecyclerView recyclerViewAc;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    public boolean isTransparentStatusBar() {
        return false;
    }

    SearchAccountAdapter mAdapter;
    Unbinder unbinder;
    SearchAccountPresenter mPresenter;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_account);
        unbinder = ButterKnife.bind(this);
        mPresenter = new SearchAccountPresenter();
        mPresenter.attachView(this);
        name = getIntent().getStringExtra(keyStartAccount);
        if (name != null) {
            if (NetworkUtils.isConnected(this)) {
                Log.d("TAG NAMe", name);
                initAdapter();
            } else {
                showNoInternet();
            }
        }
    }

    private void initAdapter() {
        showDialog();
        mPresenter.getKeyAccount(mDataBase, name);
        recyclerViewAc.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SearchAccountAdapter(recyclerViewAc, this, mPresenter.getmList(), new SearchAccountAdapter.IOnClickAccount() {
            @Override
            public void onClickAccount(Account account) {
                if (account != null) {
                    Intent intent = new Intent(SearchAccountActivity.this, SanPhamAccountActivity.class);
                    intent.putExtra(keyStartSPAccount, account.getEmailId());
                    startActivity(intent);
                    finish();
                }
            }
        });

        mAdapter.setmOnLoadMore(new SanPhamAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                progressBar.setVisibility(View.VISIBLE);
                try {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            int position = mPresenter.getTotal();
                            if (mPresenter.getmList().size() > position) {
                                mPresenter.setTotal(position + 10);
                            }
                            mPresenter.getListAccuntWithFilter(mDataBase, name);
                            mAdapter.setLoaded();
                        }
                    }, 1000);
                } catch (NullPointerException e) {

                }
            }
        });
        recyclerViewAc.setAdapter(mAdapter);
    }

    @Override
    public void getKeySuccess() {
        if (NetworkUtils.isConnected(this)) {
            if (mPresenter.getKeyList() != null) {
                if (mPresenter.getKeyList().size() != 0) {
                    mPresenter.getListAccuntWithFilter(mDataBase, name);
                } else if (mPresenter.getmList().size() == 0) {
                    showSnackbar(getString(R.string.dont_have_account));
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
    public void getListAccountSuccess() {
        Log.d("TAG SI", "" + mPresenter.getmList().size());
        if (mPresenter.getmList().size() == 0) {
            showSnackbar("Không có sản phẩm nào");
        }
        if (mAdapter != null) {
            if (mPresenter.getmList() != null) {
                Log.d("TAG  LIST", "" + mPresenter.getmList().size());
                mAdapter.notifyDataSetChanged();
                dismissDialog();
            }
        }
    }

    @Override
    public void getListAccountError() {
        showSnackbar(getString(R.string.error_retry));
        dismissDialog();
    }

    @OnClick(R.id.img_arrow_back)
    public void backActvity() {
        startActivity(new Intent(SearchAccountActivity.this, SearchActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        mPresenter.detach();
        super.onDestroy();
    }
}
