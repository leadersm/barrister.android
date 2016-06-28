package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;

/**
 * Created by lvshimin on 16/5/8.
 * 我的订单列表接口
 * <p/>
 * myOrderList
 *   提交方式：post
 *   参数:userId,verifyCode,page,pageSize,type(IM即时,APPOINTMENT预约)
 *   返回值：resultCode，resultMsg , List<OrderItem> orderItems 订单列表 , total（总数，翻页计算用）；
 *   备注：无
 */
public class GetStudyChannelListReq extends Action{

    public GetStudyChannelListReq(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return GetStudyChannelListReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_GET_CHANNEL_LIST;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        IO.GetChannelListResult result = getFromGson(json,new TypeToken<IO.GetChannelListResult>(){});//Test.getMyOrdersResult(20);//

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
