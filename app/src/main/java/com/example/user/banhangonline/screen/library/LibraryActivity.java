package com.example.user.banhangonline.screen.library;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.dragselectrecyclerview.DragSelectRecyclerView;
import com.afollestad.dragselectrecyclerview.DragSelectRecyclerViewAdapter;
import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.screen.library.adapter.PhotoAdapter;
import com.example.user.banhangonline.screen.sell.SellActivity;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.user.banhangonline.utils.KeyPreferUntils.keyIDCategory;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyIDPart;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyTitleCategory;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyTitlePart;
import static com.example.user.banhangonline.utils.KeyUntils.keyListImage;

public class LibraryActivity extends BaseActivity implements LibraryContact.View,
         DragSelectRecyclerViewAdapter.SelectionListener {
    private final static int RESULT_LOAD_IMAGE = 1;

    @BindView(R.id.recycerview_library)
    DragSelectRecyclerView recyclerViewLibrary;

    @BindView(R.id.tv_total_selected)
    TextView tvTotalSelected;

    @BindView(R.id.btn_done)
    Button btnDone;

    @Override
    public boolean isTransparentStatusBar() {
        return false;
    }

    private PhotoAdapter photoAdapter;
    private Unbinder unbinder;
    private LibraryPresenter mPresenter;
    private String idCate, titleCate, idPart, titlePart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        unbinder = ButterKnife.bind(this);
        mPresenter = new LibraryPresenter();
        mPresenter.onCreate();
        mPresenter.attachView(this);
        try {
            String s[] = {android.Manifest.permission.READ_EXTERNAL_STORAGE};
            if (checkPermissions(s)) {
                initAdapter();
            } else {
                ActivityCompat.requestPermissions(LibraryActivity.this, s, RESULT_LOAD_IMAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initDataSpinnerSell();
    }

    private void initDataSpinnerSell() {
        idCate = getIntent().getStringExtra(keyIDCategory);
        titleCate = getIntent().getStringExtra(keyTitleCategory);
        idPart = getIntent().getStringExtra(keyIDPart);
        titlePart = getIntent().getStringExtra(keyTitlePart);
    }

    private void initAdapter() {
        showDialog();
        photoAdapter = new PhotoAdapter(LibraryActivity.this, mPresenter.getListImageFromStorage(Environment.getExternalStorageDirectory()), new PhotoAdapter.OnLickListenerPhoto() {
            @Override
            public void onClickSelected(int position) {
                onDragSelectionChanged(position);
            }

            @Override
            public void onLongClickSelected(int position) {
                recyclerViewLibrary.setDragSelectActive(true, position);
            }

        });
        photoAdapter.setSelectionListener(this);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.grid_width));
        recyclerViewLibrary.setLayoutManager(layoutManager);
        recyclerViewLibrary.setAdapter(photoAdapter);
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setSupportsChangeAnimations(false);
        recyclerViewLibrary.setItemAnimator(animator);
        dismissDialog();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RESULT_LOAD_IMAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initAdapter();
        } else {
            showSnackbar(getString(R.string.khong_cho_phep_truy_cap_may_anh));
            finish();
        }
    }

    @OnClick(R.id.img_back)
    public void backActivity() {
        startActivity(new Intent(LibraryActivity.this, SellActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        mPresenter.detach();
        mPresenter.onDestroy();
        unbinder.unbind();
        unbinder = null;
        super.onDestroy();
    }

    @Override
    public void onDragSelectionChanged(int count) {
        if (count > 0) {
            btnDone.setEnabled(true);
        } else {
            btnDone.setEnabled(false);
        }
        tvTotalSelected.setText(getString(R.string.chon_anh) + " (" + count + ")");
    }

    @OnClick(R.id.btn_done)
    public void doneImage() {
        if (photoAdapter.getSelectedPhotos() != null) {
            showDialog();
            if (photoAdapter.getSelectedPhotos().size() <= 4) {
                Intent intent = new Intent(LibraryActivity.this, SellActivity.class);
                intent.putExtra(keyListImage, (Serializable) photoAdapter.getSelectedPhotos());
                if (idCate != null && titleCate != null) {
                    intent.putExtra(keyIDCategory, idCate);
                    intent.putExtra(keyTitleCategory, titleCate);
                }

                if (idPart != null && titlePart != null) {
                    intent.putExtra(keyIDPart, idPart);
                    intent.putExtra(keyTitlePart, titlePart);
                }

                startActivity(intent);
                finish();
                dismissDialog();
            } else {
                showSnackbar("Chỉ được chọn tối đa 4 ảnh");
                dismissDialog();
            }
        }
    }
}
