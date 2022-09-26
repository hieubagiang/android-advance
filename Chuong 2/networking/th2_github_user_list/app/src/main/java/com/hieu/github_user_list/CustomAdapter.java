package com.hieu.github_user_list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.hieu.github_user_list.models.GithubUserModel;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList<GithubUserModel> githubUserModels;

    public CustomAdapter(MainActivity activity, MainActivity context, ArrayList<GithubUserModel> githubUserModels) {
        this.activity = activity;
        this.context = context;
        this.githubUserModels = githubUserModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_github_user, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.txtName.setText(String.valueOf(githubUserModels.get(position).getLogin()));
        holder.txtHomeUrl.setText(String.valueOf(githubUserModels.get(position).getHtmlUrl()));
        DownloadImageTask task = new DownloadImageTask(holder.imgAvatar);
        task.execute(githubUserModels.get(position).getAvatarUrl());
    }

    @Override
    public int getItemCount() {
        return githubUserModels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtHomeUrl;
        ImageView imgAvatar;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtHomeUrl = itemView.findViewById(R.id.txtHomeUrl);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
        }

    }

}
