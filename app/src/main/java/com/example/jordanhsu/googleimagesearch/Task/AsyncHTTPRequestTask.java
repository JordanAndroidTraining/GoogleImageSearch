package com.example.jordanhsu.googleimagesearch.Task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.jordanhsu.googleimagesearch.Listener.AsyncHTTPRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by jordanhsu on 8/6/15.
 */
public class AsyncHTTPRequestTask extends AsyncTask<Void, Void, JSONObject> {
    public static final String ASYNC_HTTP_REQUEST_TASK_DEV_TAG = "asyncHTTPRequestTaskDevTag";
    private String mQueryUrl;
    private AsyncHTTPRequestListener mListener;

    public AsyncHTTPRequestTask(String url, AsyncHTTPRequestListener listener) {
        mQueryUrl = url;
        mListener = listener;
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        Log.d(ASYNC_HTTP_REQUEST_TASK_DEV_TAG,"AsyncHTTPRequestTask|doInBackground()");
        StringBuilder content = new StringBuilder();
        try{

            URL url = new URL(mQueryUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;
            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line);
            }
            bufferedReader.close();
        }catch (Exception e){
            Log.d(ASYNC_HTTP_REQUEST_TASK_DEV_TAG, e.toString());
        }

        // convert data into JSONObject and return
        try {
            Log.d(ASYNC_HTTP_REQUEST_TASK_DEV_TAG,content.toString());
            return new JSONObject(content.toString());
        } catch (JSONException e) {
            Log.d(ASYNC_HTTP_REQUEST_TASK_DEV_TAG, e.toString());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        Log.d(ASYNC_HTTP_REQUEST_TASK_DEV_TAG, "AsyncHTTPRequestTask|onPostExecute()");
        mListener.onAsyncHTTPRequestSuccess(jsonObject);
    }
}
