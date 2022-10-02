package com.hieuit.record_video;

import static com.hieuit.record_video.Utils.getOutputMediaFile;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.icu.text.SimpleDateFormat;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final String TAG = "CameraDemo";

    private Camera mCamera;
    private CameraPreview mPreview;
    Button btnRecord;
    Button btnStop;
    File outputFile;
    MediaRecorder mediaRecorder=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkCameraHardware(this);
        mCamera = getCameraInstance();
        // Create our Preview view and set it as the content of our activity .
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview =  findViewById(R.id.camera_preview);

        preview.addView(mPreview);
        Utils.setCameraDisplayOrientation(this,0,mCamera);
        btnRecord = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);

        btnRecord.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doRecordingVideo();
                    }
                }
        );
        btnStop.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stopRecordingVideo();
                    }
                }
        );
    }

    public void doRecordingVideo(){
        if (mediaRecorder == null){
            mediaRecorder = new MediaRecorder();
        }
        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
        mediaRecorder.setVideoSize(640, 480);
        mediaRecorder.setVideoFrameRate(30);
        mediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                outputFile= Utils.getOutputMediaFile(Utils.MEDIA_TYPE_VIDEO);
                mediaRecorder.setOutputFile(outputFile);
            }
        }
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopRecordingVideo(){
        if (mediaRecorder != null){
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            mCamera.lock();
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
//        Uri uri = Uri.fromFile(outputFile);
        Uri uri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider",outputFile);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        intent.setDataAndType(uri, "video/*");
        startActivity(intent);
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance() {

        Camera c = null;/*from  ww  w  .j  av a 2  s. c  o m*/
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.e("ch.getCI", "Failed to get camera!", e);
        }
        return c; // returns null if camera is unavailable
    }

    public final void takePicture(Camera.ShutterCallback shutter, Camera.PictureCallback raw, Camera.PictureCallback jpeg) {
        if (mCamera != null) {
            mCamera.takePicture(shutter, raw, jpeg);
        }
    }

    /** Create a file Uri for saving an image or video */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }



    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);

            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

}