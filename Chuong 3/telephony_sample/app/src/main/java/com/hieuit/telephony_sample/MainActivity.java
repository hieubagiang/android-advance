package com.hieuit.telephony_sample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.hieuit.telephony_sample.adapters.MessageListAdapter;
import com.hieuit.telephony_sample.models.ContactModel;
import com.hieuit.telephony_sample.models.MessageModel;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    RecyclerView recyclerView;
    MessageListAdapter customAdapter;
    ArrayList<ContactModel> contactModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactModels = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerviewChatList);
        customAdapter = new MessageListAdapter(MainActivity.this,this,contactModels);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));


        int permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_SMS);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted
            showContact();
        } else {
            // Permission is revoked
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }

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

    private void showContact(){
        Uri uri = Uri.parse("content://sms/");
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
        customAdapter.notifyDataSetChanged();
    }
}