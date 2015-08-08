package com.example.jordanhsu.googleimagesearch.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jordanhsu.googleimagesearch.DataModel.SearchResultDataModel;
import com.example.jordanhsu.googleimagesearch.R;
import com.example.jordanhsu.googleimagesearch.Utils.GeneralUtil;

/**
 * Created by jordanhsu on 8/8/15.
 */
public class FullScreenImageFragment extends Fragment {

    private ImageView mImageContainer;
    private TextView mImageTitle;
    private TextView mImageSize;
    private TextView mImageContent;
    private GeneralUtil mGeneralUtil = new GeneralUtil();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.full_screen_image_fragment, null);

        //get element reference
        mImageContainer = (ImageView) view.findViewById(R.id.imageContainerIv);
        mImageTitle = (TextView) view.findViewById(R.id.imageTitleTv);
        mImageContent = (TextView) view.findViewById(R.id.imageContentTv);
        mImageSize = (TextView) view.findViewById(R.id.imageSizeInfoTv);

        Bundle args = getArguments();
        SearchResultDataModel data = (SearchResultDataModel) args
                .getSerializable("data");
        setFullScreenViewContent(data);
        return view;
    }

    @Override
    public void onDetach() {

    }

    public void setFullScreenViewContent(SearchResultDataModel model){
        mGeneralUtil.doAsyncImageLoadingTask(model.getOriginalImgUrl() , mImageContainer);

    }
}
