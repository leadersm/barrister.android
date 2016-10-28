package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;

/**
 * Created by lvshimin on 16/5/8.
 * addOrderSummary
 *   提交方式：post
 *   参数:userId,verifyCode,orderId,lawFeedback
 *   返回值：resultCode，resultMsg
 *   备注：
 */
public class AddOrderSummaryReq extends Action {

    String orderId;
    String lawFeedback;

    public AddOrderSummaryReq(Context context, String orderId, String lawFeedback) {
        super(context);
        this.orderId = orderId;
        this.lawFeedback = lawFeedback;

        params("orderId", orderId);
        params("content", lawFeedback);

        addUserParams();

    }

    @Override
    public String getName() {
        return AddOrderSummaryReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_ADD_SUMMARY;
    }

    @Override
    public CommonResult parse(String json) throws Exception {
        CommonResult result = parseCommonResult(json);
        return result;
    }

    @Override
    public int method() {
        return POST;
    }
}
