package com.example.music_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView txtTitle, txtTimeSong, txtTimeTotal;
    SeekBar skSong;
    ImageButton btnPrev, btnStop, btnPlay, btnNext;
    MediaPlayer mediaPlayer;
    ArrayList<song> arraySong;
    int position = 0;
    Handler handler;
    Button networkUrlVideo;
    Button localUrlVideo;

    @Override
    protected void onDestroy() {
        Toast.makeText(MainActivity.this, "Exit",
                Toast.LENGTH_LONG).show();
        super.onDestroy();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onInit();
        addSong();
        networkUrlVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), MainActivity2.class);
                startActivityForResult(myIntent, 0);
            }
        });
        localUrlVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), MainActivity3.class);
                startActivityForResult(myIntent, 0);
            }
        });
        // prepare for playback
        mediaPlayer = MediaPlayer.create(MainActivity.this, arraySong.get(position).getFile());
        txtTitle.setText(arraySong.get(position).getTitle());

        txtTimeTotal.setText(parseMilisecond(mediaPlayer.getDuration()));
        skSong.setMax(mediaPlayer.getDuration());
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mediaPlayer.getDuration()

                doStart();
            }
        });

        handler = new Handler(){
            public void handleMessage(Message message){
                super.handleMessage(message);
                txtTimeSong.setText(parseMilisecond(message.arg1));
            }
        };

        skSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                skSong.setVisibility(View.VISIBLE);
                int x = (int) Math.ceil(progress / 1000f);

                if (x == 0 && mediaPlayer != null && !mediaPlayer.isPlaying()) {
                 //   clearMediaPlayer();
                    MainActivity.this.skSong.setProgress(0);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            }
        });


        
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(mediaPlayer != null ){
                   if(mediaPlayer.isPlaying()){
                       mediaPlayer.pause();
                       skSong.setProgress(0);
                       txtTimeSong.setText("00:00");
                       btnPlay.setBackgroundResource(R.drawable.ic_baseline_play_circle_outline_24);
                   }
                   } else {
                   System.out.println("stop false");
               }

//                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this, "NOTIFY")
//                        .setSmallIcon(R.drawable.ic_baseline_pause_circle_filled_24)
//                        .setContentTitle("My notification")
//                        .setContentText("Much longer text that cannot fit one line...")
//                        .setStyle(new NotificationCompat.BigTextStyle()
//                                .bigText("Much longer text that cannot fit one line..."))
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
//
//// notificationId is a unique int for each notification that you must define
//                notificationManager.notify(1, mBuilder.build());
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position >=0 && position <4 ){
                    skSong.setProgress(0);
                    position ++;
                    txtTitle.setText(arraySong.get(position).getTitle());
                    mediaPlayer = MediaPlayer.create(MainActivity.this, arraySong.get(position).getFile());
                    txtTitle.setText(arraySong.get(position).getTitle());

                    txtTimeTotal.setText(parseMilisecond(mediaPlayer.getDuration()));
                    skSong.setMax(mediaPlayer.getDuration());
                    btnPlay.setBackgroundResource(R.drawable.ic_baseline_play_circle_outline_24);

                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position >0 && position <=4 ){
                    skSong.setProgress(0);
                    position --;
                    txtTitle.setText(arraySong.get(position).getTitle());
                    mediaPlayer = MediaPlayer.create(MainActivity.this, arraySong.get(position).getFile());
                    txtTitle.setText(arraySong.get(position).getTitle());

                    txtTimeTotal.setText(parseMilisecond(mediaPlayer.getDuration()));
                    skSong.setMax(mediaPlayer.getDuration());
                    btnPlay.setBackgroundResource(R.drawable.ic_baseline_play_circle_outline_24);
                }
            }
        });
    }

    private void doStart() {
        if(mediaPlayer != null) {
            if( mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                btnPlay.setBackgroundResource(R.drawable.ic_baseline_play_circle_outline_24);
            } else {
                mediaPlayer.setVolume(50, 50);
                mediaPlayer.setLooping(false);
                mediaPlayer.start();
                btnPlay.setBackgroundResource(R.drawable.ic_baseline_pause_circle_filled_24);
            }
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    int total = mediaPlayer.getDuration();

                    while (mediaPlayer != null && mediaPlayer.isPlaying() && currentPosition < total) {
                        try {
                            Thread.sleep(1000);
                            currentPosition = mediaPlayer.getCurrentPosition();
                            Message msg = handler.obtainMessage();
                            msg.arg1 = currentPosition;
                            handler.sendMessage(msg);
                        } catch (InterruptedException e) {
                            return;
                        } catch (Exception e) {
                            return;
                        }

                        skSong.setProgress(currentPosition);

                    }
                }
            });
            thread.start();

        }
    }

    private void clearMediaPlayer() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    private void addSong() {

        arraySong = new ArrayList<>();
        arraySong.add(new song("Hai mươi hai", R.raw.hai_muoi_hai));
        arraySong.add(new song("Suy tư", R.raw.suy_tu));
        arraySong.add(new song("Tóc ngắn", R.raw.toc_ngan));
        arraySong.add(new song("Vào hạ", R.raw.vao_ha));
        arraySong.add(new song("Vì mẹ anh bắt chia tay", R.raw.vi_me_anh_bat_chia_tay));
    }

    private void onInit() {
        txtTitle = findViewById(R.id.txtTitle);
        txtTimeSong = findViewById(R.id.txtTimeSong);
        txtTimeTotal = findViewById(R.id.txtTimeTotal);
        skSong = findViewById(R.id.seekbar);
        btnPrev = findViewById(R.id.previousButton);
        btnPlay = findViewById(R.id.pauseButton);
        btnNext = findViewById(R.id.nextButton);
        btnStop = findViewById(R.id.stopButton);
        networkUrlVideo = findViewById(R.id.mybutton_id);
        localUrlVideo = findViewById(R.id.localButton);

    }

  private String parseMilisecond (int milisecond){
      DateFormat obj = new SimpleDateFormat("mm:ss");
      // we create instance of the Date and pass milliseconds to the constructor
      Date res = new Date(milisecond);
      // now we format the res by using SimpleDateFormat
      return obj.format(res);
  }
}