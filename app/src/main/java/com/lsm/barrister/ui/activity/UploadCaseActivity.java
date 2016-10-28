package com.lsm.barrister.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.androidquery.AQuery;
import com.lsm.barrister.R;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.app.UploadCaseReq;
import com.lsm.barrister.ui.UIHelper;

/**
 * Created by lvshimin on 16/8/20.
 */
public class UploadCaseActivity extends BaseActivity{
    AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_case);
        setupToolbar();

        aq = new AQuery(this);
        aq.id(R.id.btn_case_commit).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCommit();
            }
        });
    }

    boolean isLoading = false;

    private void doCommit() {

        if(isLoading){
            return;
        }

        //问题描述
        String desc = aq.id(R.id.et_case_desc).getEditable().toString();
        if(TextUtils.isEmpty(desc)){
            UIHelper.showToast(getApplicationContext(),getString(R.string.tip_empty_case_desc));
            return;
        }

        //联系人
        String name = aq.id(R.id.et_case_cname).getEditable().toString();
        if(TextUtils.isEmpty(name)){
            UIHelper.showToast(getApplicationContext(),getString(R.string.tip_empty_case_cname));
            return;
        }

        //联系方式
        String phone = aq.id(R.id.et_case_contact).getEditable().toString();
        if(TextUtils.isEmpty(phone)){
            UIHelper.showToast(getApplicationContext(),getString(R.string.tip_empty_case_phone));
            return;
        }

        //地区
        String area = aq.id(R.id.et_case_area).getEditable().toString();
        if(TextUtils.isEmpty(area)){
            UIHelper.showToast(getApplicationContext(),getString(R.string.tip_empty_case_area));
            return;
        }

        //公司抬头
        String company = aq.id(R.id.et_case_company).getEditable().toString();

        //Email
        String email = aq.id(R.id.et_case_email).getEditable().toString();

        new UploadCaseReq(this,desc,name,phone,area,company,email).execute(new Action.Callback<Boolean>() {

            @Override
            public void progress() {
                isLoading = true;
                progressDialog.setMessage(getString(R.string.loading));
                progressDialog.show();
            }

            @Override
            public void onError(int errorCode, String msg) {
                isLoading = false;
                progressDialog.dismiss();
            }

            @Override
            public void onCompleted(Boolean aBoolean) {
                progressDialog.dismiss();
                isLoading = false;
                UIHelper.showToast(getApplicationContext(),"提交成功");
                finish();
            }
        });

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.upload_case);
    }


}
