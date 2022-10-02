package com.hieu.jsonparser;


import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// A task with String input parameter, and returns the result as String.
public class DownloadJsonTask
        // AsyncTask<Params, Progress, Result>
        extends AsyncTask<String, Void, String> {

    private final TextView textView;

    public DownloadJsonTask(TextView textView)  {
        this.textView= textView;
    }

    @Override
    protected String doInBackground(String... params) {
        String textUrl = params[0];

        InputStream in = null;
        BufferedReader br= null;
        try {
            URL url = new URL(textUrl);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            int resCode = httpConn.getResponseCode();

            if (resCode == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
                br= new BufferedReader(new InputStreamReader(in));

                StringBuilder sb= new StringBuilder();
                String s= null;
                while((s= br.readLine())!= null) {
                    sb.append(s);
                    sb.append("\n");
                }
                return sb.toString();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(br);
        }
        return null;
    }

    // When the task is completed, this method will be called
    // Download complete. Lets update UI
    @Override
    protected void onPostExecute(String result) {
        if(result  != null){
            this.textView.setText(result);
        } else{
            Log.e("MyMessage", "Failed to fetch data!");
        }
    }
}
