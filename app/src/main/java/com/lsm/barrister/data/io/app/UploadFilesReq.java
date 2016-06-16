package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;

import java.io.File;

/**
 * Created by lvshimin on 16/5/8.
 * 上传资格证书接口
 * <p/>
 * uploadFiles
 *   提交方式：post
 *   参数:userId，(gnvqs，法律职业资格证书)、（certificate、执业证书）（year、年检页）（card、身份证），verifyCode
 *   返回值：resultCode，resultMsg；
 *   备注：
 */
public class UploadFilesReq extends Action{

    public UploadFilesReq(Context context, File gnvqs,File certificate,File year,File card) {
        super(context);

        addUserParams();

        addFile("gnvqs",gnvqs);
        addFile("certificate",certificate);
        addFile("year",year);
        addFile("card",card);

    }

    @Override
    public String getName() {
        return UploadFilesReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_UPLOAD_FILES;
    }

    @Override
    public Action.CommonResult parse(String json) throws Exception {
        return parseCommonResult(json);
    }

    @Override
    public int method() {
        return POST;
    }


}
