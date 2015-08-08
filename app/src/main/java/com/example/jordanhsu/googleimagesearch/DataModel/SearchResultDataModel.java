package com.example.jordanhsu.googleimagesearch.DataModel;

import java.io.Serializable;

/**
 * Created by jordanhsu on 8/6/15.
 */
public class SearchResultDataModel implements Serializable {
    private String tbImgUrl;
    private String originalImgUrl;
    private String imgWidth;
    private String imgHeight;
    private String imgTitle;
    private String imgContent;

    public String getOriginalImgUrl() {
        return originalImgUrl;
    }

    public void setOriginalImgUrl(String originalImgUrl) {
        this.originalImgUrl = originalImgUrl;
    }

    public String getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(String imgWidth) {
        this.imgWidth = imgWidth;
    }

    public String getImgHeight() {
        return imgHeight;
    }

    public void setImgHeight(String imgHeight) {
        this.imgHeight = imgHeight;
    }

    public String getImgTitle() {
        return imgTitle;
    }

    public void setImgTitle(String imgTitle) {
        this.imgTitle = imgTitle;
    }

    public String getImgContent() {
        return imgContent;
    }

    public void setImgContent(String imgContent) {
        this.imgContent = imgContent;
    }

    public String getTbImgUrl() {
        return tbImgUrl;
    }

    public void setTbImgUrl(String tbImgUrl) {
        this.tbImgUrl = tbImgUrl;
    }

}
