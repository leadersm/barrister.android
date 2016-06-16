package com.lsm.barrister.data.entity;

import java.io.Serializable;

/**
 * Created by lvshimin on 16/5/13.
 * 轮播广告
 */
public class Ad implements Serializable{

    String title;//标题
    String date;//日期
    String url;//跳转网址
    String image;//图片地址

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
