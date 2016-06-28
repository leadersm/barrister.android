package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;


/**
 * Created by lvshimin on 16/5/8.
 * bizAreaAndBizTypeList
   提交方式：post
   参数：userId,verifyCode
   返回值：resultCode，resultMsg,List<BusinessArea> bizAreas 领域列表;List<BusinessType> bizTypes 业务类型;
   备注：律师端修改专长页面调用加载显示
 */
public class GetBizTypeAreaListReq extends Action {

    public GetBizTypeAreaListReq(Context context) {
        super(context);

        addUserParams();
    }

    @Override
    public String getName() {
        return GetBizTypeAreaListReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_BIZ_TYPE_AREA_LIST;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        IO.GetBizTypeAreaListResult result = getFromGson(json, new TypeToken<IO.GetBizTypeAreaListResult>() {});//Test.getGetBizTypeAreaListResult();//

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
