package com.example.user.banhangonline.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.utils.NetworkUtils;

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

public class DialogChangeAddress extends Dialog {
    private Context context;
    private String adress;
    private List<String> mlistAdress = new ArrayList<>();
    private IOnDoneChangeAddress mListener;

    EditText edtChangeAddress;
    ListView listView;

    public DialogChangeAddress(@NonNull Context context, String adress,IOnDoneChangeAddress mListener) {
        super(context, R.style.FullscreenDialog);
        this.context = context;
        this.adress = adress;
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

        setContentView(R.layout.dialog_change_adress);
        edtChangeAddress = (EditText) findViewById(R.id.edt_change_address);
        listView = (ListView) findViewById(R.id.lv_places);
        Button btnCancel = (Button) findViewById(R.id.dialog_change_address_cancel);
        Button btnXong = (Button) findViewById(R.id.dialog_change_address_done);
        edtChangeAddress.setText(adress);

        btnXong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtChangeAddress.getText() != null) {
                    mListener.doneChangeAddress(edtChangeAddress.getText().toString().trim());
                    dismiss();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), context.getString(R.string.dont_address), Snackbar.LENGTH_LONG).show();
                }
            }
        });


        edtChangeAddress.addTextChangedListener(new TextWatcher() {
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
                    String check = edtChangeAddress.getText().toString();
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
                    edtChangeAddress.setText(mlistAdress.get(i));
                    listView.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
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

    @Override
    public void onBackPressed() {
        dismiss();
    }

    public interface IOnDoneChangeAddress {
        void doneChangeAddress(String address);
    }
}
