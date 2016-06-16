package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;

/**
 * Created by lvshimin on 16/5/8.
 * 打电话接口
 * 提交方式：post
   参数：userId,verifyCode,orderId
   返回值：resultCode，resultMsg；
   备注：后台查订单中的律师和用户电话给双方建立通话
 */
public class MakeCallReq extends Action{

    String orderId;

    public MakeCallReq(Context context, String orderId) {
        super(context);
        this.orderId = orderId;

        params("orderId",orderId);
        addUserParams();
    }

    @Override
    public String getName() {
        return MakeCallReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_MAKE_CALL;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        CommonResult result = getFromGson(json, new TypeToken<CommonResult>() {});

        if(result!=null){

            if(result.resultCode == 200){

                onSafeCompleted(result);

            }

            return result;

        }else{
            throw new Exception("解析错误");
        }
    }

    @Override
    public int method() {
        return POST;
    }
}
