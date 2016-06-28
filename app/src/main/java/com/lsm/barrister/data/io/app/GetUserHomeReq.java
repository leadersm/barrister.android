package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;

/**
 * Created by lvshimin on 16/5/8.
 * 用户首页接口
 * <p/>
 * userHome
 *   提交方式：post
 *   参数:userId,verifyCode
 *   返回值：resultCode，resultMsg , status（接单状态）,orderQty订单数，remainingBalance余额，totalIncome总收入，List<OrderItem> todoList待办事件；
 *   备注：（页面切换）前台获取更新首页信息。
 */
public class GetUserHomeReq extends Action{

    public GetUserHomeReq(Context context) {
        super(context);

        addUserParams();
    }

    @Override
    public String getName() {
        return GetUserHomeReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_GET_USER_HOME;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        final IO.HomeResult result = getFromGson(json,new TypeToken<IO.HomeResult>(){});//Test.getHomeResult();//

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
