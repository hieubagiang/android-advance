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
import com.hieuit.telephony_sample.models.ContactModel;
import com.hieuit.telephony_sample.models.MessageModel;

import java.util.ArrayList;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<MessageModel> messages;

    public MessageListAdapter(Context context,  ArrayList<MessageModel> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_sms_detailed, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
//        holder.txtName.setText(String.valueOf(githubUserModels.get(position).getLogin()));
//        holder.txtHomeUrl.setText(String.valueOf(githubUserModels.get(position).getHtmlUrl()));
//        DownloadImageTask task = new DownloadImageTask(holder.imgAvatar);
//        task.execute(githubUserModels.get(position).getAvatarUrl());
        MessageModel model = messages.get(position);
        holder.txtSmsContent.setText(model.getBody());
        holder.txtTime.setText(model.getLastTimeString());
        holder.imgAvatar.setVisibility(model.isMe(context) ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtSmsContent, txtTime;
        ImageView imgAvatar;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSmsContent =(TextView)  itemView.findViewById(R.id.singleSmsDetailedMessage);
            txtTime = (TextView) itemView.findViewById(R.id.singleSmsDetailedTime);
            imgAvatar = itemView.findViewById(R.id.smsImage);
        }

    }

}
