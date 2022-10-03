package com.hieuit.telephony_sample.interfaces;

import com.hieuit.telephony_sample.models.ContactModel;

public interface OnNewMessageListener {
    void onNewMessageReceived(ContactModel contactModel);

}
