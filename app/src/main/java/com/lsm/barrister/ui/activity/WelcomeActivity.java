package com.lsm.barrister.ui.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;

import com.lsm.barrister.R;
import com.lsm.barrister.app.AppConfig;
import com.lsm.barrister.app.Constants;
import com.lsm.barrister.app.VersionHelper;
import com.lsm.barrister.data.entity.User;
import com.lsm.barrister.ui.UIHelper;
import com.lsm.barrister.utils.DLog;


/**
 * 欢迎页
 */
public class WelcomeActivity extends BaseActivity {

    private static final String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initAppInfo();

        handler.sendEmptyMessageDelayed(MSG_WHAT_GO, DELAYED);
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

    /**
     * 初始化应用信息，读取配置文件
     */
    private void initAppInfo() {
        try {

            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            //初始化版本信息
            VersionHelper.instance().initPackageInfo(this);

            //渠道信息
            Constants.MARKET = appInfo.metaData.getInt(Constants.MARKET_KEY);
            //debug
            Constants.DEBUG = appInfo.metaData.getBoolean(Constants.DEBUG_KEY);

            Log.d(TAG, "MARKET:" + Constants.MARKET + ",DEBUG:" + Constants.DEBUG);

            DisplayMetrics dm = getResources().getDisplayMetrics();

            DLog.i(TAG, "screenWidth:" + dm.widthPixels + "-screenHeigh:" + dm.heightPixels);

            Constants.screenSize = dm.widthPixels + "*" + dm.heightPixels;
            Constants.screenHeight = dm.heightPixels;
            Constants.screenWidth = dm.widthPixels;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
