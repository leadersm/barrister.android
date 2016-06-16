package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;
import com.lsm.barrister.data.io.Test;

/**
 * Created by lvshimin on 16/5/8.
 * 获取我的预约状态，查询我的接单状态接口
 *   getMyAppointmentStatus
 *   提交方式：post
 *   参数:userId,verifyCode
 *   返回值：resultCode，resultMsg  ,status；
 *   备注：无
 */
public class GetMyAppointmentStatusReq extends Action{

    public GetMyAppointmentStatusReq(Context context) {
        super(context);
        addUserParams();
    }

    @Override
    public String getName() {
        return GetMyAppointmentStatusReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_GET_APPOINTMENT_STATUS;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        IO.GetAppointmentStatusResult result = Test.getAppointmentStatusResult();//getFromGson(json, new TypeToken<IO.GetAppointmentStatusResult>() {});

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
