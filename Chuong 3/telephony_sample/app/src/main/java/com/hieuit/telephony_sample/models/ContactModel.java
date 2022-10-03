package com.hieuit.telephony_sample.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.io.Serializable;

public class ContactModel implements Serializable {
    private String phone;
    private ArrayList<MessageModel> messages = new ArrayList<>();
    public ContactModel() {
    }

    public ContactModel(String phone) {
        this.phone = phone;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



    public boolean isToday(Date date) {

        return false;
    }

    public ArrayList<MessageModel> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<MessageModel> messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContactModel)) return false;
        ContactModel that = (ContactModel) o;
        return Objects.equals(getPhone(), that.getPhone());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPhone());
    }
}
