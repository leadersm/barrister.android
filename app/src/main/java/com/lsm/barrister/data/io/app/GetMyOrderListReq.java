package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;
import com.lsm.barrister.data.io.Test;

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
public class GetMyOrderListReq extends Action{

    public static final String TYPE_IM = "IM";
    public static final String TYPE_APPOINTMENT = "APPOINTMENT";

    String type;
    int page;

    public static int pageSize = 20;

    public GetMyOrderListReq(Context context, int page, String type) {
        super(context);
        this.page = page;
        this.type = type;

        params("type",type);
        params("page",String.valueOf(page));
        params("pageSize",String.valueOf(pageSize));

        addUserParams();
    }

    @Override
    public String getName() {
        return GetMyOrderListReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_GET_ORDER_LIST;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        IO.GetMyOrdersResult result = Test.getMyOrdersResult(20);//getFromGson(json,new TypeToken<IO.GetMyOrdersResult>(){});

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
        return POST;
    }
}
