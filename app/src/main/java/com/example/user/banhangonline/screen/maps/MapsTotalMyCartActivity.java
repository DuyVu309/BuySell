package com.example.user.banhangonline.screen.maps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.model.maps.Directions;
import com.example.user.banhangonline.model.maps.Route;
import com.example.user.banhangonline.utils.GoogleMapUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.user.banhangonline.utils.KeyPreferUntils.keyStartListAddress;

public class MapsTotalMyCartActivity extends BaseActivity implements OnMapReadyCallback {

    @BindView(R.id.tv_map_total_mycart)
    TextView tvMapTotal;

    private GoogleMap mMap;

    private List<String> listAddress = new ArrayList<>();
    private List<Polyline> listPolylinePaths = new ArrayList<>();


    @Override
    public boolean isTransparentStatusBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_total_my_cart);
        ButterKnife.bind(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                 .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listAddress = getIntent().getStringArrayListExtra(keyStartListAddress);
    }

    private void initMarkerInGoogleMap() {
        GoogleMapUtils.getLatLongFromGivenListAddress(this, listAddress);
        if (GoogleMapUtils.getListPoint() != null) {
            for (final LatLng lng : GoogleMapUtils.getListPoint()) {
                try {
                    new Directions(getMyLocation().getLatitude(), getMyLocation().getLongitude(), lng.latitude, lng.longitude, new Directions.DirectionsListener() {
                        @Override
                        public void onDirectionSuccess(List<Route> routes) {
                            for (Route route : routes) {
                                mMap.addMarker(new MarkerOptions().position(route.endLocation).title(route.endAddress));

                            }
                        }
                    }).execute();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        tvMapTotal.setText(getString(R.string.tong_so_dia_diem) + " (" + listAddress.size() + ")");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMyLocation();
        if (listAddress != null) {
            initMarkerInGoogleMap();
        }
    }

    private void setUpMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
