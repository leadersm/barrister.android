package com.lsm.barrister.utils;

import android.graphics.Color;

import com.lsm.barrister.data.entity.Case;
import com.lsm.barrister.data.entity.OrderDetail;

/**
 * Created by lvshimin on 16/7/2.
 */
public class OrderUtils {


    public static String getStatusString(String status) {
        String str = null;
        if (status.equals(OrderDetail.STATUS_WAITING)) {
            str = "待办";
        } else if (status.equals(OrderDetail.STATUS_DOING)) {
            str = "进行中";
        } else if (status.equals(OrderDetail.STATUS_DONE)) {
            str = "已完成";
        } else if (status.equals(OrderDetail.STATUS_CANCELED)) {
            str = "已取消";
        } else if (status.equals(OrderDetail.STATUS_REFUND)) {
            str = "退款中";
        } else if (status.equals(OrderDetail.STATUS_REQUEST_CANCELED)) {
            str = "取消中";
        }
        return str;
    }

    public static int getStatusColor(String status) {
        int statusColor = Color.parseColor("#cccccc");
        if (status.equals(OrderDetail.STATUS_WAITING)) {
            statusColor = Color.parseColor("#F8C82E");//ffef87;
        } else if (status.equals(OrderDetail.STATUS_DOING)) {
            statusColor = Color.parseColor("#45cd87");//45cd87;
        } else if (status.equals(OrderDetail.STATUS_DONE)) {
            statusColor = Color.parseColor("#59E1FA");//59E1FA;
        } else if (status.equals(OrderDetail.STATUS_CANCELED)) {
            statusColor = Color.parseColor("#848284");//848284;
        } else if (status.equals(OrderDetail.STATUS_REFUND)) {
            statusColor = Color.parseColor("#a9f82e");//a9f82e;
        } else if (status.equals(OrderDetail.STATUS_REQUEST_CANCELED)) {
            statusColor = Color.parseColor("#ff4444");//a9f82e;
        }
        return statusColor;
    }


    public static String getCaseStatusString(String status) {
        String str = null;
        if (status.equals(Case.STATUS_0_INIT)) {
            str = "未发布";
        } else if (status.equals(Case.STATUS_1_PUBLISHED)) {
            str = "已发布";
        } else if (status.equals(Case.STATUS_2_WAIT_UPDATE)) {
            str = "待更新";
        } else if (status.equals(Case.STATUS_3_WAIT_CLEARING)) {
            str = "待结算";
        } else if (status.equals(Case.STATUS_4_WAIT_CLEARED)) {
            str = "已结算";
        }else {
            str = getOldCaseStatusString(status);
        }
        return str;
    }

    public static String getOldCaseStatusString(String status) {
        String string = null;
        if (status != null) {
            if (status.equals(Case.STATUS_CONSULTING)) {
                string = "咨询";
            } else if (status.equals(Case.STATUS_INTERVIEW)) {
                string = "面谈";
            } else if (status.equals(Case.STATUS_SIGNATORY)) {
                string = "签约";
            } else if (status.equals(Case.STATUS_FOLLOWUP)) {
                string = "跟进";
            } else if (status.equals(Case.STATUS_CLEARING)) {
                string = "结算";
            }
        }
        return string;
    }


    /**
     * 获取状态颜色
     * @param status
     * @return
     */
    public static int getCaseStatusColor(String status) {
        int statusColor = Color.parseColor("#cccccc");
        if (status.equals(Case.STATUS_0_INIT)) {
            statusColor = Color.parseColor("#F8C82E");//ffef87;
        } else if (status.equals(Case.STATUS_1_PUBLISHED)) {
            statusColor = Color.parseColor("#45cd87");//45cd87;
        } else if (status.equals(Case.STATUS_2_WAIT_UPDATE)) {
            statusColor = Color.parseColor("#59E1FA");//59E1FA;
        } else if (status.equals(Case.STATUS_3_WAIT_CLEARING)) {
            statusColor = Color.parseColor("#4444ff");//848284;
        } else if (status.equals(Case.STATUS_4_WAIT_CLEARED)) {
            statusColor = Color.parseColor("#a9f82e");//a9f82e;
        }
        return statusColor;
    }

}
