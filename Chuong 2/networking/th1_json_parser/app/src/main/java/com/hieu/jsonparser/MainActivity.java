package com.hieu.jsonparser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.hieu.jsonparser.models.Website;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;
    private Button buttonImg;
    private Button buttonJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.imageView = (ImageView) this.findViewById(R.id.imageView);
        this.textView = (TextView) this.findViewById(R.id.textView);
        this.buttonImg = (Button) this.findViewById(R.id.button_img);
        this.buttonJson = (Button) this.findViewById(R.id.button_json);

        this.buttonImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadAndShowImage(v);
            }
        });
        this.buttonJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadAndShowJson(v);
            }
        });
    }


    private boolean checkInternetConnection() {
        // Get Connectivity Manager
        ConnectivityManager connManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Details about the currently active default data network
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            Toast.makeText(this, "No default network is currently active", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!networkInfo.isConnected()) {
            Toast.makeText(this, "Network is not connected", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!networkInfo.isAvailable()) {
            Toast.makeText(this, "Network not available", Toast.LENGTH_LONG).show();
            return false;
        }
        Toast.makeText(this, "Network OK", Toast.LENGTH_LONG).show();
        return true;
    }

    // When user click on the "Download Image".
    public void downloadAndShowImage(View view) {
        boolean networkOK = this.checkInternetConnection();
        if (!networkOK) {
            return;
        }
        String imageUrl = "https://media.techmaster.vn/api/static/brbgh4451coepbqoch60/Lf-bvYrA";

        // Create a task to download and display image.
        DownloadImageTask task = new DownloadImageTask(this.imageView);

        // Execute task (Pass imageUrl).
        task.execute(imageUrl);
    }

    // When user click on the "Download Json".
    public void downloadAndShowJson(View view) {
        boolean networkOK = this.checkInternetConnection();
        if (!networkOK) {
            return;
        }
        String jsonUrl = "https://api.github.com/users";

        // Create a task to download and display json content.
        DownloadJsonTask task = new DownloadJsonTask(this.textView);

        // Execute task (Pass jsonUrl).
        task.execute(jsonUrl);
    }


}