package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;

/**
 * Created by lvshimin on 16/5/8.
 * 收入明细列表接口
 * <p/>
 * getIncomeDetailList
 *   提交方式：post
 *   参数:userId,verifyCode,page,pageSize
 *   返回值：resultCode，resultMsg , List<IncomeDetail> incomeDetails 收入|支出列表；
 *   备注：点击跳转订单详情
 */
public class GetIncomeDetailListReq extends Action{

    int page = 1;
    int pageSize = 20;

    public GetIncomeDetailListReq(Context context, int page) {
        super(context);
        this.page = page;
        params("page",String.valueOf(page));
        params("pageSize",String.valueOf(pageSize));
        addUserParams();
    }

    @Override
    public String getName() {
        return GetIncomeDetailListReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_GET_INCOME_DETAIL_LIST;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        IO.GetIncomeDetailListResult result = getFromGson(json, new TypeToken<IO.GetIncomeDetailListResult>() {});//Test.getIncomeDetailListResult();//

        if(result!=null){

            if(result.resultCode == 200 ){

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
