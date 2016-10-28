package com.lsm.barrister.data.entity;

import java.io.Serializable;

/**
 * Created by lvshimin on 16/5/24.
 */
public class IncomeDetailItem implements Serializable{

    public static final String TYPE_ORDER = "income.type.order";//订单
    public static final String TYPE_GET_MONEY = "income.type.getmoney";//提现
    public static final String TYPE_REWARD = "income.type.reward";//打赏
    public static final String TYPE_BUY_CASE = "income.type.buycase";//购买案源

    String id;
    String serialNum;//流水号
    String money;//金额
    String orderId;//订单id，如果有
    String type;//类型：接受订单、提现、
    String date;//日期
    String rewardUserId;//打赏人id
    String rewardUserName;//打赏人昵称
    String rewardUserPhone;//打赏人手机号
    String rewardUserIcon;//打赏人头像


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getRewardUserId() {
        return rewardUserId;
    }

    public void setRewardUserId(String rewardUserId) {
        this.rewardUserId = rewardUserId;
    }

    public String getRewardUserName() {
        return rewardUserName;
    }

    public void setRewardUserName(String rewardUserName) {
        this.rewardUserName = rewardUserName;
    }

    public String getRewardUserPhone() {
        return rewardUserPhone;
    }

    public void setRewardUserPhone(String rewardUserPhone) {
        this.rewardUserPhone = rewardUserPhone;
    }

    public String getRewardUserIcon() {
        return rewardUserIcon;
    }

    public void setRewardUserIcon(String rewardUserIcon) {
        this.rewardUserIcon = rewardUserIcon;
    }
}
