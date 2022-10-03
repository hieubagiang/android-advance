package com.hieuit.telephony_sample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class SMSActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST_CODE_SEND_SMS = 1;
    private Button btnSend;
    private Button btnSendSmsManager;
    private EditText txtPhoneNumber;
    private EditText txtMessageContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        btnSend = (Button) findViewById(R.id.btnSendSMS);
        btnSendSmsManager = (Button) findViewById(R.id.btnSendSMSManager);
        txtPhoneNumber = (EditText) findViewById(R.id.txt_phone);
        txtMessageContent = (EditText) findViewById(R.id.sms_content);

        btnSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendSMSMessage();
            }
        });

        btnSendSmsManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissionAndSendSMS();
            }
        });
    }

    protected void sendSMSMessage() {
        String phoneNo = txtPhoneNumber.getText().toString().trim();
        String message = txtMessageContent.getText().toString().trim();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("address", phoneNo);
        intent.putExtra("sms_body", message);
        intent.setData(Uri.parse("smsto:" + phoneNo));
        intent.setType("vnd.android-dir/mms-sms");
        startActivity(intent);
    }

    private void requestPermissionAndSendSMS() {

        // With Android Level >= 23, you have to ask the user
        // for permission to send SMS.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) { // 23

            // Check if we have send SMS permission
            int sendSmsPermisson = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.SEND_SMS);

            if (sendSmsPermisson != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSION_REQUEST_CODE_SEND_SMS
                );
                return;
            }
        }
        try {
            String phoneNo = txtPhoneNumber.getText().toString().trim();
            String message = txtMessageContent.getText().toString().trim();
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(SMSActivity.this, "Send message success!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(SMSActivity.this, "send error", Toast.LENGTH_SHORT).show();
        }
    }
}