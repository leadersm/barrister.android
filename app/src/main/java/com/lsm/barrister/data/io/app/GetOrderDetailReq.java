package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;
import com.lsm.barrister.data.io.Test;

/**
 * Created by lvshimin on 16/5/8.
 * 订单详情接口
 * <p/>
 * OrderDetail
 *   提交方式：post
 *   参数:userId,orderId,verifyCode
 *   返回值：resultCode，resultMsg , orderDetail；
 *   备注：无
 */
public class GetOrderDetailReq extends Action{

    String orderId;

    public GetOrderDetailReq(Context context, String orderId) {
        super(context);
        this.orderId = orderId;
        addUserParams();
    }

    @Override
    public String getName() {
        return GetOrderDetailReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_GET_ORDER_DETAIL;
    }

    @Override
    public CommonResult parse(String json) throws Exception {
        IO.GetOrderDetailResult result = Test.getOrderDetailResult();//getFromGson(json, new TypeToken<IO.GetOrderDetailResult>() {});

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
