package com.example.jordanhsu.googleimagesearch;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jordanhsu.googleimagesearch.DataModel.SearchResultDataModel;
import com.example.jordanhsu.googleimagesearch.Utils.GeneralUtil;


public class FullScreenImgActivity extends ActionBarActivity {

    public static final String FULL_SCREEN_IMG_ACTIVITY_DEV_TAG = "fullScreenImgActivityDevTag";
    private GeneralUtil mGeneralUtil = new GeneralUtil();
    private SearchResultDataModel mRenderData;
    private ImageView mImgContainer;
    private TextView mImageTitle;
    private TextView mImageSize;
    private TextView mImageContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide action bar
        getSupportActionBar().hide();

        //get intent
        Intent intent = getIntent();
        mRenderData = (SearchResultDataModel) intent.getSerializableExtra("data");
        Log.d(FULL_SCREEN_IMG_ACTIVITY_DEV_TAG, mRenderData.toString());

        setContentView(R.layout.activity_full_screen_img);

        //get element reference
        mImgContainer = (ImageView) findViewById(R.id.imageContainerIv);
        mImageTitle = (TextView) findViewById(R.id.imageTitleTv);
        mImageContent = (TextView) findViewById(R.id.imageContentTv);
        mImageSize = (TextView) findViewById(R.id.imageSizeInfoTv);

        //render it!
        renderFullScreenView();
    }

    private void renderFullScreenView(){

        mGeneralUtil.doAsyncImageLoadingTask(mRenderData.getOriginalImgUrl(),mImgContainer);
        mImageContent.setText(mRenderData.getImgContent());
        mImageTitle.setText(mRenderData.getImgTitle());
        mImageSize.setText(mRenderData.getImgWidth() + "x" + mRenderData.getImgHeight());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_full_screen_img, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
