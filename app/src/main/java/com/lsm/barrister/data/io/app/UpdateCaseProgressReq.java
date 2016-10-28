package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;

import java.io.File;

/**
 * Created by lvshimin on 16/8/20.
 * 更新案源
 *   updateCaseProgress
 *   提交方式：post
 *   参数:userId,verifyCode,caseId（案源id），hasContract(是否已签订合同),money(合同金额),pic（合同照片）
 *   返回值：resultCode，resultMsg
 *   备注：勾选（是否已签订合同），填写金额，上传合同照片，提交
 */
public class UpdateCaseProgressReq extends Action {

    String caseId;
    boolean hasContract;
    String money;
    File pic;
    String percentagePayment;

    public UpdateCaseProgressReq(Context context, String caseId, boolean hasContract, String money, String percentagePayment, File pic) {
        super(context);
        this.caseId = caseId;
        this.hasContract = hasContract;
        this.money = money;
        this.percentagePayment = percentagePayment;
        this.pic = pic;

        params("caseId", caseId);
        params("hasContract", hasContract ? String.valueOf(1) : String.valueOf(0));
        params("contractMoney", money);
        params("percentagePayment", percentagePayment);
        addFile("pic", pic);

        addUserParams();

    }

    @Override
    public String getName() {
        return UpdateCaseProgressReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_UPDATE_CASE_PROGRESS;
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
