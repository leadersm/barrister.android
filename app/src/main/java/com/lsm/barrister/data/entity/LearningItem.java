package com.lsm.barrister.data.entity;

import java.io.Serializable;

/**
 * Created by lvshimin on 16/5/8.
 * 学习中心列表Item
 */
public class LearningItem implements Serializable{

    String id;
    String tag;//标签，微电影，……
    String title;//标题
    String thumb;//缩略图
    String date;//日期
    String url;//跳转链接地址


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
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
}
