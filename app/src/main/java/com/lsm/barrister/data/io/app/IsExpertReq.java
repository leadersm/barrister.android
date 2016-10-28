package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
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
public class IsExpertReq extends Action {

    public IsExpertReq(Context context) {
        super(context);
        addUserParams();

    }

    @Override
    public String getName() {
        return IsExpertReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_IS_EXPERT;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        Result result = getFromGson(json, new TypeToken<Result>() {});

        if (result != null && result.resultCode == 200) {
            onSafeCompleted(result.isExpert == 1);
        }

        return result;
    }

    class Result extends CommonResult {
        public int isExpert;//0,1
    }

    @Override
    public int method() {
        return POST;
    }


}
