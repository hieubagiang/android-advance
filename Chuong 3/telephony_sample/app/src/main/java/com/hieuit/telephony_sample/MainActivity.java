package com.hieuit.telephony_sample;

import static java.security.AccessController.getContext;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hieuit.telephony_sample.activities.ActivitySmsDetailedView;
import com.hieuit.telephony_sample.adapters.MessageContactAdapter;
import com.hieuit.telephony_sample.interfaces.OnNewMessageListener;
import com.hieuit.telephony_sample.interfaces.RecyclerItemClickListener;
import com.hieuit.telephony_sample.models.ContactModel;
import com.hieuit.telephony_sample.models.MessageModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    RecyclerView recyclerView;
    MessageContactAdapter customAdapter;
    ArrayList<ContactModel> contactModels;
    private SmsBroadcastReceiver smsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactModels = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerviewChatList);
        customAdapter = new MessageContactAdapter(MainActivity.this,contactModels);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(MainActivity.this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {

                    @Override public void onItemClick(View view, int position) {
                        Log.e("hieudt","onItemClick" + position);
                        Intent myIntent = new Intent(MainActivity.this, ActivitySmsDetailedView.class);
                        myIntent.putExtra("contact", contactModels.get(position)); // sending our object. In Kotlin is the same
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);


                        startActivity(myIntent);
                        someActivityResultLauncher.launch(myIntent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );


        int permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_SMS);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted
            showContact();
        } else {
            // Permission is revoked
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        OnNewMessageListener onNewMessageListener = contactModel -> {
            if(contactModels.contains(contactModel)){
                int index = contactModels.indexOf(contactModel);
                contactModels.get(index).getMessages().add(0,contactModel.getMessages().get(0));
            }else{
                contactModels.add(contactModel);
            }
            sortContact();
        };
        smsListener = new SmsBroadcastReceiver(onNewMessageListener);

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        if (getContext() != null)
        {
            getApplication().registerReceiver(smsListener,
                    filter);

        }

    }

    private void sortContact() {
        for (int i = 0; i < contactModels.size(); i++) {
            contactModels.get(i).sortMessageToNewest();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            contactModels.sort(((o1, o2) -> o2.getLastTime().compareTo(o1.getLastTime())));
        }
        customAdapter.notifyDataSetChanged();
    }

    private void smsReceiverHandler() {
        // Get an instance of SmsRetrieverClient, used to start listening for a matching
        // SMS message.


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSIONS_REQUEST_READ_CONTACTS){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                showContact();
            }
        }else{
            Toast.makeText(this, "You must accept this permission to display contacts", Toast.LENGTH_SHORT).show();
        }
    }
    private  void showContact(){
        getContact("content://sms/inbox");
        getContact("content://sms/sent");

        sortContact();

    }
    private void getContact(String contentPath){
        Uri uri = Uri.parse(contentPath);
        Cursor c = getContentResolver().query(uri, null, null, null, "date desc");
        while (c.moveToNext()){
            ContactModel contactModel = new ContactModel();
            String number= (c.getString(c.getColumnIndexOrThrow("address")));
            String body= (c.getString(c.getColumnIndexOrThrow("body")));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(c.getString(c.getColumnIndexOrThrow("date"))));

            contactModel.setPhone(number);
            if(contactModels.contains(contactModel)) {
                int index = contactModels.indexOf(contactModel);
                contactModels.get(index).getMessages().add(new MessageModel(number, body, calendar.getTime()));
            }else{
                contactModel.getMessages().add(new MessageModel(number, body, calendar.getTime()));
                contactModels.add(contactModel);

            }
            c.moveToNext();
        }

        c.close();
        sortContact();
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK || result.getResultCode()== Activity.RESULT_CANCELED) {
                        // There are no request codes
                        contactModels.clear();
                        showContact();


                    }
                }
            });


}
