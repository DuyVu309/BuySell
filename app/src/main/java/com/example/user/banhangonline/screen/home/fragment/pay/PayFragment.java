package com.example.user.banhangonline.screen.home.fragment.pay;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.model.Categories;
import com.example.user.banhangonline.model.SanPham;
import com.example.user.banhangonline.screen.detail.SanPhamDetailActivity;
import com.example.user.banhangonline.screen.home.fragment.adapter.SanPhamAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.Comparator;

import static com.example.user.banhangonline.AppConstants.REQUEST_CODE_LOCATION;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyStartDetail;

public class PayFragment extends Fragment implements PayContact.View {
    RecyclerView recyclerView;
    ProgressBar rotateLoading;
    private static final String ARG_BOOK = "book";
    private Categories categories;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private SanPhamAdapter mAdapter;
    private PayPresenter mPresenter;
    private Location location;

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
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        if (categories != null) {
            initLocation();
            initAdapter();
        }
        return view;
    }

    private void initAdapter() {
        mPresenter.loadSanPhamFromFirebase(mDatabase, categories.getId());
        mAdapter = new SanPhamAdapter(recyclerView, getActivity(), location, mPresenter.getSanPhamList(), new SanPhamAdapter.ISelectPayAdapter() {
            @Override
            public void onSelectedSanPham(SanPham sanPham) {
                Intent intent = new Intent(getActivity(), SanPhamDetailActivity.class);
                intent.putExtra(keyStartDetail, sanPham);
                startActivity(intent);
            }

        });
        recyclerView.setAdapter(mAdapter);
    }

    private void initLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } else {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
            }
        }
    }


    @Override
    public void loadSanPhamSuccess() {
        if (mAdapter != null) {
            Collections.sort(mPresenter.getSanPhamList(), new Comparator<SanPham>() {
                @Override
                public int compare(SanPham sanPham, SanPham t1) {
                    return sanPham.getTime().compareTo(t1.getTime());
                }
            });
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            if (permissions.length > 0 && permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }
    }
}
