package com.example.user.banhangonline.model.maps;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Directions {
    public static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private double startLat, startLng, endLat, endLng;
    private String placeId;
    private DirectionsListener mLisenter;

    public Directions(double startLat, double startLng, double endLat, double endLng, DirectionsListener mLisenter) {
        this.startLat = startLat;
        this.startLng = startLng;
        this.endLat = endLat;
        this.endLng = endLng;
        this.mLisenter = mLisenter;
    }

    public Directions(double startLat, double startLng, String placeId, DirectionsListener mLisenter) {
        this.startLat = startLat;
        this.startLng = startLng;
        this.placeId = placeId;
        this.mLisenter = mLisenter;
    }

    public void execute() throws UnsupportedEncodingException {
        new DownloadRawData().execute(createUrl());
    }

    public void executePlaceID() throws UnsupportedEncodingException {
        new DownloadRawData().execute(createUrlPlaceId());
    }

    public String createUrl() throws UnsupportedEncodingException {
        return DIRECTION_URL_API + "origin=" + startLat + "," + startLng + "&destination=" + endLat + "," + endLng + "&key=" + "AIzaSyDCUDjlQqJRaapcRmtdb2l7uTQh4J2oK8Q";
    }

    public String createUrlPlaceId() throws UnsupportedEncodingException {
        return DIRECTION_URL_API + "origin=" + startLat + "," + startLng + "&destination=place_id:" + placeId + "&key=" + "AIzaSyDCUDjlQqJRaapcRmtdb2l7uTQh4J2oK8Q";
    }

    private class DownloadRawData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                parseJSon(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseJSon(String data) throws JSONException {
        if (data == null)
            return;

        List<Route> routes = new ArrayList<Route>();
        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonRoutes = jsonData.getJSONArray("routes");
        for (int i = 0; i < jsonRoutes.length(); i++) {
            JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
            Route route = new Route();

            JSONObject overview_polylineJson = jsonRoute.getJSONObject("overview_polyline");
            JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
            JSONObject jsonLeg = jsonLegs.getJSONObject(0);
            JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
            JSONObject jsonDuration = jsonLeg.getJSONObject("duration");
            JSONObject jsonEndLocation = jsonLeg.getJSONObject("end_location");
            JSONObject jsonStartLocation = jsonLeg.getJSONObject("start_location");

            route.distance = jsonDistance.getString("text");
            route.duration = jsonDuration.getString("text");
            route.endAddress = jsonLeg.getString("end_address");
            route.startAddress = jsonLeg.getString("start_address");
            route.startLocation = new LatLng(jsonStartLocation.getDouble("lat"), jsonStartLocation.getDouble("lng"));
            route.endLocation = new LatLng(jsonEndLocation.getDouble("lat"), jsonEndLocation.getDouble("lng"));
            route.points = decodePolyLine(overview_polylineJson.getString("points"));

            routes.add(route);
        }
        mLisenter.onDirectionSuccess(routes);
    }

    private List<LatLng> decodePolyLine(final String poly) {
        return PolyUtil.decode(poly);
    }

    public interface DirectionsListener {
        void onDirectionSuccess(List<Route> route);
    }

}