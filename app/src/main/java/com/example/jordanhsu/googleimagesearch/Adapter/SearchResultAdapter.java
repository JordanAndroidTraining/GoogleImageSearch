package com.example.jordanhsu.googleimagesearch.Adapter;

import android.app.ActionBar;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;


import com.example.jordanhsu.googleimagesearch.DataModel.SearchResultDataModel;
import com.example.jordanhsu.googleimagesearch.R;
import com.example.jordanhsu.googleimagesearch.Task.ImageLoadingTask;
import com.example.jordanhsu.googleimagesearch.Utils.GeneralUtil;

import org.xmlpull.v1.XmlPullParser;

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
    private GeneralUtil mGeneralUtil = new GeneralUtil();
    private int mActivityVerticalMarginPixel;
    private int mSrpMarginPixel;
    private int mSrpColNum;
    private int mScreenWidthPixel;

    public SearchResultAdapter(Context context, int resource, ArrayList<SearchResultDataModel> SRItemList) {
        super(context, resource, SRItemList);
        mContext = context;
        mSRItemList = SRItemList;

        //Get screen && srp column width
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        mActivityVerticalMarginPixel = mContext.getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
        mSrpMarginPixel = mContext.getResources().getDimensionPixelSize(R.dimen.srp_img_margin);
        mSrpColNum = mContext.getResources().getInteger(R.integer.srp_col_num);
        mScreenWidthPixel = displayMetrics.widthPixels;
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

        // calculate scale value
        int scaledImgWidth = (mScreenWidthPixel - 2*(mActivityVerticalMarginPixel) - (mSrpColNum + 1)*mSrpMarginPixel)/mSrpColNum;
        float scale = (float) scaledImgWidth / Integer.parseInt(mSRItemList.get(position).getImgWidth());

        // set image height to prevent load more error && scroll error
        viewHolder.resultImg.getLayoutParams().height = (int)(Integer.parseInt(mSRItemList.get(position).getImgHeight())*scale);

        mGeneralUtil.doAsyncImageLoadingTask(mSRItemList.get(position).getTbImgUrl(), viewHolder.resultImg);
        viewHolder.resultImg.setOnClickListener((View.OnClickListener) mContext);
        return convertView;
    }
}
