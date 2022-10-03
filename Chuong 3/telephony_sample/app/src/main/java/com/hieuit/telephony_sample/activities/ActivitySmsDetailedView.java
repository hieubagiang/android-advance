package com.hieuit.telephony_sample.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hieuit.telephony_sample.MainActivity;
import com.hieuit.telephony_sample.R;
import com.hieuit.telephony_sample.adapters.MessageListAdapter;
import com.hieuit.telephony_sample.models.ContactModel;
import com.hieuit.telephony_sample.models.MessageModel;

import java.util.Date;

public class ActivitySmsDetailedView extends AppCompatActivity {
    private static final String LOG_TAG = "HieuLog";
    private static final int MY_PERMISSION_REQUEST_CODE_SEND_SMS = 100;
    RecyclerView recyclerView;
    MessageListAdapter customAdapter;
    ContactModel contactModel;
    EditText editText;
    ImageView btnSend;
    String currentPhoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_detailed_view);

        contactModel = (ContactModel) getIntent().getSerializableExtra("contact");
        recyclerView = findViewById(R.id.recyclerview);
        customAdapter = new MessageListAdapter(ActivitySmsDetailedView.this, contactModel.getMessages());
        recyclerView.setAdapter(customAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        btnSend = findViewById(R.id.sendButton);
        editText = findViewById(R.id.etMessage);
        btnSend.setOnClickListener(v -> {
            sendSMS_by_smsManager();
        });
        askPermissionAndSendSMS(ActivitySmsDetailedView.this, this);
        TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        currentPhoneNumber = tMgr.getLine1Number();

    }

    private void askPermissionAndSendSMS(Activity activity, Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_MMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_WAP_PUSH) != PackageManager.PERMISSION_GRANTED  ) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_SMS,
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.RECEIVE_MMS,
                            Manifest.permission.RECEIVE_WAP_PUSH},
                    100);
        }
        // With Android Level >= 23, you have to ask the user
        // for permission to send SMS.
//        if (android.os.Build.VERSION.SDK_INT >=  android.os.Build.VERSION_CODES.M) { // 23
//
//            // Check if we have send SMS permission
//            int sendSmsPermisson = ActivityCompat.checkSelfPermission(this,
//                    Manifest.permission.SEND_SMS);
//
//            if (sendSmsPermisson != PackageManager.PERMISSION_GRANTED) {
//                // If don't have permission so prompt the user.
//                this.requestPermissions(
//                        new String[]{Manifest.permission.SEND_SMS},
//                        MY_PERMISSION_REQUEST_CODE_SEND_SMS
//                );
//                return;
//            }
//        }
    }

    private void sendSMS_by_smsManager()  {

        String phoneNumber = currentPhoneNumber;
        String message = this.editText.getText().toString();
        if(message.isEmpty()) return;
//String message="";
        try {
            // Get the default instance of the SmsManager
            SmsManager smsManager = SmsManager.getDefault();
            // Send Message
            smsManager.sendTextMessage(contactModel.getPhone(),
                    null,
                    message,
                    null,
                    null);

            editText.setText("");
            Log.i( LOG_TAG,"Your sms has successfully sent!");
            Toast.makeText(getApplicationContext(),"Your sms has successfully sent!",
                    Toast.LENGTH_LONG).show();
            MessageModel messageModel= new MessageModel(phoneNumber, message, new Date(System.currentTimeMillis()));
            contactModel.addMessage(messageModel);
            customAdapter.notifyDataSetChanged();
        } catch (Exception ex) {
            Log.e( LOG_TAG,"Your sms has failed...", ex);
            Toast.makeText(getApplicationContext(),"Your sms has failed... " + ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }


    // When you have the request results
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE_SEND_SMS: {

                // Note: If request is cancelled, the result arrays are empty.
                // Permissions granted (SEND_SMS).
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.i( LOG_TAG,"Permission granted!");
                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_LONG).show();

                    this.sendSMS_by_smsManager();
                }
                // Cancelled or denied.
                else {
                    Log.i( LOG_TAG,"Permission denied!");
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    // When results returned
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_PERMISSION_REQUEST_CODE_SEND_SMS) {
            if (resultCode == RESULT_OK) {
                // Do something with data (Result returned).
                Toast.makeText(this, "Action OK", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action canceled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void finish() {
        Intent data = new Intent();
        setResult(RESULT_OK, data);

        super.finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

}
