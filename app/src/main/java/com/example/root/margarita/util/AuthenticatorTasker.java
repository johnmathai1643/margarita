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
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AuthenticatorTasker extends AsyncTask<Void,Void,Void> implements LocationProvider.LocationCallback,GoogleMap.OnMarkerDragListener  {

    private static final String TAG = AuthenticatorTasker.class.getName();
    private String jsonstring = " ";
    public String phone,type,lat,lng;
    private ProgressDialog progressDialog;
    private Context context;
    private String URL;
//    private DatabaseHandler db;
    private static final String SHAREDPREF_USER = "USER_CREDENTIALS";
    private double currentLatitude;
    private double currentLongitude;

    public AuthenticatorTasker(String phone, String lat, String lng, Context context, ProgressDialog progressDialog){
        this.phone = phone;
        this.lat = lat;
        this.lng = lng;
        this.URL = GlobalVar.URL+"api/user/create";
        this.type = "register";
        this.context = context;
        this.progressDialog = progressDialog;
    }

    public AuthenticatorTasker(String phone, Context context, ProgressDialog progressDialog) {
        this.phone = phone;
        this.URL = GlobalVar.URL+"api/user/create";
        this.type = "register";
        this.context = context;
        this.progressDialog = progressDialog;
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

        try {
            if(this.type.equals("register")){
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("phone",phone));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            }
            else
            {List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
             nameValuePairs.add(new BasicNameValuePair("phone",phone));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            }
            Log.d(TAG,"try basic value pairs...............");
        } catch (IOException e) {
            // TODO Auto-generated catch block
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
            Log.d(TAG,jsonstring);
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

    protected void get_auth_token() throws JSONException {

        if (jsonstring != null) {
            JSONObject userDetails = new JSONObject(jsonstring);
            Log.i(TAG, userDetails.toString());
//            SessionManager mSessionManager = new SessionManager(activity);
//            mSessionManager.set_session();
            Log.i(TAG, userDetails.getString("data"));
            if ("OK".equals(userDetails.getString("status"))) {
                if (this.type == "register") {
                    SharedPreferences user_sp = context.getSharedPreferences(SHAREDPREF_USER, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = user_sp.edit();
                    editor.putString("PHONE", phone);
                    GlobalVar.setToken(userDetails.getString("data"));
                    GlobalVar.setphone(phone);
                    editor.putBoolean("LOGGED_IN", true);
                    editor.putBoolean("REGISTER", true);
                    editor.commit();
                    Toast.makeText(context, "Register Successful", Toast.LENGTH_SHORT).show();}
                else if (this.type == "login") {
                    SharedPreferences user_sp = context.getSharedPreferences(SHAREDPREF_USER, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = user_sp.edit();
                    editor.putString("PHONE", phone);
                    GlobalVar.setToken(userDetails.getString("data"));
                    GlobalVar.setphone(phone);
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
