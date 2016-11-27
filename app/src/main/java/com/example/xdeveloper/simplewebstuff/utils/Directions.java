package com.example.xdeveloper.simplewebstuff.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Directions {

    private GoogleMap map;
    private Context context;
    private String mode;
    public final static String MODE_DRIVING = "driving";
    public final static String MODE_WALKING = "walking";
    public final static String MODE_BICYCLING = "bicycling";
    public final static String MODE_TRANSIT = "transit";
    public Polyline line;

    private Directions() {
    }

    private static Directions _instance = new Directions();

    public static Directions getInstance() {
        return _instance;
    }

    public void draw(Context context, LatLng origin, LatLng dest,
                     GoogleMap map, String mode) {
        this.context = context;
        this.map = map;
        this.mode = mode;
        String url = getDirectionsUrl(origin, dest, mode);
        System.out.println(url);
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest,
                                    String mode) {

        String str_origin = "origin=" + origin.latitude + ","
                + origin.longitude;

        String str_dest = "destination=" + dest.latitude + ","
                + dest.longitude;

        String sensor = "sensor=true";

        String parameters = str_origin + "&" + str_dest + "&"
                + sensor + "&mode=" + mode;

        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + parameters;

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Exception downloading", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            if (line != null)
                line.remove();

            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            // 走訪所有 result
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = result.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);
            }
            if (lineOptions != null) {
                line = map.addPolyline(lineOptions);

                Log.w("MyMapFragment", line.toString());

            } else {
                Toast.makeText(context, mode + " mode have no result !",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
