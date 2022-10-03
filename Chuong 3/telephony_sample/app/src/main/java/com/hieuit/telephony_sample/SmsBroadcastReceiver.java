package com.hieuit.telephony_sample;

import android.annotation.TargetApi;
import static android.content.ContentValues.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.hieuit.telephony_sample.interfaces.OnNewMessageListener;
import com.hieuit.telephony_sample.models.ContactModel;
import com.hieuit.telephony_sample.models.MessageModel;

import java.util.Date;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    OnNewMessageListener onNewMessageListener;

    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    public SmsBroadcastReceiver() {
    }

    public SmsBroadcastReceiver(OnNewMessageListener onNewMessageListener) {
        this.onNewMessageListener = onNewMessageListener;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null && ACTION.compareTo(intent.getAction()) == 0) {
//            Object[] pduArray = (Object[]) intent.getExtras().get("pdus");
//            SmsMessage[] messages = new SmsMessage[pduArray.length];
//            for(int i =0; i< pduArray.length; i++){
//                messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i]);
//                System.out.println("From: " + messages[i].getOriginatingAddress());
//                System.out.println("message: " + messages[i].getMessageBody());
//            }
//            System.out.println("SMS received");


            // Get the SMS message.
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs;
            String strMessage = "";
            String format = bundle.getString("format");
            // Retrieve the SMS message received.
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                // Check the Android version.
                boolean isVersionM =
                        (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
                // Fill the msgs array.
                msgs = new SmsMessage[pdus.length];
                for (int i = 0; i < msgs.length; i++) {
                    // Check Android version and use appropriate createFromPdu.
                    if (isVersionM) {
                        // If Android version M or newer:
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                    } else {
                        // If Android version L or older:
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
                    // Build the message to show.
                    strMessage += "SMS from " + msgs[i].getOriginatingAddress();
                    strMessage += " :" + msgs[i].getMessageBody() + "\n";
                    // Log and display the SMS message.
                    Log.d(TAG, "onReceive: " + strMessage);
                    Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();
                    String number = msgs[i].getOriginatingAddress();
                    String body = msgs[i].getMessageBody();

                    ContactModel contactModel = new ContactModel();
                    contactModel.setPhone(number);
                    contactModel.getMessages().add(new MessageModel(number,body,new Date(msgs[i].getTimestampMillis())));
                    System.out.println(contactModel);
                    onNewMessageListener.onNewMessageReceived(contactModel);
                }
            }
        }

    }
}