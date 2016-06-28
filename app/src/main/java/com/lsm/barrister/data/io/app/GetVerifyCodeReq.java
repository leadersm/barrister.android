package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;

/**
 * Created by lvshimin on 16/5/8.
 * 获取验证码接口
 * <p/>
 *   名称：getVerifyCode
 *   提交方式：post
 *   参数:phone
 *   返回值：resultCode，resultMsg；
 *   备注：调用频率限制，后台，前端60s倒计时
 */
public class GetVerifyCodeReq extends Action{

    public GetVerifyCodeReq(Context context, String phone) {
        super(context);

        params("phone",phone);

    }

    @Override
    public String getName() {
        return GetVerifyCodeReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_GET_VERIFY_CODE;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        IO.GetVerifyCodeResult result = getFromGson(json,new TypeToken<IO.GetVerifyCodeResult>(){});//Test.getVerifyCodeResult();//

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
