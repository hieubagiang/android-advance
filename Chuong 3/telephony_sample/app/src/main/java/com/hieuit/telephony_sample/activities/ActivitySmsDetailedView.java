package com.hieuit.telephony_sample.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hieuit.telephony_sample.MainActivity;
import com.hieuit.telephony_sample.R;
import com.hieuit.telephony_sample.adapters.MessageListAdapter;
import com.hieuit.telephony_sample.models.ContactModel;

public class ActivitySmsDetailedView extends AppCompatActivity {
    RecyclerView recyclerView;
    MessageListAdapter customAdapter;
    ContactModel contactModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_detailed_view);
        contactModel = (ContactModel) getIntent().getSerializableExtra("contact");
        recyclerView = findViewById(R.id.recyclerview);
        customAdapter = new MessageListAdapter(ActivitySmsDetailedView.this,contactModel.getMessages());
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ActivitySmsDetailedView.this));

    }
}
