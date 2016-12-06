package com.lsm.barrister.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.lsm.barrister.R;
import com.lsm.barrister.app.AppConfig;
import com.lsm.barrister.app.Constants;
import com.lsm.barrister.data.entity.User;
import com.lsm.barrister.module.push.PushUtil;
import com.lsm.barrister.ui.UIHelper;
import com.lsm.barrister.utils.DLog;

import cn.jpush.android.api.JPushInterface;


/**
 * 欢迎页
 */
public class WelcomeActivity extends BaseActivity {

    private static final String TAG = "WelcomeActivity";

    public static boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        isRunning = true;

        setupPush();

        handler.sendEmptyMessageDelayed(MSG_WHAT_GO, DELAYED);
    }

    private void setupPush() {
        if (TextUtils.isEmpty(Constants.PUSH_ID)) {
            String pushId = JPushInterface.getRegistrationID(this);
            DLog.d(TAG, "pushId:" + pushId);
        }

        String pushTag = AppConfig.getInstance().getPushTag(this);

        //设置别名，防止内外网推送混乱
        if (Constants.DEBUG) {

            DLog.d(TAG, "推送设置：内网接收");

            if (TextUtils.isEmpty(pushTag) || !pushTag.equals(Constants.TAG_LAN)) {
                PushUtil.getInstance().setTag(Constants.TAG_LAN);
            }

        } else {

            DLog.d(TAG, "推送设置：外网接收");
            if (TextUtils.isEmpty(pushTag) || !pushTag.equals(Constants.TAG_WAN)) {
                PushUtil.getInstance().setTag(Constants.TAG_WAN);
            }

        }

    }

    private static final int MSG_WHAT_GO = 0;
    //跳转延时
    private static final int DELAYED = 3000;


    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            //已登录，跳转主页
//            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
//            startActivity(intent);

            User user = AppConfig.getUser(getApplicationContext());

            if(user!=null){

                //已登录，跳转主页
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
            }else{
                //未登录跳转登录页
                UIHelper.goLoginActivity(WelcomeActivity.this);
            }

            finish();

        }
    };



    @Override
    public void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }
}
