package com.example.user.banhangonline.screen.feedback;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.user.banhangonline.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedBackActivity extends AppCompatActivity {
    @BindView(R.id.edt_subject_feedback)
    EditText edtSubject;

    @BindView(R.id.edt_body_feedback)
    EditText edtBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_fb)
    public void clickLinkFb(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(getString(R.string.info_dev)));
        startActivity(intent);
    }

    @OnClick(R.id.btn_send_feedback)
    public void sendFeedBack() {
        String to = "BuySelVN309@gmail.com";
        String subject = edtSubject.getText().toString();
        String message = edtBody.getText().toString();

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        emailIntent.setType("message/rfe822");
        startActivity(Intent.createChooser(emailIntent, "Select Email app"));
    }
}
