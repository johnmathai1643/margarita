package com.example.root.margarita.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.root.margarita.ActivityLogin;
import com.example.root.margarita.MainActivity;
import com.google.android.gms.ads.internal.request.StringParcel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class AuthenticatorTasker extends AsyncTask<Void,Void,Void> implements LocationProvider.LocationCallback,GoogleMap.OnMarkerDragListener  {

    private static final String TAG = AuthenticatorTasker.class.getName();
    private String jsonstring = " ";
    public String phone,type,lat,lng;
    private ProgressDialog dialog;
    private Context context;
    private String URL;
//    private DatabaseHandler db;
    private static final String SHAREDPREF_USER = "USER_CREDENTIALS";
    private double currentLatitude;
    private double currentLongitude;

    public AuthenticatorTasker(String phone,String lat,String lng, Context context){
        this.phone = phone;
        this.lat = lat;
        this.lng = lng;
        this.URL = "http://localhost:4000/api/user/";
        this.type = "register";
        this.context = context;
    }

    @Override
    protected void onPreExecute(){
//        this.dialog.show(activity, "Registering", "Please wait...");
    }

    @Override
    protected Void doInBackground(Void... params) {

        DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
        HttpPost httppost = new HttpPost(URL);
        Log.i(TAG, URL);

        StringEntity en = null;
        try {
            en = new StringEntity(createJsonString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httppost.setEntity(en);

        if (this.type == "register")
            httppost.setHeader("Content-type", "application/json");
        else if (this.type == "login"){
            httppost.setHeader("Content-type", "application/json");
            httppost.addHeader("Accept", "application/json");
        }

        InputStream inputstream = null;
        try {
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            inputstream = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream,"UTF-8"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = reader.readLine())!=null){
                sb.append(line + "\n");
            }
            jsonstring = sb.toString();

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        try {
            get_auth_token();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    protected String createJsonString() {
        JSONObject user_params = new JSONObject();

        lat = String.valueOf(currentLatitude);
        lng = String.valueOf(currentLongitude);

        if (this.type == "register") {
            try {
                user_params.put("phone", phone);
                user_params.put("lat", lat);
                user_params.put("lng", lng);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject userObject = new JSONObject();
            try {
                userObject.put("user", user_params);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String send_json = userObject.toString();
            Log.i(TAG, send_json);
            return send_json;

        }
        else if (this.type == "login"){
            try {
                user_params.put("phone", phone);
//                user_params.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject userObject = new JSONObject();
            try {
                userObject.put("user", user_params);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String send_json = userObject.toString();
            Log.i(TAG, send_json);
            return send_json;
        }
        else
            return null;
    }

    protected void get_auth_token() throws JSONException {

        if (jsonstring != null) {
            JSONObject userDetails = new JSONObject(jsonstring);
            Log.i(TAG, userDetails.toString());
//            SessionManager mSessionManager = new SessionManager(activity);
//            mSessionManager.set_session();
            Log.i(TAG, userDetails.getString("data"));
            if ("OK".equals(userDetails.getString("data"))) {
                if (this.type == "register") {
                    SharedPreferences user_sp = context.getSharedPreferences(SHAREDPREF_USER, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = user_sp.edit();
                    editor.putString("AUTH_TOKEN", userDetails.getString("data"));
                    editor.putBoolean("LOGGED_IN", true);
                    editor.putBoolean("REGISTER", true);
                    editor.commit();
                    Toast.makeText(context, "Register Successful", Toast.LENGTH_SHORT).show();}
                else if (this.type == "login") {
                    SharedPreferences user_sp = context.getSharedPreferences(SHAREDPREF_USER, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = user_sp.edit();
                    editor.putString("AUTH_TOKEN", userDetails.getString("data"));
                    editor.putBoolean("LOGGED_IN", true);
                    editor.putBoolean("REGISTER", true);
                    editor.commit();
                    Toast.makeText(context, "Logged in Successfully", Toast.LENGTH_LONG).show();}
                } else {
                    if (this.type == "register")
                        Toast.makeText(context, "Register Failed", Toast.LENGTH_LONG);
                    else if (this.type == "login")
                        Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG);
                }
        }

    }

    @Override
    public void handleNewLocation(Location location) {
        this.currentLatitude = location.getLatitude();
        this.currentLongitude = location.getLongitude();
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
