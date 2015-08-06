package com.example.jordanhsu.googleimagesearch.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.jordanhsu.googleimagesearch.DataModel.SearchResultDataModel;
import com.example.jordanhsu.googleimagesearch.R;
import com.example.jordanhsu.googleimagesearch.Task.ImageLoadingTask;

import java.util.ArrayList;

import static com.example.jordanhsu.googleimagesearch.R.color.black;
import static com.example.jordanhsu.googleimagesearch.R.color.grey;

/**
 * Created by jordanhsu on 8/6/15.
 */
public class SearchResultAdapter extends ArrayAdapter<SearchResultDataModel> {
    public static final String SEARCH_RESULT_ADAPTER_DEV_TAG = "searchResultAdapterDevTag";
    private Context mContext;
    private ArrayList<SearchResultDataModel> mSRItemList;
    public SearchResultAdapter(Context context, int resource, ArrayList<SearchResultDataModel> SRItemList) {
        super(context, resource, SRItemList);
        mContext = context;
        mSRItemList = SRItemList;
    }

    private static class ViewHolder{
        ImageView resultImg;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            Log.d(SEARCH_RESULT_ADAPTER_DEV_TAG, "Convert View is null");
            convertView = LayoutInflater.from(mContext).inflate(R.layout.result_item,parent,false);
            viewHolder = new ViewHolder();

            // get element reference
            viewHolder.resultImg = (ImageView) convertView.findViewById(R.id.searchResultImageIv);

            // save to view tag
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // setting layout value
        viewHolder.resultImg.setBackgroundColor(black);
        doAsyncImageLoadingTask(mSRItemList.get(position).getTbImgUrl(),viewHolder.resultImg);

        return convertView;
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
