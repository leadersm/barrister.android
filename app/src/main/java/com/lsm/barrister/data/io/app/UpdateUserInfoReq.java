package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;

import java.util.HashMap;

/**
 * Created by lvshimin on 16/5/8.
 * 修改个人信息接口
 * <p/>
 * updateUserInfo
 *   提交方式：post
 *   参数:userId，verifyCode,name、gendar、phone（重新验证 TBD）、email、area（省+市）、goodat（专长）、lawOffice(律所)，employmentYears(从业时间)、introduction（个人简介）
 *   返回值：resultCode，resultMsg , user；
 *   备注：resultCode，resultMsg；
 */
public class UpdateUserInfoReq extends Action{
    HashMap<String,String> userParams;

    public UpdateUserInfoReq(Context context, HashMap<String, String> userParams) {
        super(context);
        this.userParams = userParams;
        for(String key:userParams.keySet()){
            params(key,userParams.get(key));
        }
        addUserParams();
    }

    @Override
    public String getName() {
        return UpdateUserInfoReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_UPDATE_USER;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        IO.GetUpdateUserResult result = getFromGson(json, new TypeToken<IO.GetUpdateUserResult>() {});//Test.GetUpdateUserResult();//

        if(result!=null){

            if(result.resultCode == 200){

                onSafeCompleted(result.user);

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
