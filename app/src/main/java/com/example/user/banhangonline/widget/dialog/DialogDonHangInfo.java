package com.example.user.banhangonline.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.model.DonHang;

public class DialogDonHangInfo extends Dialog {

    private Context context;
    private DonHang donHang;

    public DialogDonHangInfo(@NonNull Context context, DonHang donHang) {
        super(context);
        this.context = context;
        this.donHang = donHang;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (getWindow() != null) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.dialog_donhang_info);
        TextView tvName = (TextView) findViewById(R.id.tv_name_nguoi_mua);
        TextView tvDiaChi = (TextView) findViewById(R.id.tv_diachi_nguoi_mua);
        TextView tvDonHang = (TextView) findViewById(R.id.tv_donhang_nguoi_mua);
        LinearLayout ln_sdt = (LinearLayout) findViewById(R.id.ln_sdt);
        TextView tvSdt = (TextView) findViewById(R.id.tv_sdt_nguoi_mua);
        TextView tvSoLuong = (TextView) findViewById(R.id.tv_soluong);
        TextView tvThoiGian = (TextView) findViewById(R.id.tv_thoi_gian);

        if (donHang != null) {
            tvName.setText(donHang.getNameNguoiMua());
            tvDiaChi.setText(donHang.getDiaChi());
            tvDonHang.setText(donHang.getHeader());
            if (tvSdt.getText() != null) {
                tvSdt.setText(donHang.getSoDienThoai());
            } else {
                ln_sdt.setVisibility(View.GONE);
            }
            tvSoLuong.setText(donHang.getSoLuong());
            tvThoiGian.setText(donHang.getThoiGian());
        }
    }
}
