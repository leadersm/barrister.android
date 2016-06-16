package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;
import com.lsm.barrister.data.io.Test;

/**
 * Title: GetLatestVersionReq.java Description:获取最新版本信息
 *
 * @author lsm
 * @date 2015-4-3
 *   提交方式：get
 *   参数:无
 *   返回值：resultCode，resultMsg  ,version（程序版本）；
 *   备注：
 */
public class GetLatestVersionReq extends Action {

    public GetLatestVersionReq(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return GetLatestVersionReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_GET_APP_VERSION;
    }


    @Override
    public IO.GetAppVersionResult parse(String json) throws Exception {

        IO.GetAppVersionResult result = Test.getAppVersionResult();//getFromGson(json, new TypeToken<IO.GetAppVersionResult>() {});

        if(result!=null){

            if(result.resultCode == 200){

                onSafeCompleted(result.version);

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
