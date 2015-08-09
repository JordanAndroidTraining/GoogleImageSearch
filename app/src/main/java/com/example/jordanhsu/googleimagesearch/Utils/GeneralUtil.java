package com.example.jordanhsu.googleimagesearch.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jordanhsu.googleimagesearch.Task.ImageLoadingTask;

import org.json.JSONObject;

/**
 * Created by jordanhsu on 8/7/15.
 */
public class GeneralUtil {
    public boolean checkJSONObjectCol(String key, JSONObject obj){
        if (obj.has(key) && !obj.isNull(key)){
            return true;
        }
        else {
            return false;
        }
    }

    public void doAsyncImageLoadingTask(String url, ImageView iv){
        // clear image resource
        iv.setImageResource(0);

        // cancel previous task
        ImageLoadingTask prevTask = (ImageLoadingTask) iv.getTag();
        if(prevTask != null) {
            prevTask.cancel(true);
        }

        // create new task && set to ImageView tag
        ImageLoadingTask newTask = new ImageLoadingTask(url,iv);
        iv.setTag(newTask);

        // execute asyncTask
        newTask.execute();
    }

    public Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean status = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        if(status == false){
            Toast.makeText(context,"Network Connection Error!",Toast.LENGTH_SHORT).show();
        }
        return status;
    }
}
