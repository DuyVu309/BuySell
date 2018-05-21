package com.example.user.banhangonline.screen.purchased;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.caches.SaveMyPurchased;
import com.example.user.banhangonline.model.DonHang;
import com.example.user.banhangonline.screen.myGioHang.MyGioHangActivity;
import com.example.user.banhangonline.screen.myGioHang.adapter.MyGioHangAdapter;
import com.example.user.banhangonline.utils.DialogUntils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MyPurchasedActivity extends BaseActivity {

    @BindView(R.id.rv_my_purchased)
    RecyclerView rvMyChased;

    @Override
    public boolean isTransparentStatusBar() {
        return false;
    }

    Unbinder unbinder;
    MyGioHangAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purchased);
        unbinder = ButterKnife.bind(this);
        initAdapter();


    }

    private void initAdapter() {
        rvMyChased.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyGioHangAdapter(rvMyChased, this, null, SaveMyPurchased.readMyPurchasedFile(this), new MyGioHangAdapter.IOnClickMyCart() {
            @Override
            public void onClickMyCart(DonHang donHang) {
                DialogUntils.showDonHangInfo(MyPurchasedActivity.this, donHang);

            }

            @Override
            public void onOptionSelectedMyCart(int postion, DonHang donHang) {
                SaveMyPurchased.readMyPurchasedFile(MyPurchasedActivity.this).remove(postion);
                mAdapter.notifyItemRemoved(postion);
                mAdapter.notifyItemRangeChanged(postion, SaveMyPurchased.readMyPurchasedFile(MyPurchasedActivity.this).size());
            }
        });
        rvMyChased.setAdapter(mAdapter);
    }

    @OnClick(R.id.img_arrow_back)
    public void backActivity(){
        finish();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        unbinder = null;
        super.onDestroy();
    }
}
