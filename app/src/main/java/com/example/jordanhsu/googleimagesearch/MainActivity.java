package com.example.jordanhsu.googleimagesearch;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.example.jordanhsu.googleimagesearch.Adapter.SearchResultAdapter;
import com.example.jordanhsu.googleimagesearch.DataModel.SearchResultDataModel;
import com.example.jordanhsu.googleimagesearch.Listener.AsyncHTTPRequestListener;
import com.example.jordanhsu.googleimagesearch.Task.AsyncHTTPRequestTask;
import com.example.jordanhsu.googleimagesearch.Utils.GoogleImageSearchAPIUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity implements AsyncHTTPRequestListener, View.OnClickListener {
    public static final String MAIN_ACTIVITY_DEV_TAG = "mainActivityDevTag";

    private GoogleImageSearchAPIUtil mGISUtil = new GoogleImageSearchAPIUtil(this);
//    private AsyncHTTPRequestTask mAsyncHttpReqTask = new AsyncHTTPRequestTask()
    private Button mSearchBtn;
    private EditText mSearchInput;
    private ArrayList<SearchResultDataModel> mSRItemList;
    private StaggeredGridView mMainContainerGridView;
    private SearchResultAdapter mSRAdapter;
    private Activity mSelf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSelf = this;

        // Get laytout element reference
        mSearchBtn = (Button) findViewById(R.id.searchButton);
        mSearchInput = (EditText) findViewById(R.id.searchInputEt);
        mMainContainerGridView = (StaggeredGridView) findViewById(R.id.searchResultGv);


        //Bind Click Event
        mSearchBtn.setOnClickListener(this);

//        mGISUtil.getResultByKeyword("macbook");

        HashMap<String,String> queryParam = new HashMap<String,String>();
        queryParam.put("a","bbb");
        queryParam.put("c","xxxbbb");
        queryParam.put("d", "gggla");
        Log.d(MAIN_ACTIVITY_DEV_TAG, queryParam.toString());
        mGISUtil.getResultWithParameter("macbook", queryParam);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;


//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//        searchView.setOnQueryTextListener(this);

//        return true;

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

    @Override
    public void onAsyncHTTPRequestSuccess(JSONObject jsonObject) {
        if(jsonObject != null){

            Toast.makeText(this,"onAsyncHTTPRequestSuccess", Toast.LENGTH_SHORT).show();
            Log.d(MAIN_ACTIVITY_DEV_TAG, "MainActivity|onAsyncHTTPRequestSuccess: " + jsonObject.toString());

            mSRItemList = mGISUtil.processAPIReturnJSON(jsonObject);
            Log.d(MAIN_ACTIVITY_DEV_TAG, "MainActivity|onAsyncHTTPRequestSuccess|mSRItemList: " + mSRItemList.toString());
            mSRAdapter = new SearchResultAdapter(mSelf,0,mSRItemList);
            Log.d(MAIN_ACTIVITY_DEV_TAG, "MainActivity|onAsyncHTTPRequestSuccess|mSRAdapter: " + mSRAdapter.toString());

            mMainContainerGridView.setAdapter(mSRAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.searchButton:
                String keyword = String.valueOf(mSearchInput.getText());
                mGISUtil.getResultByKeyword(keyword);
                Log.d(MAIN_ACTIVITY_DEV_TAG,"searchBtn Clicked!");
                break;
        }
    }
}
