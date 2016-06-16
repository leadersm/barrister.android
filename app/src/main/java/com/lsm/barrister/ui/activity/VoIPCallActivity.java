package com.lsm.barrister.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.androidquery.AQuery;
import com.lsm.barrister.R;
import com.lsm.barrister.rongcloud.ECHelper;
import com.lsm.barrister.utils.DLog;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECVoIPCallManager;

public class VoIPCallActivity extends BaseActivity {

    private static final String TAG = VoIPCallActivity.class.getSimpleName();

    /**昵称*/
    public static final String EXTRA_CALL_NAME = "con.lsm.barrister.VoIP_CALL_NAME";
    /**通话号码*/
    public static final String EXTRA_CALL_NUMBER = "con.lsm.barrister.VoIP_CALL_NUMBER";
    /**呼入方或者呼出方*/
    public static final String EXTRA_OUTGOING_CALL = "con.lsm.barrister.VoIP_OUTGOING_CALL";
    /**VoIP呼叫*/
    public static final String ACTION_VOICE_CALL = "con.lsm.barrister.intent.ACTION_VOICE_CALL";
    /**Video呼叫*/
    public static final String ACTION_VIDEO_CALL = "con.lsm.barrister.intent.ACTION_VIDEO_CALL";
    public static final String ACTION_CALLBACK_CALL = "con.lsm.barrister.intent.ACTION_VIDEO_CALLBACK";

    private boolean isCallBack;

    AQuery aq;

    /**通话昵称*/
    protected String mCallName;
    /**通话号码*/
    protected String mCallNumber;
    protected String mPhoneNumber;
    /**是否来电*/
    protected boolean mIncomingCall = false;
    /**呼叫唯一标识号*/
    protected String mCallId;
    /**VoIP呼叫类型（音视频）*/
    protected ECVoIPCallManager.CallType mCallType;
    /**透传号码参数*/
    private static final String KEY_TEL = "tel";
    /**透传名称参数*/
    private static final String KEY_NAME = "nickname";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voip_call);

        setupToolbar();

        mIncomingCall = !(getIntent().getBooleanExtra(EXTRA_OUTGOING_CALL, false));
        mCallType = (ECVoIPCallManager.CallType) getIntent().getSerializableExtra(ECDevice.CALLTYPE);

        StringBuffer sb = new StringBuffer();

        if(mIncomingCall) {

            // 来电
            mCallId = getIntent().getStringExtra(ECDevice.CALLID);
            DLog.e(TAG, "mCallId----" + mCallId);
            mCallNumber = getIntent().getStringExtra(ECDevice.CALLER);
            sb.append("来电，from:"+mCallId+",mCallNumber:"+mCallNumber);
        } else {
            // 呼出
            mCallName = getIntent().getStringExtra(EXTRA_CALL_NAME);
            mCallNumber = getIntent().getStringExtra(EXTRA_CALL_NUMBER);

            isCallBack = getIntent().getBooleanExtra(ACTION_CALLBACK_CALL, false);

            sb.append("呼出，mCallName:"+mCallName+",mCallNumber:"+mCallNumber);
        }


        aq = new AQuery(this);

        aq.id(R.id.tv_voip_from).text(sb.toString());
        aq.id(R.id.btn_voip_accept).clicked(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ECHelper.getInstance().setCurrentCallId(mCallId);
                ECHelper.getInstance().accept();
            }
        });
        aq.id(R.id.btn_voip_reject).clicked(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ECHelper.getInstance().setCurrentCallId(mCallId);
                ECHelper.getInstance().rejected();
            }
        });

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
