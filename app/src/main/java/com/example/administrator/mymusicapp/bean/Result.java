package com.example.administrator.mymusicapp.bean;



import com.example.administrator.mymusicapp.loopj.android.image.SmartImageView;

import java.util.List;

/**
 * Created by Jugeny on 2017/6/8.
 */

public class Result {
    private String picurl;
    private String des;
    private String createdAt;
    private String updatedAt;
    private String objectId;
    private SmartImageView smartImageView;

    public Result(String picurl, String des, String createdAt, String updatedAt, String objectId) {
        this.picurl = picurl;
        this.des = des;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.objectId = objectId;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public SmartImageView getSmartImageView() {
        return smartImageView;
    }

    public void setSmartImageView(SmartImageView smartImageView) {
        this.smartImageView = smartImageView;
    }
}
