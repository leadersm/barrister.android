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
    //    d.请求取消
    public static final String STATUS_REQUEST_CANCELED = "order.status.request.cancel";
    //    e.已取消
    public static final String STATUS_CANCELED = "order.status.canceled";
    //    f.退款中
    public static final String STATUS_REFUND = "order.status.refund";


    public static final String ISSTART_YES = "isStart.yes";// 已经给星
    public static final String ISSTART_NO = "isStart.no";// 没有给星

    String id;//订单id
    String orderNo;//订单号
    String type;//订单类型：即时，预约
    String status;//订单状态
    String caseType;//案件类型（中文），财产纠纷，民事纠纷，……
    String payTime;//下单时间
    float paymentAmount;//支付金额
    String remarks;//备注

    String customerId;//客户id
    String customerNickname;//客户昵称
    String customerIcon;//客户头像
    String customerPhone;//客户电话

    String barristerId;//律师id
    String barristerNickname;//律师昵称
    String barristerIcon;//律师头像
    String barristerPhone;//律师电话

    String lawFeedback;//律师小结

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    String comment;//用户评论

    String startTime ;//start_time	开始时间
    String endTime ;//end_time		结束时间

    List<CallHistory> callHistories;//通话记录

    String isStart;

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

    public float getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(float paymentAmount) {
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

    public String getLawFeedback() {
        return lawFeedback;
    }

    public void setLawFeedback(String lawFeedback) {
        this.lawFeedback = lawFeedback;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    public String getBarristerId() {
        return barristerId;
    }

    public void setBarristerId(String barristerId) {
        this.barristerId = barristerId;
    }

    public String getBarristerNickname() {
        return barristerNickname;
    }

    public void setBarristerNickname(String barristerNickname) {
        this.barristerNickname = barristerNickname;
    }

    public String getBarristerIcon() {
        return barristerIcon;
    }

    public void setBarristerIcon(String barristerIcon) {
        this.barristerIcon = barristerIcon;
    }

    public String getBarristerPhone() {
        return barristerPhone;
    }

    public void setBarristerPhone(String barristerPhone) {
        this.barristerPhone = barristerPhone;
    }

    public String getIsStart() {
        return isStart;
    }

    public void setIsStart(String isStart) {
        this.isStart = isStart;
    }
}
