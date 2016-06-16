package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;

/**
 * Created by lvshimin on 16/5/8.
 * 提现请求接口
 * <p/>
 * getMoney
 *   提交方式：post
 *   参数:userId,verifyCode,money
 *   返回值：resultCode，resultMsg ；
 *   备注：后台记录提现操作（每人每个月只能提现一次，错误码，错误信息提示）。完成后前台提示每个月固定日期到账。
 */
public class GetMoneyReq extends Action{

    String money;

    public GetMoneyReq(Context context, String money) {
        super(context);
        this.money = money;

        addUserParams();
    }

    @Override
    public String getName() {
        return GetMoneyReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_GET_MONEY;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        CommonResult result = getFromGson(json, new TypeToken<CommonResult>() {});

        if(result!=null){

            if(result.resultCode == 200){

                onSafeCompleted(true);

            }

            return result;

        }
        return null;
    }

    @Override
    public int method() {
        return POST;
    }
}
