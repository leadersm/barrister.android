package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;

/**
 * Created by lvshimin on 16/5/8.
 * 完成订单
 *   finishOrder
 *   提交方式：post
 *   参数:userId,verifyCode,orderId
 *   返回值：resultCode，resultMsg
 *   备注：将订单状态变成“已完成”
 */
public class FinishOrderReq extends Action {

    String orderId;

    public FinishOrderReq(Context context, String orderId) {
        super(context);
        this.orderId = orderId;

        params("orderId", orderId);

        addUserParams();

    }

    @Override
    public String getName() {
        return FinishOrderReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_FINISH_ORDER;
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
