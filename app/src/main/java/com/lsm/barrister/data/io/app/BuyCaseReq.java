package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;

/**
 * Created by lvshimin on 16/8/20.
 * 律师接（购买）案源接口
 */
public class BuyCaseReq extends Action{

    String caseId;

    public BuyCaseReq(Context context, String caseId) {
        super(context);
        this.caseId = caseId;

        params("caseId",caseId);
        addUserParams();
    }

    @Override
    public String getName() {
        return BuyCaseReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_BUY_CASE;
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
