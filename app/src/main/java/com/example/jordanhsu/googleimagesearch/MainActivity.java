package com.example.jordanhsu.googleimagesearch;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.SearchView;


import com.etsy.android.grid.StaggeredGridView;
import com.example.jordanhsu.googleimagesearch.Adapter.SearchResultAdapter;
import com.example.jordanhsu.googleimagesearch.DataModel.SearchResultDataModel;
import com.example.jordanhsu.googleimagesearch.Fragment.FilterSettingFragment;
import com.example.jordanhsu.googleimagesearch.Listener.AsyncHTTPRequestListener;
import com.example.jordanhsu.googleimagesearch.Listener.FilterSettingListener;
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
//    private AsyncHTTPRequestTask mAsyncHttpReqTask = new AsyncHTTPRequestTask()
    private Button mSearchBtn;
    private EditText mSearchInput;
    private ArrayList<SearchResultDataModel> mSRItemList;
    private StaggeredGridView mMainContainerGridView;
    private SearchResultAdapter mSRAdapter;
    private Activity mSelf;
    private int mLoadedPage;
    private String mKeyword = null;
    private boolean mIsLoading = true;
    private SearchView mSearchView;
    private FilterSettingFragment mFSFragment;
    private HashMap<String,String> mFilterQueryParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initilize && Get laytout element reference
        mSelf = this;
        mSearchBtn = (Button) findViewById(R.id.searchButton);
        mSearchInput = (EditText) findViewById(R.id.searchInputEt);
        mMainContainerGridView = (StaggeredGridView) findViewById(R.id.searchResultGv);
        mFSFragment = new FilterSettingFragment();;


        // set search result adapter
        mSRItemList = new ArrayList<>();
        mSRAdapter = new SearchResultAdapter(mSelf,0,mSRItemList);
        mMainContainerGridView.setAdapter(mSRAdapter);

        //Bind Event
        mSearchBtn.setOnClickListener(this);

       // mMainContainerGridView.setOnScrollListener(this);

//        mGISUtil.getResultByKeyword("macbook");

//        HashMap<String,String> queryParam = new HashMap<String,String>();
//        queryParam.put("a","bbb");
//        queryParam.put("c","xxxbbb");
//        queryParam.put("d", "gggla");
//        Log.d(MAIN_ACTIVITY_DEV_TAG, queryParam.toString());
//        mGISUtil.getResultWithParameter("macbook", queryParam);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;



        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_searchGI);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//        SearchView searchView = (SearchView) menu.findItem(R.id.action_searchGI);
//        SearchView searchView = (SearchView) findViewById(R.id.action_searchGI);
        Log.d(MAIN_ACTIVITY_DEV_TAG, "searchView: " + searchView);

        if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // perform query here
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
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
        switch (v.getId()){
            case R.id.searchButton:
                mKeyword = String.valueOf(mSearchInput.getText());
                renderSRP();


                Log.d(MAIN_ACTIVITY_DEV_TAG,"searchBtn Clicked!");
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        Log.d(MAIN_ACTIVITY_DEV_TAG,"onScrollStateChanged()|scrollState" + scrollState);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.d(MAIN_ACTIVITY_DEV_TAG,"onScroll() called!");
        if(firstVisibleItem + visibleItemCount >= totalItemCount && mLoadedPage < TOTAL_SEARCH_RESULT_PAGES && !mIsLoading && mKeyword != null ){
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
