package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;

/**
 * Created by lvshimin on 16/5/8.
 * agreeOrderCancel
 *   提交方式：post
 *   参数:userId,verifyCode,orderId
 *   返回值：resultCode，resultMsg
 *   备注：将订单状态变成“已取消”，退钱
 */
public class AgreeRequestCancelReq extends Action {

    String orderId;

    public AgreeRequestCancelReq(Context context, String orderId) {
        super(context);
        this.orderId = orderId;

        params("orderId", orderId);

        addUserParams();

    }

    @Override
    public String getName() {
        return AgreeRequestCancelReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_AGREE_CANCEL_ORDER;
    }

    @Override
    public CommonResult parse(String json) throws Exception {
        CommonResult result = parseCommonResult(json);
        if (result != null && result.resultCode == 200) {
            onSafeCompleted(true);
        }
        return result;
    }

    @Override
    public int method() {
        return POST;
    }
}
