package com.example.root.margarita;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.example.root.margarita.util.XML;

import org.json.JSONException;
import org.json.JSONObject;

public class DecoderActivity extends Activity implements QRCodeReaderView.OnQRCodeReadListener {

//    private TextView myTextView;
    ProgressDialog pDialog;
    SharedPreferences sharedPreferences;
    TextView myTextView;
    String auth_pin,user_roll,user_hash;
    private QRCodeReaderView mydecoderview;
    private ImageView line_image;
    boolean QRcoderead=false;
    String gender=null;
    private String TAG = "DecoderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoder);

        myTextView = (TextView) findViewById(R.id.exampleTextView);
        line_image = (ImageView) findViewById(R.id.red_line_image);
        line_image.bringToFront();

        mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        mydecoderview.setOnQRCodeReadListener(this);


        TranslateAnimation mAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.5f);
        mAnimation.setDuration(1000);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mAnimation.setInterpolator(new LinearInterpolator());
        line_image.setAnimation(mAnimation);

//        myTextView = (TextView) findViewById(R.id.exampleTextView);
    }


    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed
    @Override
    public void onQRCodeRead(String text, PointF[] points) {

        if(!QRcoderead) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Scanning QRCode...");
            pDialog.setCancelable(true);
            pDialog.setCanceledOnTouchOutside(true);
            pDialog.show();
            QRcoderead = true;
//            String list[] = text.split("&");
//            String temp[] = list[1].split("=");
//            user_roll = temp[1];
//            temp = list[2].split("=");
//            user_hash = temp[1];
            Toast.makeText(this.getApplicationContext(),text,Toast.LENGTH_LONG).show();
            xml_to_json(text);
            pDialog.setCancelable(true);
        }

    }

    public void xml_to_json(String xml) {

         int PRETTY_PRINT_INDENT_FACTOR = 4;
//        String TEST_XML_STRING = "<?xml version=\"1.0\" ?><test attrib=\"moretest\">Turn this to JSON</test>";

            try {
                JSONObject xmlJSONObj = XML.toJSONObject(xml);
                String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
                Log.d(TAG,jsonPrettyPrintString);
            } catch (JSONException je) {
                Log.d(TAG,je.toString());
            }
    }

    // Called when your device have no camera
    @Override
    public void cameraNotFound() {

    }

    // Called when there's no QR codes in the camera preview image
    @Override
    public void QRCodeNotFoundOnCamImage() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mydecoderview.getCameraManager().startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mydecoderview.getCameraManager().stopPreview();
    }
}