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

public class DialogPositiveNegative extends Dialog {

    private String mTitle, mMessage, mAnswerPositive, mAnswerNegative;
    private IPositiveNegativeDialogListener mListener;

    public void setOnIPositiveNegativeDialogListener(IPositiveNegativeDialogListener listener) {
        this.mListener = listener;
    }


    public DialogPositiveNegative(@NonNull Context context, String title, String message, String positive, String negative) {
        super(context, R.style.FullscreenDialog);
        mTitle = title;
        mMessage = message;
        mAnswerPositive = positive;
        mAnswerNegative = negative;
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

        setContentView(R.layout.dialog_posotive_negative);
        TextView mTvTitle = (TextView) findViewById(R.id.dialog_positive_negative_title);
        TextView mTvMessage = (TextView) findViewById(R.id.dialog_positive_negative_message);
        Button mBtnPositive = (Button) findViewById(R.id.dialog_positive_negative_answer_positive);
        Button mBtnNegative = (Button) findViewById(R.id.dialog_positive_negative_answer_negative);

        mTvTitle.setText(mTitle);
        mTvMessage.setText(mMessage);
        mBtnPositive.setText(mAnswerPositive);
        mBtnNegative.setText(mAnswerNegative);
        mBtnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.onClickAnswerPositive(DialogPositiveNegative.this);
            }
        });
        mBtnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickAnswerNegative(DialogPositiveNegative.this);
            }
        });
    }

    public interface IPositiveNegativeDialogListener {
        void onClickAnswerPositive(DialogPositiveNegative dialog);

        void onClickAnswerNegative(DialogPositiveNegative dialog);
    }
}
