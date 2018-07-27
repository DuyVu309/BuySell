package com.example.user.banhangonline.screen.setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.interactor.prefer.PreferManager;
import com.example.user.banhangonline.views.swipe.SwipeBackLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SettingActivity extends BaseActivity {
    @BindView(R.id.sw_mua_hang)
    Switch swDistanceSell;

    @BindView(R.id.sw_gio_hang)
    Switch swDistanceCart;

    Unbinder unbinder;

    @Override
    public boolean isTransparentStatusBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        unbinder = ButterKnife.bind(this);
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);
        swDistanceSell.setChecked(PreferManager.getIsShowDistanceSell(this));
        swDistanceCart.setChecked(PreferManager.getIsShowDistanceCart(this));
        swDistanceSell.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                PreferManager.setShowDistanceSell(SettingActivity.this, b);
            }
        });

        swDistanceCart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                PreferManager.setShowDistanceCart(SettingActivity.this, b);
            }
        });

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
