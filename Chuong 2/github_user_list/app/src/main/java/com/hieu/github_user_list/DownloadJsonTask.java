package com.hieu.github_user_list;


import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hieu.github_user_list.models.GithubUserModel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

// A task with String input parameter, and returns the result as String.
public class DownloadJsonTask
        // AsyncTask<Params, Progress, Result>
        extends AsyncTask<String, Void, String> {
    CustomAdapter customAdapter;
    ArrayList<GithubUserModel> githubUserModels;

    public DownloadJsonTask(CustomAdapter customAdapter, ArrayList<GithubUserModel> githubUserModels)  {
        this.customAdapter = customAdapter;
        this.githubUserModels=githubUserModels;
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
        Log.i("DownloadJsonTask", "onPostExecute: " + result);
        ArrayList<GithubUserModel> userList= decodeStringToListWithGson(result);
        Log.i("DownloadJsonTask", "userListuserList: " + userList);
        githubUserModels.addAll(userList);
        customAdapter.getItemCount();
        customAdapter.notifyDataSetChanged();
        if(result  != null){
        } else{
            Log.e("MyMessage", "Failed to fetch data!");
        }
    }

    private ArrayList<GithubUserModel> decodeStringToListWithGson(String jsonString) {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<GithubUserModel>>(){}.getType();
        ArrayList<GithubUserModel> githubUserModels = gson.fromJson(jsonString, listType);
        return githubUserModels;
    }
}
