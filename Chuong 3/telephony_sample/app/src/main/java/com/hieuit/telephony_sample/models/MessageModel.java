package com.hieuit.telephony_sample.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.Serializable;

public class MessageModel implements Serializable {
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

    public String getBody() {
        return body;
    }

    public void setLastMessage(String body) {
        this.body = body;
    }

    public Date getLastTime() {
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
}
