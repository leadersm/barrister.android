package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;
import com.lsm.barrister.data.io.Test;

/**
 * Created by lvshimin on 16/5/8.
 * 轮播广告列表接口
 * <p/>
 * lunboAds
 *   提交方式：get
 *   参数:无
 *   返回值：resultCode，resultMsg ,List<Ad>；
 *   备注：
 */
public class GetLunboAdsReq extends Action{

    public GetLunboAdsReq(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return GetLunboAdsReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_HOME_LUNBO_ADS;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        IO.GetLunboAdsResult result = Test.getLunoAdsResult();//getFromGson(json, new TypeToken<IO.GetLunboAdsResult>() {});

        if(result!=null){

            if(result.resultCode == 200){

                onSafeCompleted(result);

            }

            return result;

        }
        return null;
    }

    @Override
    public int method() {
        return GET;
    }
}
