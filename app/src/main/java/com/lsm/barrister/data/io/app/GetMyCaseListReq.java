package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;

/**
 * Created by lvshimin on 16/5/8.
 * 案源列表caseList
 *   提交方式：post
 *   参数：userId,verifyCode, page, pageSize
 *   返回值：resultCode，resultMsg；List<Case> cases,total
 */
public class GetMyCaseListReq extends Action {
    int page;
    public static int pageSize = 20;

    public GetMyCaseListReq(Context context, int page) {
        super(context);
        this.page = page;

        params("page", String.valueOf(page));
        params("pageSize", String.valueOf(pageSize));

        addUserParams();
    }

    @Override
    public String getName() {
        return GetMyCaseListReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_GET_MY_CASE_LIST;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        IO.GetCaseListResult result = getFromGson(json, new TypeToken<IO.GetCaseListResult>() {});//Test.getCaseListResult(20);//

        if (result != null) {

            if (result.resultCode == 200) {

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
