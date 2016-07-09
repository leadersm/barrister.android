package com.lsm.barrister.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.androidquery.AQuery;
import com.lsm.barrister.R;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.app.AddOrderSummaryReq;
import com.lsm.barrister.ui.UIHelper;


/**
 * 小结反馈页
 */
public class AddOrderSummaryActivity extends BaseActivity {

    AQuery aq;

    AddOrderSummaryReq mFeedbackReq;
    String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_summary);
        setupToolbar();
        orderId = getIntent().getStringExtra("id");

        aq = new AQuery(this);
        aq.id(R.id.btn_feedback_commit).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCommit();
            }
        });
    }

    private void doCommit() {

        String content = aq.id(R.id.et_feedback_content).getEditable().toString();
        if(TextUtils.isEmpty(content)){
            UIHelper.showToast(getApplicationContext(),getString(R.string.tip_empty_feedback));
            return;
        }

        mFeedbackReq = new AddOrderSummaryReq(this,orderId,content);

        mFeedbackReq.execute(new Action.Callback<Boolean>() {

            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {
                UIHelper.showToast(getApplicationContext(),msg);
            }

            @Override
            public void onCompleted(Boolean aBoolean) {
                UIHelper.showToast(getApplicationContext(),"提交成功");

                setResult(RESULT_OK);

                finish();
            }
        });

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_summary);
    }
}
