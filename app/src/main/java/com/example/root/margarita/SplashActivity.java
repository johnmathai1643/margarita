package com.example.root.margarita;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;

import com.example.root.margarita.util.LocationProvider;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class SplashActivity extends Activity implements LocationProvider.LocationCallback,GoogleMap.OnMarkerDragListener{

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    private static final String SHAREDPREF_USER = "USER_CREDENTIALS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                SharedPreferences settings_sp = getSharedPreferences(SHAREDPREF_USER, Context.MODE_PRIVATE);
                Boolean REGISTER = settings_sp.getBoolean("REGISTER", true);
                Intent intent;
                if(REGISTER)
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                else
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void handleNewLocation(Location location) {
        SharedPreferences user_sp = getSharedPreferences(SHAREDPREF_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = user_sp.edit();
        editor.putString("LAT", String.valueOf(location.getLatitude()));
        editor.putString("LNG", String.valueOf(location.getLongitude()));
        editor.putBoolean("LOCATION_ON", true);
        editor.commit();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }
}