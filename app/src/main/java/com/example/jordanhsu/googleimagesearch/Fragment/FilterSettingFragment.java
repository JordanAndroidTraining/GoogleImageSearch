package com.example.jordanhsu.googleimagesearch.Fragment;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.jordanhsu.googleimagesearch.Listener.FilterSettingListener;
import com.example.jordanhsu.googleimagesearch.MainActivity;
import com.example.jordanhsu.googleimagesearch.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import static android.R.layout.browser_link_context_header;
import static android.R.layout.simple_dropdown_item_1line;
import static android.R.layout.simple_spinner_item;


/**
 * Created by jordanhsu on 8/8/15.
 */
public class FilterSettingFragment extends DialogFragment implements View.OnClickListener {
    public static final String FILTER_SETTING_FRAGMENT_DEV_TAG = "filterSettingFragmentDevTag";
    public static final String ANY = "any";
    private Context mSelf;
    private Spinner mColorFilterSp;
    private Spinner mSizeFilterSp;
    private Spinner mTypeFilterSp;
    private Spinner mSiteFilterSp;
    private Button mCancelBtn;
    private Button mApplyBtn;
    private static final String[] FILTER_COLOR_OPTIONS = new String[]{ANY,"purple","black","blue","brown","gray","green","orange","pink","teal","yellow","white","red"};
    private static final String[] FILTER_SIZE_OPTIONS = new String[]{ANY,"icon","small", "medium", "large", "xlarge", "xxlarge"};
    private static final String[] FILTER_TYPE_OPTIONS = new String[]{ANY,"faces", "photo", "clipart", "lineart"};
    private static final String[] FILTER_SITE_OPTIONS = new String[]{ANY,"espn.com","photobucket.com"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.filter_setting_fragment, container);
        //commentLv = (ListView) view.findViewById(R.id.allCommentListView);
        mSelf = view.getContext();

        getDialog().setCanceledOnTouchOutside(true);

        //get element reference
        mColorFilterSp = (Spinner) view.findViewById(R.id.colorFilterSp);
        mSizeFilterSp = (Spinner) view.findViewById(R.id.sizeFilterSp);
        mTypeFilterSp = (Spinner) view.findViewById(R.id.typeFilterSp);
        mSiteFilterSp = (Spinner) view.findViewById(R.id.siteFilterSp);
        mApplyBtn = (Button) view.findViewById(R.id.applyBtn);
        mCancelBtn = (Button) view.findViewById(R.id.cancelBtn);

        //init dropdown
        dropDownInit(mColorFilterSp,FILTER_COLOR_OPTIONS);
        dropDownInit(mSizeFilterSp, FILTER_SIZE_OPTIONS);
        dropDownInit(mTypeFilterSp, FILTER_TYPE_OPTIONS);
        dropDownInit(mSiteFilterSp, FILTER_SITE_OPTIONS);



//        getActivity().
        // Bing Event
        mApplyBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);

        return view;
    }

    private void dropDownInit(Spinner sp, String[] options){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mSelf, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.applyBtn:
                Log.d(FILTER_SETTING_FRAGMENT_DEV_TAG, "apply Btn clicked!");
                Log.d(FILTER_SETTING_FRAGMENT_DEV_TAG,"color: " + mColorFilterSp.getSelectedItem() + " type: " + mTypeFilterSp.getSelectedItem());
                FilterSettingListener activity = (FilterSettingListener) getActivity();
                activity.onFilterSettingFinished(processFilterSetting());
                this.dismiss();
                break;
            case R.id.cancelBtn:
                Log.d(FILTER_SETTING_FRAGMENT_DEV_TAG, "cancel Btn clicked!");
                this.dismiss();
                break;
        }
    }

    private HashMap<String,String> processFilterSetting(){
        HashMap<String,String> queryParam = new HashMap<String,String>();

        String selectedColor = (String) mColorFilterSp.getSelectedItem();
        String selectedSize = (String) mSizeFilterSp.getSelectedItem();
        String selectedType = (String) mTypeFilterSp.getSelectedItem();
        String selectedSite = (String) mSiteFilterSp.getSelectedItem();

        if(!selectedColor.equals(ANY)){
            queryParam.put("imgcolor",selectedColor);
        }
        if(!selectedSize.equals(ANY)){
            queryParam.put("imgsz",selectedSize);
        }
        if(!selectedType.equals(ANY)){
            queryParam.put("imgtype",selectedType);
        }
        if(!selectedSite.equals(ANY)){
            queryParam.put("as_sitesearch",selectedSite);
        }

        return queryParam;
    }
}
