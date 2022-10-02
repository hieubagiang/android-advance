package com.hieuit.telephony_sample;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CallingActivity extends AppCompatActivity {

    private Button btnCall;
    private EditText txtPhone;
    private final static String TAG = CallingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        txtPhone = (EditText) findViewById(R.id.txt_phone);
        btnCall = (Button) findViewById(R.id.btn_call);

        // add PhoneStateListener for monitoring
        CustomPhoneListener phoneListener = new CustomPhoneListener();
        TelephonyManager telephonyManager =
        (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        // receive notifications of telephony state changes
        telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);


        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtPhone.getText().toString().trim().equals("")) {
                    Toast.makeText(CallingActivity.this, "Please input a phone number", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        callPhone(txtPhone.getText().toString());
//                        String uri = "tel:" + txtPhone.getText().toString();
//                            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));
//                        if (ActivityCompat.checkSelfPermission(CallingActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                            return;
//                        }
//                        startActivity(callIntent);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Your call has failed...", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private class CustomPhoneListener extends PhoneStateListener {

        private boolean onCall = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    // phone ringing...
                    Log.i(TAG, "Destination phone is ringing, please wait...");
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    // one call exists that is dialing, active, or on hold
                    Log.i(TAG, "on Calling...");

                    //because user answers the incoming call
                    onCall = true;
                    break;

                case TelephonyManager.CALL_STATE_IDLE:
                    // in initialization of the class and at the end of phone call
                    // detect flag from CALL_STATE_OFFHOOK
                    if (onCall) {
                        Log.i(TAG, "Restarting app after a call...!");

                        // restart our application
                        Intent restart = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                        restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(restart);

                        onCall = false;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void callPhone(String phoneNumber) {
        if (!isValidPhoneNumber(phoneNumber)) return;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            // request the permission.
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CALL_PHONE },
                    1);
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }
    private Boolean isValidPhoneNumber(String number){
        return number != null && number.length() == 10;
    }

}
