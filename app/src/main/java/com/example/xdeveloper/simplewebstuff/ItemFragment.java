package com.example.xdeveloper.simplewebstuff;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.xdeveloper.simplewebstuff.dummy.DummyContent;
import com.example.xdeveloper.simplewebstuff.dummy.DummyContent.DummyItem;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */


public class ItemFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    ArrayList<Station> station_list = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyItemRecyclerViewAdapter rvAdapter;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private LatLng current_location;
    public final String LM_GPS = LocationManager.GPS_PROVIDER;
    public final String LM_NETWORK = LocationManager.NETWORK_PROVIDER;
    private Context context;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemFragment newInstance(int columnCount) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (station_list.size() == 0) {
//            volleyJson();
//        }


        context = this.getContext();
        mLocationManager =
                (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new MyLocationListener();
        openGPS(context);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }


        if (mLocationManager == null) {
            mLocationManager =
                    (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            mLocationListener = new MyLocationListener();
        }


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mLocationManager.requestLocationUpdates(LM_GPS, 0, 0, mLocationListener);
        mLocationManager.requestLocationUpdates(LM_NETWORK, 0, 0, mLocationListener);
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        openGPS(getContext());
        mLocationManager
                .requestLocationUpdates(LM_GPS, 0, 0, mLocationListener);
        mLocationManager.requestLocationUpdates(LM_NETWORK, 0, 0,
                mLocationListener);

        Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        this.current_location = new LatLng(location.getLatitude(), location.getLongitude());


    }

    // 開啟 GPS
    public void openGPS(Context context) {
        boolean gps = mLocationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = mLocationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (gps || network) {
            return;
        } else {
            // 開啟手動GPS設定畫面
            Intent gpsOptionsIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionsIntent);
        }
    }


    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());


/*
            Toast.makeText(getContext(), "location is:"+location, Toast.LENGTH_LONG).show();

            CameraPosition cameraPos = new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude()))
                    .zoom(17.0f).bearing(300).tilt(0).build();
            CameraUpdate cameraUpt = CameraUpdateFactory
                    .newCameraPosition(cameraPos);
           map.animateCamera(cameraUpt, 1000, null);
*/
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        // GPS位置資訊的狀態被更新
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        Log.i("onCreateView", "onCreateView this station list is " + station_list);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            Log.i("item_fragment", "this station list is " + station_list);


            if (station_list.size() == 0) {
                volleyJson();
            }


            //           recyclerView.setAdapter(new MyItemRecyclerViewAdapter(DummyContent.ITEMS, mListener));

        }
        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Station item);
    }


    private void volleyJson() {

        String url = "http://192.168.0.102/ecmap/api/get_station_json";

        // String url = "http://12.0.2.2/ecmap/api/get_station_json";

        // Request a string response
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject event = (JSONObject) response.get(i);

                                float[] result = new float[1];

                                Location.distanceBetween(current_location.latitude, current_location.longitude, event.getDouble("lat"), event.getDouble("lng"), result);

                                double distance = result[0];

                                Station s = new Station(event.getInt("id"), event.getString("name_tc"), event.getString("name_en"), event.getString("packing_no"), event.getDouble("lat"), event.getDouble("lng"), event.getString("image"), distance, event.getString("type"), event.getString("provider_en"), event.getString("address_en"), event.getString("address_tc"));
                                Log.w("ItemFragment", "station object  :  " + s);
                                Log.w("ItemFragment", "the station list size  :  " + station_list.size());
                                station_list.add(s);

                            }


                            Collections.sort(station_list, new Comparator<Station>() {
                                        @Override
                                        public int compare(Station s1, Station s2) {
                                            String sd1 = String.valueOf(s1.get_km_distance());
                                            String sd2 = String.valueOf(s2.get_km_distance());
                                            return sd1.compareTo(sd2);
                                        }
                                    }

                            );


                            System.out.println(station_list.size());
                            System.out.println();


                            rvAdapter = new MyItemRecyclerViewAdapter(station_list, mListener, getActivity());
                            Log.i("item_fragment", "this rvadapter is " + station_list);
                            recyclerView.setAdapter(rvAdapter);
                            recyclerView.invalidate();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error handling
                        System.out.println("Something went wrong!");
                        Toast.makeText(getContext(), "!!!! THE JSON HAS ERROR", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        );
        // Add the request to the RequestQueue.
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(req);
    }


}
