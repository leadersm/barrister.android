package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;
import com.lsm.barrister.data.io.Test;

/**
 * Created by lvshimin on 16/5/8.
 * 登陆|注册接口
 * <p/>
 * 名称：login
 *   提交方式：post
 *   参数:phone,verifyCode（验证码）
 *   返回值:resultCode，resultMsg，user（用户基本信息，资质验证状态，接单状态……）
 *   备注：后台保存verifyCode（作为一次性密码），前端也保存，涉及到用户操作的其他接口需带上此密码后台进行校验，如果不一致返回错误提示需要重新登陆。
 */
public class LoginReq extends Action {

    String phone;
    String verifyCode;

    public LoginReq(Context context, String phone, String verifyCode) {
        super(context);

        this.phone = phone;
        this.verifyCode = verifyCode;

        params.put("phone", phone);
        params.put("verifyCode", verifyCode);

    }

    @Override
    public String getName() {
        return LoginReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_LOGIN;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        IO.LoginResult result = Test.getLoginResult();//getFromGson(json,new TypeToken<IO.LoginResult>(){});

        if(result!=null){

            if(result.resultCode == 200){

                onSafeCompleted(result.user);

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
