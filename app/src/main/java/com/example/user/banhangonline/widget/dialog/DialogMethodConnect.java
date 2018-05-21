package com.example.user.banhangonline.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.banhangonline.R;

public class DialogMethodConnect extends Dialog{

    private IOnClickChooseMethodPay mListener;

    public DialogMethodConnect(@NonNull Context context, IOnClickChooseMethodPay mListener) {
        super(context, R.style.FullscreenDialog);
        this.mListener = mListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (getWindow() != null && getWindow().getDecorView() != null) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.dialog_method_pay);

        TextView tvPayBuy = (TextView) findViewById(R.id.dialog_method_connect_call);
        TextView tvAddCart = (TextView) findViewById(R.id.dialog_method_connect_sms);
        Button btnCancel = (Button) findViewById(R.id.dialog_method_pay_cancel);

        tvPayBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onMethodCall();
                dismiss();
            }
        });

        tvAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onMethodSMS();
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public interface IOnClickChooseMethodPay{
        void onMethodCall();

        void onMethodSMS();
    }
}
