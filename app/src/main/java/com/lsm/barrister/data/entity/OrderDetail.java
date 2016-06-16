package com.lsm.barrister.data.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lvshimin on 16/5/8.
 * 订单详情
 */
public class OrderDetail implements Serializable {

    //    a.待处理
    public static final String STATUS_WAITING = "order.status.waiting";
    //    b.进行中
    public static final String STATUS_DOING = "order.status.doing";
    //    c.已完成
    public static final String STATUS_DONE = "order.status.done";
    //    d.已取消
    public static final String STATUS_CANCELED = "order.status.canceled";
    //    e.退款中
    public static final String STATUS_REFUND = "order.status.refund";

    String id;//订单id
    String orderNo;//订单号
    String type;//订单类型：即时，预约
    String status;//订单状态
    String caseType;//案件类型（中文），财产纠纷，民事纠纷，……
    String payTime;//下单时间
    int paymentAmount;//支付金额
    String remarks;//备注

    String customerId;//客户id
    String customerNickname;//客户昵称
    String customerIcon;//客户头像
    String customerPhone;//客户电话

    List<CallHistory> callHistories;//通话记录

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerNickname() {
        return customerNickname;
    }

    public void setCustomerNickname(String customerNickname) {
        this.customerNickname = customerNickname;
    }

    public String getCustomerIcon() {
        return customerIcon;
    }

    public void setCustomerIcon(String customerIcon) {
        this.customerIcon = customerIcon;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public List<CallHistory> getCallHistories() {
        return callHistories;
    }

    public void setCallHistories(List<CallHistory> callHistories) {
        this.callHistories = callHistories;
    }
}
