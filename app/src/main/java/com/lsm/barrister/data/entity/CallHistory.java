package com.lsm.barrister.data.entity;

/**
 * Created by lvshimin on 16/5/19.
 */
public class CallHistory {
    String callId;//通话id，网络电话建立通话后平台返回的id
    String orderId;//订单id
    String startTime;//通话开始时间
    long duration;//通话时长
    String recordUrl;//录音下载地址

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getRecordUrl() {
        return recordUrl;
    }

    public void setRecordUrl(String recordUrl) {
        this.recordUrl = recordUrl;
    }
}
