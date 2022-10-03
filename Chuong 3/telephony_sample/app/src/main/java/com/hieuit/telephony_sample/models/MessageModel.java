package com.hieuit.telephony_sample.models;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.io.Serializable;

public class MessageModel implements Serializable, Comparator<MessageModel> {
    private String phone;
    private String body;
    private Date time;

    public MessageModel() {
    }

    public MessageModel(String phone, String body, Date time) {
        this.phone = phone;
        this.body = body;
        this.time = time;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @SuppressLint("MissingPermission")
    public boolean isMe(Context context) {
        TelephonyManager tMgr = (TelephonyManager) context  .getSystemService(Context.TELEPHONY_SERVICE);

        return tMgr.getLine1Number().equals(phone);
    }

    public String getBody() {
        return body;
    }

    public void setLastMessage(String body) {
        this.body = body;
    }

    public Date getDate() {
        return time;
    }

    public String getLastTimeString() {
        if(time == null) return "";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        if (isToday(time)) {
            formatter = new SimpleDateFormat("HH:mm");
        }
        return formatter.format(time);
    }

    public void setLastTime(Date time) {
        this.time = time;
    }

    public boolean isToday(Date date) {

        return false;
    }

    @Override
    public int compare(MessageModel o1, MessageModel o2) {
        return o2.time.compareTo(o1.time);
    }

}
