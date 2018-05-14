package com.example.user.banhangonline.screen.search.account;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.model.Account;
import com.example.user.banhangonline.screen.search.account.adapter.SearchAccountAdapter;
import com.example.user.banhangonline.screen.home.fragment.adapter.SanPhamAdapter;
import com.example.user.banhangonline.screen.spAccount.SanPhamAccountActivity;
import com.example.user.banhangonline.untils.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.user.banhangonline.AppConstants.REQUEST_CALL_PHONE;
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

            @SuppressLint("MissingPermission")
            @Override
            public void onClickPhoneNumber(String phoneNumber) {
                String s[] = {Manifest.permission.CALL_PHONE};
                if (checkPermissions(s)) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phoneNumber));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(callIntent);
                } else {
                    ActivityCompat.requestPermissions(SearchAccountActivity.this, s, REQUEST_CALL_PHONE);
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
        if (mAdapter != null) {
            if (mPresenter.getmList() != null) {
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
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        mPresenter.detach();
        super.onDestroy();
    }
}
