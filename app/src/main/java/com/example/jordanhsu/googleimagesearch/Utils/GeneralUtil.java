package com.example.jordanhsu.googleimagesearch.Utils;

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
}
