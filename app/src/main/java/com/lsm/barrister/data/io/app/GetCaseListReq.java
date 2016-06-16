package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;
import com.lsm.barrister.data.io.Test;

/**
 * Created by lvshimin on 16/5/8.
 * 案源列表caseList
 *   提交方式：post
 *   参数：userId,verifyCode, page, pageSize
 *   返回值：resultCode，resultMsg；List<Case> cases,total
 */
public class GetCaseListReq extends Action {
    int page;
    public static int pageSize = 20;

    public GetCaseListReq(Context context, int page) {
        super(context);
        this.page = page;

        params("page", String.valueOf(page));
        params("pageSize", String.valueOf(pageSize));

        addUserParams();
    }

    @Override
    public String getName() {
        return GetCaseListReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_GET_STUDY_LIST;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        IO.GetCaseListResult result = Test.getCaseListResult(20);//getFromGson(json, new TypeToken<IO.GetCaseListResult>() {});

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
