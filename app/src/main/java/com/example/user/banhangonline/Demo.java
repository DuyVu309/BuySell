package com.example.user.banhangonline;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.model.SanPham;
import com.example.user.banhangonline.untils.NetworkUtils;
import com.example.user.banhangonline.untils.TimeNowUtils;
import com.example.user.banhangonline.views.ZoomableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoViewAttacher;

import static com.example.user.banhangonline.untils.KeyUntils.keyIdCateDoAn;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCategory;
import static com.example.user.banhangonline.untils.KeyUntils.keySanPham;

public class Demo extends BaseActivity {

    @BindView(R.id.recycerview)
    ListView recyclerView;

    @BindView(R.id.img_)
    ImageView zoomableImageView;
    List<String> listKey = new ArrayList<>();
    int position = 0;
    boolean isLoadedWithLittle = false;
    List<String> list = new ArrayList<>();
    private List<String> mlistPlaceId = new ArrayList<>();
    private List<String> mlistAdress = new ArrayList<>();
    ArrayAdapter adapter;

    @BindView(R.id.edt_search)
    EditText edtSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        ButterKnife.bind(this);
        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(zoomableImageView);
        photoViewAttacher.update();
//        getKeyObject();
//        for (int i = 0; i < 100; i++) {
//            list.add("" + (int) (Math.random() * 100));
//        }
//        Collections.sort(list, new Comparator<String>() {
//            public int compare(String s, String t1) {
//                return s.compareTo(t1);
//            }
//        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (NetworkUtils.isConnected(Demo.this)) {
                    if (charSequence.toString().equals("")) {
                        recyclerView.setVisibility(View.GONE);
                    }
                    GetPlaces task = new GetPlaces();
                    String check = edtSearch.getText().toString();
                    if (check.trim().isEmpty()) {
                        //do something
                    } else {
                        task.execute(check);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mlistPlaceId != null) {
                    Log.d("TAG AD", mlistPlaceId.get(i));
                    Log.d("TAG AD2", mlistAdress.get(i));
                }
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
                    mlistPlaceId = placesID;
                    mlistAdress = predictionsArr;
                }
            } catch (IOException e) {

            } catch (JSONException e) {

            }
            return predictionsArr;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            adapter = new ArrayAdapter<String>(getBaseContext(),
                     android.R.layout.simple_list_item_1);
            recyclerView.setAdapter(adapter);

            for (String string : strings) {
                adapter.add(string);
                adapter.notifyDataSetChanged();

            }
        }
    }


    private void getKeyObject() {
        mDataBase.child(keySanPham).orderByChild(keyIdCategory).equalTo(keyIdCateDoAn).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    listKey.add(snapshot.getKey());
                }

                if (listKey.size() > 0) {
                    Log.d("TAG SIZE", "" + listKey.size());
                    loadMore();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void loadMore() {
        Log.d("TAG po", position + "");
        if (listKey.size() >= position + 10) {
            mDataBase.child(keySanPham).orderByKey().startAt(listKey.get(position)).endAt(listKey.get(position + 9)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SanPham sanPham = snapshot.getValue(SanPham.class);
                        if (sanPham.getIdCategory().equals(keyIdCateDoAn)) {
                            Log.d("TAG LOG >", sanPham.getIdCategory());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        if (listKey.size() < position + 10) {
            if (!isLoadedWithLittle) {
                mDataBase.child(keySanPham).orderByKey().startAt(listKey.get(position)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            SanPham sanPham = snapshot.getValue(SanPham.class);
                            if (sanPham.getIdCategory().equals(keyIdCateDoAn)) {
                                Log.d("TAG LOG <", sanPham.getIdCategory());
                            }
                        }
                        isLoadedWithLittle = true;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        }
    }

    @OnClick(R.id.btn_loadmore)
    public void load() {
//        if (listKey.size() > position) {
//            position += 10;
//        }
//        loadMore();



        List<Date> listDates = new ArrayList<Date>();
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            listDates.add(dateFormatter.parse("2013-09-30"));
            listDates.add(dateFormatter.parse("2013-07-06"));
            listDates.add(dateFormatter.parse("2013-11-28"));
        } catch (ParseException ex) {
            Log.d("TAG X", ex.toString());
        }

        for (Date s : listDates) {
            Log.d("TAG X", s.toString());
        }

        Collections.sort(listDates, new Comparator<Date>() {
            @Override
            public int compare(Date date, Date t1) {
                return date.compareTo(t1);
            }
        });

        for (Date s : listDates) {
            Log.d("TAG YYYYYY", s.toString());
        }

    }

}
