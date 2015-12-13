package com.example.root.margarita;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.root.margarita.util.GlobalVar;
import com.example.root.margarita.util.LocationProvider;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class SplashActivity extends Activity implements LocationProvider.LocationCallback,GoogleMap.OnMarkerDragListener{

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 4000;
    private static final String SHAREDPREF_USER = "USER_CREDENTIALS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences settings_sp = getSharedPreferences(SHAREDPREF_USER, Context.MODE_PRIVATE);
        final String phone = settings_sp.getString("PHONE", null);
        final String session = settings_sp.getString("AUTH_TOKEN", null);


        if(phone != null && !phone.isEmpty())
            GlobalVar.setphone(phone);

        if(session != null && !session.isEmpty())
            GlobalVar.setToken(session);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent intent;
                if(phone != null && !phone.isEmpty())
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                else
                    intent = new Intent(SplashActivity.this, ActivityLogin.class);
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
        Log.d("Location call", String.valueOf(location.getLongitude()));
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