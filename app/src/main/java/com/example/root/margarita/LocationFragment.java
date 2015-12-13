package com.example.root.margarita;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.root.margarita.util.AsyncTasker;
import com.example.root.margarita.util.GlobalVar;
import com.example.root.margarita.util.LocationProvider;
import com.example.root.margarita.util.NetworkConnection;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class LocationFragment extends Fragment implements LocationProvider.LocationCallback,GoogleMap.OnMarkerDragListener {

    public static JSONArray dataFromAsyncTask;
    public static Location LOCATION_CURRENT;
    public static JSONArray dataFromFreqUpater;
    MapView mapView;
    GoogleMap map;
    public static final String TAG = MainActivity.class.getSimpleName();
    static LatLng CurLocation;
    private ProgressDialog dialog;
    private Handler h;
    boolean markerClicked;
    private ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();

    int CASE_NUM = 0;

    private LocationProvider mLocationProvider;
    private NetworkConnection mNetworkConnection;
    private static final String SHAREDPREF_USER = "USER_CREDENTIALS";

    public LocationFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.location_fragment, container, false);
        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());
        map.setOnMarkerDragListener(this);

        mNetworkConnection = new NetworkConnection(getActivity());
        mLocationProvider = new LocationProvider(getActivity(), this);

        return view;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
        mLocationProvider.connect();
    }

    @Override
    public void onPause(){
        super.onPause();
        mLocationProvider.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        CurLocation = new LatLng(currentLatitude, currentLongitude);
        Marker MyLocation = map.addMarker(new MarkerOptions().position(CurLocation).title("Current Location").snippet("This is your location"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(CurLocation, 12));
        map.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);
        SharedPreferences user_sp = getActivity().getSharedPreferences(SHAREDPREF_USER, Context.MODE_PRIVATE);
        String mobile_phone= user_sp.getString("PHONE",null);
        AsyncTasker mAsyncTasker = new AsyncTasker(GlobalVar.URL+"api/s/user/updatestatus" ,""+currentLatitude+":"+currentLongitude+":"+mobile_phone,1);
        mAsyncTasker.updatelocation(GlobalVar.URL+"api/s/user/updatestatus",""+currentLatitude+":"+currentLongitude+":"+mobile_phone);

    }


    @Override
    public void onMarkerDragStart(Marker marker) {
        Toast.makeText(getActivity(),marker.getSnippet().toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

}
