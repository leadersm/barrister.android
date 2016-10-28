package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;

/**
 * Created by lvshimin on 16/8/20.
 * 上传案源
 */
public class UploadCaseReq extends Action{


    String desc;
    String phone;
    String area;
    String name;
    String company;
    String email;

    public UploadCaseReq(Context context, String desc, String name, String phone, String area, String company, String email) {
        super(context);

        this.desc = desc;
        this.phone = phone;
        this.area = area;
        this.name = name;
        this.company = company;
        this.email = email;

        params("caseInfo",desc);
        params("contactPhone",phone);
        params("area",area);
        params("contact",name);
        params("company",company);
        params("email",email);

        addUserParams();
    }

    @Override
    public String getName() {
        return UploadCaseReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_UPLOAD_CASE;
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
