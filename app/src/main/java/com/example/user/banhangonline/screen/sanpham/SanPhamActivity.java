package com.example.user.banhangonline.screen.sanpham;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.model.Part;
import com.example.user.banhangonline.model.SanPham;
import com.example.user.banhangonline.screen.home.fragment.adapter.SanPhamAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.user.banhangonline.untils.KeyPreferUntils.keyIdSanPham;

public class SanPhamActivity extends BaseActivity implements SanPhamContact.View {
    private Part part;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.edt_search)
    EditText edtSearch;

    @BindView(R.id.img_search)
    ImageView imgSearch;

    @BindView(R.id.rv_sanpham)
    RecyclerView recyclerViewSp;

    @BindView(R.id.pg_loading)
    ProgressBar progressBar;

    SanPhamAdapter mAdapter;

    Unbinder unbinder;
    SanPhamPresenter mPresenter;
    GridLayoutManager manager;

    @Override
    public boolean isTransparentStatusBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_san_pham);
        unbinder = ButterKnife.bind(this);
        mPresenter = new SanPhamPresenter();
        mPresenter.attachView(this);
        mPresenter.setContext(this);
        part = (Part) getIntent().getSerializableExtra(keyIdSanPham);
        if (part != null) {
            initData();
        }
    }

    private void initData() {
        tvTitle.setText(part.getTitle());
        mPresenter.getIdSanPhamFromFireBase(mDataBase, part.getIDCategory());
        showDialog();
        initAdapter();
    }

    private void initAdapter() {
        manager = new GridLayoutManager(this, 2);
        recyclerViewSp.setLayoutManager(manager);
        mAdapter = new SanPhamAdapter(recyclerViewSp, this, mPresenter.getSanPhamList(), new SanPhamAdapter.ISelectPayAdapter() {
            @Override
            public void onSelectedSanPham(SanPham sanPham) {
                Toast.makeText(SanPhamActivity.this, sanPham.getIdCategory(), Toast.LENGTH_SHORT).show();
            }
        });

        mAdapter.setmOnLoadMore(new SanPhamAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                progressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        int position = mPresenter.getTotal();
                        if (mPresenter.getSanPhamList().size() > position) {
                            mPresenter.setTotal(position + 10);
                        }
                        mPresenter.getSanPhamFromFirebase(mDataBase, part.getIDCategory());
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

    @Override
    public void getKeySuccess() {
        mPresenter.getSanPhamFromFirebase(mDataBase, part.getIDCategory());
    }

    @Override
    public void getKeyError() {
        showSnackbar(getString(R.string.error_retry));
    }

    @Override
    public void getSpSuccess(List<SanPham> sanPham) {
        mAdapter.notifyDataSetChanged();
        dismissDialog();
    }

    @Override
    public void getSpError() {
        dismissDialog();
    }

    @Override
    protected void onDestroy() {
        mPresenter.detach();
        unbinder.unbind();
        super.onDestroy();
    }

}
