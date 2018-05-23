package com.example.user.banhangonline.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GoogleMapUtils {

    private static List<LatLng> listPoint = new ArrayList<>();
    private static LatLng latLong;

    public static List<LatLng> getListPoint() {
        return listPoint;
    }

    public static LatLng getLatLong() {
        return latLong;
    }

    public static void getLatLongFromGivenListAddress(Context context, List<String> youraddress) {
        for (String addressMyCart : youraddress) {
            try {
                Geocoder selected_place_geocoder = new Geocoder(context);
                List<Address> address;

                address = selected_place_geocoder.getFromLocationName(addressMyCart, 5);

                if (address == null) {
                    return;
                } else {
                    Address location = address.get(0);
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();
                    listPoint.add(new LatLng(lat, lng));
                }

            } catch (Exception e) {
                e.printStackTrace();
                FetchListLatLongFromService fetchLatlngFromService = new FetchListLatLongFromService(addressMyCart.replaceAll("\\s+", ""));
                fetchLatlngFromService.execute();

            }
        }

    }

    public static class FetchListLatLongFromService extends
             AsyncTask<Void, Void, StringBuilder> {
        String place;


        public FetchListLatLongFromService(String place) {
            super();
            this.place = place;

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            this.cancel(true);
        }

        @Override
        protected StringBuilder doInBackground(Void... params) {
            try {
                HttpURLConnection conn = null;
                StringBuilder jsonResults = new StringBuilder();

                String googleMapUrl = "http://maps.google.com/maps/api/geocode/json?address=" +
                         this.place + "&key=AIzaSyDCUDjlQqJRaapcRmtdb2l7uTQh4J2oK8Q";

                URL url = new URL(googleMapUrl);
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(
                         conn.getInputStream());
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }
                return jsonResults;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(StringBuilder result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result == null) {
                return;
            }
            try {
                JSONObject jsonObj = new JSONObject(result.toString());
                JSONArray resultJsonArray = jsonObj.getJSONArray("results");

                JSONObject before_geometry_jsonObj = resultJsonArray
                         .getJSONObject(0);

                JSONObject geometry_jsonObj = before_geometry_jsonObj
                         .getJSONObject("geometry");

                JSONObject location_jsonObj = geometry_jsonObj
                         .getJSONObject("location");

                String lat_helper = location_jsonObj.getString("lat");
                double lat = Double.valueOf(lat_helper);

                String lng_helper = location_jsonObj.getString("lng");
                double lng = Double.valueOf(lng_helper);

                LatLng point = new LatLng(lat, lng);
                listPoint.add(point);
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }

    public static void getLatLongFromGivenAddress(Context context, String youraddress) {
        try {
            Geocoder selected_place_geocoder = new Geocoder(context);
            List<Address> address;

            address = selected_place_geocoder.getFromLocationName(youraddress, 5);

            if (address == null) {
                return;
            } else {
                Address location = address.get(0);
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                latLong = new LatLng(lat, lng);
            }

        } catch (Exception e) {
            e.printStackTrace();
            FetchLatLongFromService fetchLatlngFromService = new FetchLatLongFromService(youraddress.replaceAll("\\s+", ""));
            fetchLatlngFromService.execute();
        }
    }

    public static class FetchLatLongFromService extends
             AsyncTask<Void, Void, StringBuilder> {
        String place;


        public FetchLatLongFromService(String place) {
            super();
            this.place = place;

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            this.cancel(true);
        }

        @Override
        protected StringBuilder doInBackground(Void... params) {
            try {
                HttpURLConnection conn = null;
                StringBuilder jsonResults = new StringBuilder();

                String googleMapUrl = "http://maps.google.com/maps/api/geocode/json?address=" +
                         this.place + "&key=AIzaSyDCUDjlQqJRaapcRmtdb2l7uTQh4J2oK8Q";

                URL url = new URL(googleMapUrl);
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(
                         conn.getInputStream());
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }
                return jsonResults;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(StringBuilder result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result == null) {
                return;
            }
            try {
                JSONObject jsonObj = new JSONObject(result.toString());
                JSONArray resultJsonArray = jsonObj.getJSONArray("results");

                JSONObject before_geometry_jsonObj = resultJsonArray
                         .getJSONObject(0);

                JSONObject geometry_jsonObj = before_geometry_jsonObj
                         .getJSONObject("geometry");

                JSONObject location_jsonObj = geometry_jsonObj
                         .getJSONObject("location");

                String lat_helper = location_jsonObj.getString("lat");
                double lat = Double.valueOf(lat_helper);

                String lng_helper = location_jsonObj.getString("lng");
                double lng = Double.valueOf(lng_helper);
                latLong = new LatLng(lat, lng);
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }
}
