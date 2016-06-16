package com.lsm.barrister.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.lsm.barrister.R;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;
import com.lsm.barrister.data.io.app.GetMyMsgsReq;

/**
 * 消息列表页
 * 1.收到预约
 * 2.评价
 * 3.到账
 * 4.提现记录
 */
public class MsgsActivity extends BaseActivity {

    GetMyMsgsReq mGetMyMsgListReq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        setupToolbar();

        load();
    }

    int page = 1;
    private void load() {
        mGetMyMsgListReq = new GetMyMsgsReq(this,page = 1);
        mGetMyMsgListReq.execute(new Action.Callback<IO.GetMyMsgsResult>() {
            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {

            }

            @Override
            public void onCompleted(IO.GetMyMsgsResult getMyMsgsResult) {

            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_msg);
    }
}
