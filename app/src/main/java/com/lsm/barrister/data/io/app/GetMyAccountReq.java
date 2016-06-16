package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;
import com.lsm.barrister.data.io.Test;

/**
 * Created by lvshimin on 16/5/8.
 * 我的账户接口
 * <p/>
 * myAccount
 *   提交方式：post
 *   参数:userId,verifyCode
 *   返回值：resultCode，resultMsg , account(账户对象，余额，总收入，银行卡信息)；
 *   备注：无
 */
public class GetMyAccountReq extends Action{

    public GetMyAccountReq(Context context) {
        super(context);
        addUserParams();
    }

    @Override
    public String getName() {
        return GetMyAccountReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_GET_MY_ACCOUNT;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        IO.GetAccountResult result = Test.getMyAccount();//getFromGson(json, new TypeToken<IO.GetAccountResult>() {});

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
