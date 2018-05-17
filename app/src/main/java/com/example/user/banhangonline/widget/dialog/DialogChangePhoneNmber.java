package com.example.user.banhangonline.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.banhangonline.R;

public class DialogChangePhoneNmber extends Dialog {

    private Context context;
    private String phoneNumber;
    private IOnDoneChangeBuyPhoneNumber lisenter;
    public DialogChangePhoneNmber(@NonNull Context context, String phoneNumber, IOnDoneChangeBuyPhoneNumber lisenter) {
        super(context, R.style.FullscreenDialog);
        this.context = context;
        this.phoneNumber = phoneNumber;
        this.lisenter = lisenter;
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

        setContentView(R.layout.dialog_change_buy_phone_number);
        final EditText edtPhone = (EditText) findViewById(R.id.edt_change_phone_number);
        Button btnCancel = (Button) findViewById(R.id.dialog_change_phone_cancel);
        Button btnDonePhoneNumber = (Button) findViewById(R.id.dialog_change_phone_done);
        edtPhone.setText(phoneNumber);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnDonePhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtPhone.getText() != null) {
                    lisenter.doneChangeBuyPhoneNumber(edtPhone.getText().toString().trim());
                    dismiss();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), context.getString(R.string.dont_address), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    public interface IOnDoneChangeBuyPhoneNumber{
        void doneChangeBuyPhoneNumber(String phoneNumber);
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }
}
