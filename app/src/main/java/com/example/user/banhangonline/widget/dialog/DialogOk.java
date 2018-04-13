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

public class DialogOk extends Dialog {

    private String mTitle, mMessage;
    private OkDialogListener mListener;

    public DialogOk(@NonNull Context context, String mTitle, String mMessage, OkDialogListener mListener) {
        super(context, R.style.FullscreenDialog);
        this.mTitle = mTitle;
        this.mMessage = mMessage;
        this.mListener = mListener;
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
        setContentView(R.layout.dialog_ok);
        TextView mTvTitle = (TextView) findViewById(R.id.dialog_title);
        TextView mTvMessage = (TextView) findViewById(R.id.dialog_message);
        Button mBtnOk = (Button) findViewById(R.id.dialog_btn_ok);

        mTvTitle.setText(mTitle);
        mTvMessage.setText(mMessage);
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onOkDialogAnswerOk(DialogOk.this);
                } else {
                    dismiss();
                }
            }
        });
    }

    public interface OkDialogListener {
        void onOkDialogAnswerOk(DialogOk dialog);
    }
}
