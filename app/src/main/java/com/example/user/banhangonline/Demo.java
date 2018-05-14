package com.example.user.banhangonline;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.user.banhangonline.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Demo extends BaseActivity {
    String dataTitle, dataMessage;
    EditText title, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        ButterKnife.bind(this);
        title = (EditText) findViewById(R.id.title);
        message = (EditText) findViewById(R.id.message);
    }

    @OnClick(R.id.btn_sent  )
    public void sendMessage(View view) {

    }
}
