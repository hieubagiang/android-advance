package com.hieuit.telephony_sample.interfaces;

import android.view.View;

import java.util.Date;

public interface OnItemActionListener {
    void onTap(View view, int position);
    void onLongTap(View view, int position);
    void onDoubleTap(View view, int position);
}
