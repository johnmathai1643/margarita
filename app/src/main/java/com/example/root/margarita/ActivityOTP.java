package com.example.root.margarita;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.margarita.util.AsyncTasker;
import com.example.root.margarita.util.GlobalVar;

import butterknife.ButterKnife;
import butterknife.Bind;


public class ActivityOTP extends AppCompatActivity{
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static final String SHAREDPREF_USER = "USER_CREDENTIALS";
    private String latitude,longitude;

    @Bind(R.id.input_email) EditText _phoneText;
    @Bind(R.id.btn_login) Button _loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    public void login() {
        Log.d(TAG, "OTP");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ActivityOTP.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String otp = _phoneText.getText().toString();

        SharedPreferences user_sp = getSharedPreferences(SHAREDPREF_USER, Context.MODE_PRIVATE);
        Boolean LOCATION_ON = user_sp.getBoolean("LOCATION_ON",false);
        String mobile_phone= user_sp.getString("PHONE",null);
        AsyncTask<Void, Void, Void> AuthenticatorTasker_object;

        AsyncTasker mAsyncTasker = new AsyncTasker(GlobalVar.URL+"api/user/auth",""+otp+":"+mobile_phone,1);
        mAsyncTasker.updateOTP(GlobalVar.URL+"api/user/auth",""+otp+":"+mobile_phone,this.getApplicationContext());

        progressDialog.dismiss();
        onLoginSuccess();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        Intent intent;
        intent = new Intent(ActivityOTP.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "OTP failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _phoneText.getText().toString();

        if (email.isEmpty() || !Patterns.PHONE.matcher(email).matches()) {
            _phoneText.setError("enter a valid OTP number");
            valid = false;
        } else {
            _phoneText.setError(null);
        }

        return valid;
    }
}
