package com.example.user.banhangonline.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import com.example.user.banhangonline.R;
import com.example.user.banhangonline.untils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class DialogChangeAccount extends Dialog {
    private Context context;
    private String name, phoneNumber, address;
    private List<String> mlistAdress = new ArrayList<>();
    IOnClickDoneChange mLisenter;
    ListView listView;

    public void setmLisenter(IOnClickDoneChange mLisenter) {
        this.mLisenter = mLisenter;
    }

    public DialogChangeAccount(Context context, String name, String phoneNumber, String address) {
        super(context, R.style.FullscreenDialog);
        this.context = context;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
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
        setContentView(R.layout.dialog_change_info_account);
        ImageView imageView = (ImageView) findViewById(R.id.img_close_dialog);
        final EditText edtName = (EditText) findViewById(R.id.edt_name);
        final EditText edtPhone = (EditText) findViewById(R.id.edt_number_phone);
        final EditText edtAddress = (EditText) findViewById(R.id.edt_address);
        final Button btnHuy = (Button) findViewById(R.id.btn_huy);
        final Button btnXong = (Button) findViewById(R.id.btn_xong);
        listView = (ListView) findViewById(R.id.listview_address);

        edtName.setText(name);
        edtPhone.setText(phoneNumber);
        edtAddress.setText(address);

        edtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (NetworkUtils.isConnected(context)) {
                    if (charSequence.toString().equals("")) {
                        listView.setVisibility(View.INVISIBLE);
                    }
                    GetPlaces task = new GetPlaces();
                    String check = edtAddress.getText().toString();
                    if (check.trim().isEmpty()) {

                    } else {
                        task.execute(check);
                        listView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mlistAdress != null) {
                    edtAddress.setText(mlistAdress.get(i));
                    listView.setVisibility(View.INVISIBLE);
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnXong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLisenter.doneChange(edtName.getText().toString().trim(),
                         edtPhone.getText().toString().trim(),
                         edtAddress.getText().toString().trim());
                dismiss();
            }
        });
    }

    public interface IOnClickDoneChange {
        void doneChange(String name, String phone, String address);
    }

    private class GetPlaces extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            ArrayList<String> predictionsArr = new ArrayList<String>();
            ArrayList<String> placesID = new ArrayList<String>();

            try {
                URL googlePlaces = new URL("https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + URLEncoder.encode(strings[0].toString(), "UTF-8") + "&types=geocode&language=en&sensor=true&key=AIzaSyDCUDjlQqJRaapcRmtdb2l7uTQh4J2oK8Q");
                URLConnection tc = googlePlaces.openConnection();

                BufferedReader in = new BufferedReader(
                         new InputStreamReader(tc.getInputStream()));

                String line;
                StringBuffer sb = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                JSONObject predictions = new JSONObject(sb.toString());
                JSONArray ja = new JSONArray(predictions
                         .getString("predictions"));

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = (JSONObject) ja.get(i);
                    predictionsArr.add(jo.getString("description"));
                    placesID.add(jo.getString("place_id"));
                    mlistAdress = predictionsArr;
                }
            } catch (IOException e) {

            } catch (JSONException e) {

            }
            return predictionsArr;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            ArrayAdapter adapter = new ArrayAdapter<String>(context,
                     android.R.layout.simple_list_item_1);
            listView.setAdapter(adapter);

            for (String string : strings) {
                adapter.add(string);
                adapter.notifyDataSetChanged();

            }
        }
    }


}
