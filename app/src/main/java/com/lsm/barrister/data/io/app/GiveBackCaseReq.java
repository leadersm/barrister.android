package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;

/**
 * Created by lvshimin on 16/8/20.
 * 退回案源接口
 *   giveCaseBack
 *   提交方式：post
 *   参数:userId,verifyCode,caseId（案源id）
 *   返回值：resultCode，resultMsg
 *   备注：案源详情页
 */
public class GiveBackCaseReq extends Action {

    String caseId;

    public GiveBackCaseReq(Context context, String caseId) {
        super(context);
        this.caseId = caseId;

        params("caseId", caseId);
        addUserParams();
    }

    @Override
    public String getName() {
        return GiveBackCaseReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_GIVE_CASE_BACK;
    }

    @Override
    public CommonResult parse(String json) throws Exception {
        return parseCommonResult(json);
    }

    @Override
    public int method() {
        return POST;
    }

}
