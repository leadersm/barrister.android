package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;

/**
 * Created by lvshimin on 16/5/8.
 * .改变接单状态
 changeIMStatus
 提交方式：post
 参数：userId,verifyCode,status (can_not 或 can)
 返回值：resultCode，resultMsg；
 备注：
 */
public class ChangeIMStatusReq extends Action {

    String status;

    public ChangeIMStatusReq(Context context, String status) {
        super(context);
        this.status = status;

        params("status", status);

        addUserParams();

    }

    @Override
    public String getName() {
        return ChangeIMStatusReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_CHANGE_IM_STATUS;
    }

    @Override
    public CommonResult parse(String json) throws Exception {
        CommonResult result = parseCommonResult(json);
        return result;
    }

    @Override
    public int method() {
        return POST;
    }

    
}
