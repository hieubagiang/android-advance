package com.hieuit.telephony_sample.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.hieuit.telephony_sample.MainActivity;
import com.hieuit.telephony_sample.R;
import com.hieuit.telephony_sample.activities.ActivitySmsDetailedView;
import com.hieuit.telephony_sample.models.ContactModel;
import com.hieuit.telephony_sample.models.MessageModel;

import java.util.ArrayList;

public class MessageContactAdapter extends RecyclerView.Adapter<MessageContactAdapter.MyViewHolder> {

    private Activity activity;
    private ArrayList<ContactModel> contactModels;

    public MessageContactAdapter(MainActivity activity, ArrayList<ContactModel> contactModels) {
        this.activity = activity;
        this.contactModels = contactModels;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.single_sms_small_layout, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
//        holder.txtName.setText(String.valueOf(githubUserModels.get(position).getLogin()));
//        holder.txtHomeUrl.setText(String.valueOf(githubUserModels.get(position).getHtmlUrl()));
//        DownloadImageTask task = new DownloadImageTask(holder.imgAvatar);
//        task.execute(githubUserModels.get(position).getAvatarUrl());
        ContactModel contactModel = contactModels.get(position);
        holder.txtSender.setText(contactModel.getPhone());
        holder.txtSmsContent.setText(contactModel.getMessages().isEmpty() ? "": contactModel.getMessages().get(0).getBody());
        holder.txtTime.setText(contactModel.getMessages().get(0).getLastTimeString());
    }

    @Override
    public int getItemCount() {
        return contactModels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtSender, txtSmsContent, txtTime;
        ImageView imgAvatar;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSender = itemView.findViewById(R.id.smsSender);
            txtSmsContent = itemView.findViewById(R.id.smsContent);
            txtTime = itemView.findViewById(R.id.time);
        }

    }

}
