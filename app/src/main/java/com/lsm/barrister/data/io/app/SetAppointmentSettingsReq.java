package com.lsm.barrister.data.io.app;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;
import com.lsm.barrister.utils.DLog;

/**
 * Created by lvshimin on 16/5/8.
 * 预约设置接口
 * setAppointmentSettings
 *   提交方式：post
 *   参数:userId,verifyCode,(key:2016-05-10 ：value:0，1，0，1……）、(2016-05-11 ：0，1，0，1……）（日期为key,value为36个对应时段0表示不可用）
 *   返回值：resultCode，resultMsg  ；
 *   备注：无
 */
public class SetAppointmentSettingsReq extends Action{

    String settings;

    public SetAppointmentSettingsReq(Context context,@NonNull String settings) {
        super(context);
        this.settings = settings;

        params("settings",settings);

        DLog.d(getName(),"settings:"+settings);

        addUserParams();
    }

    @Override
    public String getName() {
        return SetAppointmentSettingsReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_SET_APPOINTMENT_SETTINGS;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        CommonResult result = getFromGson(json,new TypeToken<CommonResult>(){});

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
