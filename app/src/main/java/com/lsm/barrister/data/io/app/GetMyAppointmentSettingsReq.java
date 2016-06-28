package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;

/**
 * Created by lvshimin on 16/5/8.
 * 获取我的预约设置接口
 * getMyAppointmentSettings
 *   提交方式：post
 *   参数:userId,verifyCode,date (当天)
 *   返回值：resultCode，resultMsg , List<AppointmentSetting> appointmentSettings (从当天开始七天的36个时段设置数据)；
 *   备注：6：00~24：00    36个时间段；
 */
public class GetMyAppointmentSettingsReq extends Action{

    String date;

    public GetMyAppointmentSettingsReq(Context context, String date) {
        super(context);
        this.date = date;

        params("date",date);

        addUserParams();

    }

    @Override
    public String getName() {
        return GetMyAppointmentSettingsReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_GET_MY_APPOINTMENT_SETTINGS;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        IO.GetAppointmentSettingsResult result = getFromGson(json, new TypeToken<IO.GetAppointmentSettingsResult>() {});//Test.getAppointmentSettingsResult();//

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
