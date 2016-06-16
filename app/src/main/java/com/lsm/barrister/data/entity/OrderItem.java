package com.lsm.barrister.data.entity;

import java.io.Serializable;

/**
 * Created by lvshimin on 16/5/8.
 * 订单列表Item
 */
public class OrderItem implements Serializable{

    public static final String TYPE_IM = "IM";//即时
    public static final String TYPE_APPOINTMENT = "APPOINTMENT";//预约

    String id;
    String type;//订单类型：即时、预约
    String userIcon;//用户头像
    String nickname;//用户昵称
    String date;//日期
    String status;//订单状态
    String caseType;//案件类型：财产纠纷，离婚，……
    String clientPhone;//用户手机

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }
}
