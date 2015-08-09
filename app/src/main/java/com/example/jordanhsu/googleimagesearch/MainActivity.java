package com.example.jordanhsu.googleimagesearch;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.SearchView;


import com.etsy.android.grid.StaggeredGridView;
import com.example.jordanhsu.googleimagesearch.Adapter.SearchResultAdapter;
import com.example.jordanhsu.googleimagesearch.DataModel.SearchResultDataModel;
import com.example.jordanhsu.googleimagesearch.Fragment.FilterSettingFragment;
import com.example.jordanhsu.googleimagesearch.Listener.AsyncHTTPRequestListener;
import com.example.jordanhsu.googleimagesearch.Listener.FilterSettingListener;
import com.example.jordanhsu.googleimagesearch.Utils.GeneralUtil;
import com.example.jordanhsu.googleimagesearch.Utils.GoogleImageSearchAPIUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends Activity implements AsyncHTTPRequestListener, View.OnClickListener, AbsListView.OnScrollListener, FilterSettingListener {
    public static final String MAIN_ACTIVITY_DEV_TAG = "mainActivityDevTag";
    public static final int DEFAULT_SEARCH_RESULT_PAGES = 3;
    public static final int TOTAL_SEARCH_RESULT_PAGES = 7;
    public static final String FILTER_SETTING_FRAGMENT_TAG = "filterSettingFragmentTag";

    private GoogleImageSearchAPIUtil mGISUtil = new GoogleImageSearchAPIUtil(this);
    private ArrayList<SearchResultDataModel> mSRItemList;
    private StaggeredGridView mMainContainerGridView;
    private SearchResultAdapter mSRAdapter;
    private Activity mSelf;
    private int mLoadedPage;
    private String mKeyword = null;
    private boolean mIsLoading = true;
    private SearchView mSearchView;
    private FilterSettingFragment mFSFragment;
    private GeneralUtil mGeneralUtil = new GeneralUtil();

    private HashMap<String,String> mFilterQueryParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initilize && Get laytout element reference
        mSelf = this;


        mMainContainerGridView = (StaggeredGridView) findViewById(R.id.searchResultGv);
        mFSFragment = new FilterSettingFragment();

        // set search result adapter
        mSRItemList = new ArrayList<>();
        mSRAdapter = new SearchResultAdapter(mSelf,0,mSRItemList);
        mMainContainerGridView.setAdapter(mSRAdapter);

        // check network connection
        mGeneralUtil.isNetworkAvailable(mSelf);

        mMainContainerGridView.setOnScrollListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_searchGI);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if(!mGeneralUtil.isNetworkAvailable(mSelf)){
                        return false;
                    }
                    // perform query here
                    mKeyword = query;
                    renderSRP();
                    Log.d(MAIN_ACTIVITY_DEV_TAG,"onQueryTextSubmit()");
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.d(MAIN_ACTIVITY_DEV_TAG,"onQueryTextChange()");
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FragmentManager fm = getFragmentManager();
            mFSFragment.show(fm, FILTER_SETTING_FRAGMENT_TAG);
            Log.d(MAIN_ACTIVITY_DEV_TAG, "filter item clicked!");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAsyncHTTPRequestSuccess(JSONObject jsonObject) {
        if(jsonObject != null){
            Log.d(MAIN_ACTIVITY_DEV_TAG, "MainActivity|onAsyncHTTPRequestSuccess: " + jsonObject.toString());
            mSRItemList.addAll(mGISUtil.processAPIReturnJSON(jsonObject));
            mIsLoading = false;
            mSRAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        if(!mGeneralUtil.isNetworkAvailable(mSelf)){
            return;
        }
        switch (v.getId()){
//            case R.id.searchButton:
//                mKeyword = String.valueOf(mSearchInput.getText());
//                renderSRP();
//                Log.d(MAIN_ACTIVITY_DEV_TAG,"searchBtn Clicked!");
//                break;
            case R.id.searchResultImageIv:
                int clickedPosition = mMainContainerGridView.getPositionForView(v);
                Intent intent = new Intent(this,FullScreenImgActivity.class);
                intent.putExtra("data", mSRItemList.get(clickedPosition));
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // nothing to do!
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(!mGeneralUtil.isNetworkAvailable(mSelf)){
            return;
        }
        Log.d(MAIN_ACTIVITY_DEV_TAG,"onScroll()| firstVisibleItem: " + firstVisibleItem + " | visibleItemCount: " + visibleItemCount + " |totalItemCount: "+ totalItemCount);
        if(firstVisibleItem + visibleItemCount >= totalItemCount && mLoadedPage < TOTAL_SEARCH_RESULT_PAGES && !mIsLoading && mKeyword != null) {
            mLoadedPage++;
            mGISUtil.getResultWithParameter(mKeyword, mFilterQueryParam, mLoadedPage);
            Log.d(MAIN_ACTIVITY_DEV_TAG,"onScroll()| REACH BOTTOM | mLoadedPage: " + mLoadedPage);
            mIsLoading = true;
        }
    }


    @Override
    public void onFilterSettingFinished(HashMap<String, String> queryParam) {
        mFilterQueryParam = queryParam;
        renderSRP();
        Log.d(MAIN_ACTIVITY_DEV_TAG,"onFilterSettingFinished|queryParam" + queryParam.toString());
    }

    private void renderSRP(){
        // clear prev search result
        mSRItemList.clear();
        // load 3 pages by default
        for(int i = 0; i < DEFAULT_SEARCH_RESULT_PAGES ; i++){
            mLoadedPage = i;
            mGISUtil.getResultWithParameter(mKeyword, mFilterQueryParam, mLoadedPage);
            mIsLoading = true;
        }
    }

}
