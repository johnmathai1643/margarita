package com.example.root.margarita.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.root.margarita.MainActivity;

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
import org.json.JSONStringer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.net.ssl.HttpsURLConnection;

public class AsyncTasker {
    private String URL,in_jsonstring,TAG = MainActivity.class.getSimpleName();
    private int type;
    private static final String SHAREDPREF_USER = "USER_CREDENTIALS";

    public AsyncTasker(String URL, String jsonstring, int type) {
        this.URL = URL;
        this.in_jsonstring = jsonstring;
        this.type = type;
    }


    class AsyncTaskRunner1 extends AsyncTask<Void, Void, Void> {
        private String URL,in_jsonstring,jsonstring, TAG = MainActivity.class.getSimpleName();
        private static final String SHAREDPREF_USER = "USER_CREDENTIALS";
        private Context context;

        public AsyncTaskRunner1(String URL, String jsonstring, Context applicationContext) {
            this.URL = URL;this.in_jsonstring = jsonstring;
            this.context = applicationContext;
        }

        @Override
        protected void onPreExecute() {
//        this.dialog.show(activity, "Registering", "Please wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {

            DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
            HttpPost httppost = new HttpPost(URL+"?");

            Log.i(TAG, URL);
            JSONObject user_params = new JSONObject();
            String uid = "000000000000";

            try {
                JSONObject jObject = new JSONObject(this.in_jsonstring);
                JSONObject userDetails = jObject.getJSONObject("PrintLetterBarcodeData");
                uid = userDetails.getString("uid");
                Log.d(TAG,uid);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            SharedPreferences user_sp = context.getSharedPreferences(SHAREDPREF_USER, Context.MODE_PRIVATE);
            String phone = user_sp.getString("PHONE",null);
            String session = user_sp.getString("AUTH_TOKEN",null);

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("phone",phone));
                nameValuePairs.add(new BasicNameValuePair("session",session));
                nameValuePairs.add(new BasicNameValuePair("aadhar",uid));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d(TAG,"try basic value pairs...............");
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }

            InputStream inputstream = null;
            try {
                Log.d(TAG,"Check response.............");
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                inputstream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
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
                get_response();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public void get_response() throws JSONException {
            if (jsonstring != null) {
                JSONObject userDetails = new JSONObject(jsonstring);
                Log.i(TAG, userDetails.toString());
                Log.i(TAG, userDetails.getString("data"));

                if ("OK".equals(userDetails.getString("status"))) {
//                    Toast.makeText(context, userDetails.getString("data"), Toast.LENGTH_SHORT).show();
                    Log.d(TAG,userDetails.getString("data"));
                }
            }

        }

    }

    class AsyncTaskRunner2 extends AsyncTask<Void, Void, Void> {
        private String URL,in_jsonstring,jsonstring, TAG = MainActivity.class.getSimpleName();
        private static final String SHAREDPREF_USER = "USER_CREDENTIALS";

        public AsyncTaskRunner2(String URL,String jsonstring) {
            this.URL = URL;this.in_jsonstring = jsonstring;
        }

