package com.example.user.banhangonline;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.user.banhangonline.base.BaseActivity;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class Demo extends BaseActivity implements OnMapReadyCallback {
    private static final int REQUEST_CODE_MY_LOCATION = 1;

    private GoogleMap mMap;
    double longitude, latitude;
    private LocationRequest locationRequest;
    private GoogleApiClient apiClient;

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
        listAddress.add("Chung+Cư+C6,+Man+Thiện,+Tăng+Nhơn+Phú+A,+Quận+9,+Hồ+Chí+Minh");
        listAddress.add("Chưa có địa chỉ");


        GoogleMapUtils.getLatLongFromGivenListAddress(this, listAddress);
        for (LatLng lng : GoogleMapUtils.getListPoint()) {
            Log.d("TAG LATLONG", lng.latitude + "\n" + lng.longitude);


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_MY_LOCATION) {
            if (permissions.length == 1 && permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
            } else {

            }
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMyLocation();
        for (LatLng lng : GoogleMapUtils.getListPoint()) {
            mMap.addMarker(new MarkerOptions().position(lng));
        }
    }

    private void setUpMyLocation() {
        String s[] = {android.Manifest.permission.ACCESS_FINE_LOCATION};

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, s, REQUEST_CODE_MY_LOCATION);
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        Location myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        longitude = myLocation.getLongitude();
        latitude = myLocation.getLatitude();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(longitude, latitude), 16));
        if (apiClient == null) {
            buildApi();
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

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

    ;

}
