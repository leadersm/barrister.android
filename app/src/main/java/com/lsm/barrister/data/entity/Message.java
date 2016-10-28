package com.lsm.barrister.data.entity;

import java.io.Serializable;

/**
 * Created by lvshimin on 16/5/8.
 * 消息，相关页面及功能并没有详细设计、定义，待定
 */
public class Message implements Serializable{

    String id;
    String icon;
    String title;
    String content;
    String contentId;//假如 是订单类型，那么这个id就是订单id，客户端点击可以跳转到订单详情；
    String type;//系统通知 TYPE_SYSTEM , 订单通知 TYPE_ORDER,学习中心（待定）TYPE_LEARNING;
    String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
