package com.lsm.barrister.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.androidquery.AQuery;
import com.lsm.barrister.R;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.app.FeedbackReq;
import com.lsm.barrister.ui.UIHelper;


/**
 * 反馈页
 */
public class FeedbackActivity extends BaseActivity {

    AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        setupToolbar();

        aq = new AQuery(this);
        aq.id(R.id.btn_feedback_commit).clicked(new View.OnClickListener() {
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

        String content = aq.id(R.id.et_feedback_content).getEditable().toString();
        if(TextUtils.isEmpty(content)){
            UIHelper.showToast(getApplicationContext(),getString(R.string.tip_empty_feedback));
            return;
        }

        String contact = aq.id(R.id.et_feedback_contact).getEditable().toString();

        new FeedbackReq(this,content,contact).execute(new Action.Callback<Boolean>() {

            @Override
            public void progress() {
                isLoading = true;
            }

            @Override
            public void onError(int errorCode, String msg) {
                isLoading = false;
            }

            @Override
            public void onCompleted(Boolean aBoolean) {
                isLoading = false;
                UIHelper.showToast(getApplicationContext(),"感谢您的反馈");
                finish();
            }
        });

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.feedback);
    }
}
