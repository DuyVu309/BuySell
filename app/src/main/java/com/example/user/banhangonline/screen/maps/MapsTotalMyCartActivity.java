package com.example.user.banhangonline.screen.maps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.widget.TextView;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.model.maps.Directions;
import com.example.user.banhangonline.model.maps.Route;
import com.example.user.banhangonline.utils.GoogleMapUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.user.banhangonline.utils.KeyPreferUntils.keyStartListAddress;

public class MapsTotalMyCartActivity extends BaseActivity implements OnMapReadyCallback {

    private static final int REQUEST_CODE_MY_LOCATION = 1;

    @BindView(R.id.tv_map_total_mycart)
    TextView tvMapTotal;

    private GoogleMap mMap;
    private double longitudeMyLocation, latitudeMyLocation;

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
                    new Directions(latitudeMyLocation, longitudeMyLocation, lng.latitude, lng.longitude, new Directions.DirectionsListener() {
                        @Override
                        public void onDirectionSuccess(List<Route> routes) {
                            for (Route route : routes) {
                                PolylineOptions polylineOptions = new PolylineOptions().geodesic(true).color(Color.RED).width(12);
                                for (int i = 0; i < route.points.size(); i++) {
                                    polylineOptions.add(route.points.get(i));
                                }
                                listPolylinePaths.add(mMap.addPolyline(polylineOptions));
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
        longitudeMyLocation = myLocation.getLongitude();
        latitudeMyLocation = myLocation.getLatitude();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(longitudeMyLocation, latitudeMyLocation), 16));

    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeMyLocation = location.getLongitude();
            latitudeMyLocation = location.getLatitude();
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


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_MY_LOCATION) {
            if (permissions.length == 1 &&
                     permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                     grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
            } else {

            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
