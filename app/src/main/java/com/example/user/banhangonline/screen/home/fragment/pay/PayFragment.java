package com.example.user.banhangonline.screen.home.fragment.pay;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.model.Categories;
import com.example.user.banhangonline.model.SanPham;
import com.example.user.banhangonline.screen.detail.SanPhamDetailActivity;
import com.example.user.banhangonline.screen.home.fragment.adapter.SanPhamAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.user.banhangonline.untils.KeyPreferUntils.keyStartDetail;

public class PayFragment extends Fragment implements PayContact.View {
    RecyclerView recyclerView;
    ProgressBar rotateLoading;
    private static final String ARG_BOOK = "book";
    private Categories categories;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private SanPhamAdapter mAdapter;
    private PayPresenter mPresenter;
    GridLayoutManager manager;

    public static PayFragment newInstance(Categories categories) {
        PayFragment fragment = new PayFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_BOOK, categories);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categories = (Categories) getArguments().getSerializable(ARG_BOOK);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay, container, false);
        mPresenter = new PayPresenter();
        mPresenter.attachView(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycerview);
        rotateLoading = (ProgressBar) view.findViewById(R.id.rotate_loading);
        manager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        if (categories != null) {
            initAdapter();
        }
        return view;
    }

    private void initAdapter() {

        mAdapter = new SanPhamAdapter(getActivity(), mPresenter.getSanPhamList(), new SanPhamAdapter.ISelectPayAdapter() {
            @Override
            public void onSelectedSanPham(SanPham sanPham) {
                Intent intent = new Intent(getActivity(), SanPhamDetailActivity.class);
                intent.putExtra(keyStartDetail, sanPham);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(mAdapter);

        mPresenter.loadSanPhamFromFirebase(mDatabase, categories.getId());
    }


    @Override
    public void loadSanPhamSuccess() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadSanPhamError() {

    }

    @Override
    public void onDestroy() {
        mPresenter.detach();
        super.onDestroy();
    }
}