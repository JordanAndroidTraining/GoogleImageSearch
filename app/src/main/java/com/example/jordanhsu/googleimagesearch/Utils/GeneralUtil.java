package com.example.jordanhsu.googleimagesearch.Utils;

import android.widget.ImageView;

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
}
