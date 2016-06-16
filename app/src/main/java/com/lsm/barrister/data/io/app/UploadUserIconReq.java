package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;

import java.io.File;

/**
 * Created by lvshimin on 16/5/8.
 * 上传头像接口
 * <p/>
 * uploadUserIcon
 *   提交方式：post
 *   参数:userId, (userIcon，文件)，verifyCode
 *   返回值：resultCode，resultMsg；
 *   备注：
 */
public class UploadUserIconReq extends Action{

    File file;

    public UploadUserIconReq(Context context, File file) {
        super(context);
        this.file = file;

        addUserParams();

        addFile("userIcon",file);

    }

    @Override
    public String getName() {
        return UploadUserIconReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_UPLOAD_USERICON;
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
