package com.lsm.barrister.app;


import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.BitmapAjaxCallback;
import com.androidquery.util.AQUtility;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.lsm.barrister.push.PushUtil;
import com.lsm.barrister.utils.DLog;
import com.tencent.bugly.crashreport.CrashReport;
import com.zhy.http.okhttp.OkHttpUtils;

import cn.jpush.android.api.JPushInterface;


/**
 * 应用程序入口
 *
 * @author lsm
 */
public class App extends Application {

    private static final String TAG = App.class.getSimpleName();

    private static App instance;

    /**
     * 单例，返回一个实例
     * @return
     */
    public static App getInstance() {
        if (instance == null) {
            DLog.w(TAG,"[ECApplication] instance is null.");
        }
        return instance;
    }

    public void onCreate() {
        super.onCreate();
        init();
    }

    public void init() {

        instance = this;

        initAppInfo();

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        PushUtil.getInstance().init(this);

        //Facebook，Fresco
        Fresco.initialize(this);

        OkHttpUtils.getInstance().debug("testDebug");
    }


    /**
     * 初始化应用信息，读取配置文件
     */
    private void initAppInfo() {
        try {

            //bugly
            CrashReport.initCrashReport(getApplicationContext(), "900037690", false);
            //tencent.bugly
            CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
            strategy.setAppChannel(Constants.MARKET);
            strategy.setAppReportDelay(20000);//20s后上报

            AppConfig.getInstance().init(this);

            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            //初始化版本信息
            VersionHelper.instance().initPackageInfo(this);

            //初始化deviceId
            Constants.deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);//deviceId
            DLog.i(TAG, "deviceId:" + Constants.deviceId);

            //渠道信息
            Constants.MARKET = appInfo.metaData.getString(Constants.MARKET_KEY);
            //debug
            Constants.DEBUG = appInfo.metaData.getBoolean(Constants.DEBUG_KEY);

            //pushId
            Constants.PUSH_ID = AppConfig.getInstance().getPushId(this);

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

    private void strictMode() {
        if (AQuery.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Constants.DEBUG) {
            AQUtility.debug("enable strict mode!");

            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
//            .penaltyDeath()
                    .build());

        }
    }

    @Override
    public void onLowMemory() {
        BitmapAjaxCallback.clearCache();
        super.onLowMemory();
    }


}