        @Override
        protected void onPreExecute() {
//        this.dialog.show(activity, "Registering", "Please wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {

            DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
            HttpPost httppost = new HttpPost(URL+"?");

            Log.i(TAG, URL);
            JSONObject user_params = new JSONObject();
            String uid = "000000000000";
            Log.d(TAG,in_jsonstring);
            StringTokenizer tokens = new StringTokenizer(in_jsonstring, ":");
            String lat = tokens.nextToken();
            String lng = tokens.nextToken();
            String phone = tokens.nextToken();

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("phone",phone));
                nameValuePairs.add(new BasicNameValuePair("session",GlobalVar.getToken()));
                nameValuePairs.add(new BasicNameValuePair("lat",lat));
                nameValuePairs.add(new BasicNameValuePair("long",lng));
                nameValuePairs.add(new BasicNameValuePair("safe","false"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d(TAG,"try basic value pairs...............");
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }

            InputStream inputstream = null;
            try {
                Log.d(TAG,"Check response.............");
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                inputstream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
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
                get_response();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public void get_response() throws JSONException {
            if (jsonstring != null) {
                JSONObject userDetails = new JSONObject(jsonstring);
                Log.i(TAG, userDetails.toString());
                Log.i(TAG, userDetails.getString("data"));

                if ("OK".equals(userDetails.getString("status"))) {
//                    Toast.makeText(context, userDetails.getString("data"), Toast.LENGTH_SHORT).show();
                    Log.d(TAG,userDetails.getString("data"));
                }
            }

        }

    }


    class AsyncTaskRunner3 extends AsyncTask<Void, Void, Void> {
        private String URL,in_jsonstring,jsonstring, TAG = MainActivity.class.getSimpleName();
        private Context context;
        private static final String SHAREDPREF_USER = "USER_CREDENTIALS";

        public AsyncTaskRunner3(String URL, String jsonstring, Context applicationContext) {
            this.URL = URL;this.in_jsonstring = jsonstring;
            this.context = applicationContext;
        }

        @Override
        protected void onPreExecute() {
//        this.dialog.show(activity, "Registering", "Please wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {

            DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
            HttpPost httppost = new HttpPost(URL+"?");
            Log.i(TAG, URL);

            Log.d(TAG,in_jsonstring);
            StringTokenizer tokens = new StringTokenizer(in_jsonstring, ":");
            String OTP = tokens.nextToken();
            String phone = tokens.nextToken();

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("phone",phone));
                nameValuePairs.add(new BasicNameValuePair("otp",OTP));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d(TAG,"try basic value pairs...............");
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }

            InputStream inputstream = null;
            try {
                Log.d(TAG,"Check response.............");
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                inputstream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
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
                get_response();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public void get_response() throws JSONException {
            if (jsonstring != null) {
                JSONObject userDetails = new JSONObject(jsonstring);
                Log.i(TAG, userDetails.toString());
                Log.i(TAG, userDetails.getString("data"));

                if ("OK".equals(userDetails.getString("status"))) {
                    GlobalVar.setToken(userDetails.getString("data"));
                    SharedPreferences user_sp = context.getSharedPreferences(SHAREDPREF_USER, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = user_sp.edit();
                    editor.putString("AUTH_TOKEN", userDetails.getString("data"));

                    if(GlobalVar.getToken() != null && !GlobalVar.getToken().isEmpty()) {
                        Log.d(TAG,GlobalVar.getToken());
                        editor.putString("AUTH_TOKEN", GlobalVar.getToken());
                    }
                    String session_null= user_sp.getString("AUTH_TOKEN",null);
                    if(session_null != null && !session_null.isEmpty())
                        GlobalVar.setToken(session_null);

                    editor.putString("AUTH_TOKEN", userDetails.getString("data"));
                    Log.d(TAG+"..............",userDetails.getString("data"));
                }
            }

        }

    }

    class AsyncTaskRunner4 extends AsyncTask<Void, Void, Void> {
        private String URL,in_jsonstring,jsonstring, TAG = MainActivity.class.getSimpleName();
        private Context context;

        public AsyncTaskRunner4(String URL, String jsonstring, Context applicationContext) {
            this.URL = URL;this.in_jsonstring = jsonstring;
            this.context = applicationContext;
        }

        @Override
        protected void onPreExecute() {
//        this.dialog.show(activity, "Registering", "Please wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {

            DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
            HttpPost httppost = new HttpPost(URL+"?");

            Log.i(TAG, URL);
            Log.d(TAG,in_jsonstring);

            StringTokenizer tokens = new StringTokenizer(in_jsonstring, ":");
            String phone = tokens.nextToken();
            String session = tokens.nextToken();
            String friend = tokens.nextToken();

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("phone",phone));
                nameValuePairs.add(new BasicNameValuePair("session",session));
                nameValuePairs.add(new BasicNameValuePair("friend",friend));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d(TAG,"try basic value pairs...............");
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }

            InputStream inputstream = null;
            try {
                Log.d(TAG,"Check response.............");
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                inputstream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
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
                get_response();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public void get_response() throws JSONException {
            if (jsonstring != null) {
                JSONObject userDetails = new JSONObject(jsonstring);
                Log.i(TAG, userDetails.toString());
                Log.i(TAG, userDetails.getString("data"));

                if ("OK".equals(userDetails.getString("status"))) {
                    Toast.makeText(context, "Friend Added", Toast.LENGTH_SHORT).show();
                    Log.d(TAG,userDetails.getString("data"));
                }
            }

        }

    }



    public void updateaadhar(String URL, String in_jsonstring, Context applicationContext) {
        AsyncTask<Void, Void, Void> mAsyncTaskRunner1 = new AsyncTaskRunner1(GlobalVar.URL+"api/s/user/updatestatusaadhar",in_jsonstring,applicationContext).execute();
    }

    public void updateprofile(String URL,String jsonstring){


    }

    public void updatelocation(String URL, String jsonstring) {
        AsyncTask<Void, Void, Void> mAsyncTaskRunner2 = new AsyncTaskRunner2(GlobalVar.URL+"api/s/user/updatestatus",in_jsonstring).execute();
    }

    public void updateOTP(String URL, String jsonstring, Context applicationContext) {
        AsyncTask<Void, Void, Void> mAsyncTaskRunner3 = new AsyncTaskRunner3(GlobalVar.URL+"api/user/auth",in_jsonstring,applicationContext).execute();
    }

    public void updatefriend(String URL, String jsonstring, Context applicationContext) {
        AsyncTask<Void, Void, Void> mAsyncTaskRunner4 = new AsyncTaskRunner4(GlobalVar.URL+"api/s/user/addfriend",in_jsonstring,applicationContext).execute();
    }

}
