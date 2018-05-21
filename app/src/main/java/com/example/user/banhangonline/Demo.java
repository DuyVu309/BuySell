package com.example.user.banhangonline;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.model.SanPham;
import com.example.user.banhangonline.model.maps.Directions;
import com.example.user.banhangonline.model.maps.Route;
import com.example.user.banhangonline.utils.GoogleMapUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class Demo extends BaseActivity implements OnMapReadyCallback {
    private static final int REQUEST_CODE_MY_LOCATION = 1;

    private GoogleMap mMap;
    double longitude, latitude;
    private LocationRequest locationRequest;
    private GoogleApiClient apiClient;

//    private List<Distance> mDistance = new ArrayList<>();
    List<SanPham> sanPhamList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        ButterKnife.bind(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                 .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        List<String> listAddress = new ArrayList<>();
        listAddress.add("Cẩm Vũ, Cẩm Giàng District, Hai Duong, Vietnam");
        listAddress.add("Chung cư C6, Tăng Nhơn Phú A, District 9, Ho Chi Minh City, Vietnam");
        listAddress.add("Horical Co.,Ltd");
        listAddress.add("Quận 1, Ho Chi Minh City, Vietnam");
        GoogleMapUtils.getLatLongFromGivenListAddress(this, listAddress);

        LatLng latLng = new LatLng(getMyLocation().getLatitude(), getMyLocation().getLongitude());
//        Collections.sort(sanPhamList, new SortPlacesUtils(latLng));
//        tag();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMyLocation();

        for (LatLng lng : GoogleMapUtils.getListPoint()) {
            try {
                new Directions(latitude, longitude, lng.latitude, lng.longitude, new Directions.DirectionsListener() {
                    @Override
                    public void onDirectionSuccess(List<Route> routes) {
                        for (Route route : routes) {
                            mMap.addMarker(new MarkerOptions().position(route.endLocation).title(route.endAddress));
//                            mDistance.add(new Distance(route.endAddress, route.endLocation));
                        }
                    }
                }).execute();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }


    private void setUpMyLocation() {
        Location myLocation = getMyLocation();
        if (myLocation == null) return;

        longitude = myLocation.getLongitude();
        latitude = myLocation.getLatitude();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16));
        if (apiClient == null) {
            buildApi();
        }
    }

    private void buildApi() {
        apiClient = new GoogleApiClient.Builder(this)
                 .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                     @SuppressLint("RestrictedApi")
                     @Override
                     public void onConnected(@Nullable Bundle bundle) {
                         locationRequest = new LocationRequest();
                         locationRequest.setInterval(1000);
                         locationRequest.setFastestInterval(1000);
                         locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                     }

                     @Override
                     public void onConnectionSuspended(int i) {

                     }
                 })
                 .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                     @Override
                     public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                     }
                 })
                 .addApi(LocationServices.API)
                 .build();
        apiClient.connect();
    }

}
