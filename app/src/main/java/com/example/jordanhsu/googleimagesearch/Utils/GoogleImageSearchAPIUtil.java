package com.example.jordanhsu.googleimagesearch.Utils;

import android.util.Log;

import com.example.jordanhsu.googleimagesearch.DataModel.SearchResultDataModel;
import com.example.jordanhsu.googleimagesearch.Listener.AsyncHTTPRequestListener;
import com.example.jordanhsu.googleimagesearch.Task.AsyncHTTPRequestTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jordanhsu on 8/6/15.
 */
public class GoogleImageSearchAPIUtil {
    public static final String GOOGLE_IMAGE_SEARCH_API_UTIL_DEV_TAG = "GoogleImageSearchAPIUtilDevTag";
    private static String mAPIUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8";
    private GeneralUtil mGeneralUtil = new GeneralUtil();
    private AsyncHTTPRequestListener mListner;


    public GoogleImageSearchAPIUtil(AsyncHTTPRequestListener listener) {
        mListner = listener;
    }

    public void getResultByKeyword(String keyword){
        try {
            String queryUrl = mAPIUrl + "&q=" + URLEncoder.encode(keyword, "UTF-8") ;
            Log.d(GOOGLE_IMAGE_SEARCH_API_UTIL_DEV_TAG,"queryUrl: "+ queryUrl);
            AsyncHTTPRequestTask mAsyncHttpReqTask = new AsyncHTTPRequestTask(queryUrl, mListner);
            mAsyncHttpReqTask.execute();
        } catch (UnsupportedEncodingException e){
            Log.d(GOOGLE_IMAGE_SEARCH_API_UTIL_DEV_TAG, "UnsupportedEncodingException| " +e.toString());
            e.printStackTrace();
        }
    }

    public void getResultWithParameter(String keyword, HashMap<String,String> param){
        Log.d(GOOGLE_IMAGE_SEARCH_API_UTIL_DEV_TAG,"getResultWithParameter| keyword: " + keyword + " param: " + param.toString());
        String queryUrl = mAPIUrl + "&q=" + keyword;
        for(String key : param.keySet()) {
            Log.d(GOOGLE_IMAGE_SEARCH_API_UTIL_DEV_TAG, "getResultWithParameter| key: " + key + " value: " + param.get(key));
            queryUrl += String.format("&%s=%s",key,param.get(key));
        }
        Log.d(GOOGLE_IMAGE_SEARCH_API_UTIL_DEV_TAG,"queryUrl: "+ queryUrl);
    }

    public ArrayList<SearchResultDataModel> processAPIReturnJSON(JSONObject jsonObject){

        try {
            ArrayList returnArrList = new ArrayList();
            if(mGeneralUtil.checkJSONObjectCol("responseData",jsonObject)){
                if(mGeneralUtil.checkJSONObjectCol("results",jsonObject.getJSONObject("responseData"))){
                    JSONArray resultList = jsonObject.getJSONObject("responseData").getJSONArray("results");
                    for(int i=0; i < resultList.length(); i++){
                        JSONObject row = (JSONObject) resultList.get(i);
                        SearchResultDataModel srRow = new SearchResultDataModel();

                        // init value
                        String tbImgUrl = "";

                        // process JSON
                        if(mGeneralUtil.checkJSONObjectCol("tbUrl",row)){
                            tbImgUrl = row.getString("tbUrl");
                        }
                        Log.d(GOOGLE_IMAGE_SEARCH_API_UTIL_DEV_TAG,"processAPIReturnJSON()|tbUrl: " + tbImgUrl);

                        // set value to data model
                        srRow.setTbImgUrl(tbImgUrl);

                        // append to return array list
                        returnArrList.add(srRow);
                    }
                }
            }
            return returnArrList;
        }catch (JSONException e){
            Log.d(GOOGLE_IMAGE_SEARCH_API_UTIL_DEV_TAG, "JSONException| " +e.toString());
            e.printStackTrace();
            return  null;
        }


    }


}
