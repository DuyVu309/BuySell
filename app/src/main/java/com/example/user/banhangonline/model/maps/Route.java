package com.example.user.banhangonline.model.maps;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Route {
    public String distance;
    public String duration;
    public String endAddress;
    public String startAddress;
    public LatLng startLocation;
    public LatLng endLocation;

    public List<LatLng> points;
}
