package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;

/**
 * Created by lvshimin on 16/5/8.
 * disagreeOrderCancel
 *   提交方式：post
 *   参数:userId,verifyCode,orderId
 *   返回值：resultCode，resultMsg
 *   备注：将订单状态变成“待处理”，驳回（是否需要填写理由？。待定。。）
 */
public class DisagreeRequestCancelReq extends Action {

    String orderId;

    public DisagreeRequestCancelReq(Context context, String orderId) {
        super(context);
        this.orderId = orderId;

        params("orderId", orderId);

        addUserParams();

    }

    @Override
    public String getName() {
        return DisagreeRequestCancelReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_DISAGREE_CANCEL_ORDER;
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
