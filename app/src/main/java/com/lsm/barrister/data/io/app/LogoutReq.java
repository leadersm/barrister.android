package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;

/**
 * Created by lvshimin on 16/5/8.
 * 注销接口
 * <p/>
 * logout
 *   提交方式：post
 *   参数:id，verifyCode
 *   返回值：resultCode，resultMsg；
 *   备注：后台清除登陆的verifyCode（验证码，一次性密码），前台也清除用户信息。
 */
public class LogoutReq extends Action{

    public LogoutReq(Context context) {
        super(context);

        addUserParams();

    }

    @Override
    public String getName() {
        return LogoutReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_LOGOUT;
    }

    @Override
    public Action.CommonResult parse(String json) throws Exception {
        final Action.CommonResult result = getFromGson(json,new TypeToken<Action.CommonResult>(){});

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
