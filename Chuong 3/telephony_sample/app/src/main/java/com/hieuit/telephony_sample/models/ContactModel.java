package com.hieuit.telephony_sample.models;

import android.os.Build;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.io.Serializable;

public class ContactModel implements Serializable  {
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
        public void addMessage(MessageModel messageModel){
        this.messages.add(0,messageModel);
    }
    public ArrayList<MessageModel> getMessages() {
        return messages;
    }
    public void sortMessageToNewest(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            messages.sort(((o1, o2) -> o2.getDate().compareTo(o1.getDate())));
        }
    }
    public Date getLastTime() {
        return messages.get(0).getDate();
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

    @Override
    public String toString() {
        return "ContactModel{" +
                "phone='" + phone + '\'' +
                ", messages=" + messages +
                '}';
    }
}
