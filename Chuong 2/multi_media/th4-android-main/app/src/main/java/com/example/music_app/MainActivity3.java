package com.example.music_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        try {
            VideoView video=(VideoView) findViewById(R.id.videoView2);
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(video);
            video.setMediaController(mediaController);
            video.setKeepScreenOn(true);
            video.setVideoPath(Utils.getFilePath("test2.mp4"));
            video.start();
            video.requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}