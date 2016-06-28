package com.lsm.barrister.app;


import android.app.Application;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings.Secure;

import com.androidquery.AQuery;
import com.androidquery.callback.BitmapAjaxCallback;
import com.androidquery.util.AQUtility;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.lsm.barrister.push.PushUtil;
import com.lsm.barrister.utils.DLog;
import com.lsm.barrister.utils.NetworkManager;
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

//		strictMode();

        AppConfig.getInstance().init(this);

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        PushUtil.getInstance().init(this);

        //Facebook，Fresco
        Fresco.initialize(this);

        NetworkManager.getInstance(this).init();
        NetworkManager.getInstance(this).registerNetworkReceiver(this);

        //初始化deviceId
        Constants.deviceId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);//deviceId
        DLog.i(TAG, "deviceId:" + Constants.deviceId);

        //ECSDK-百度地图
        try {
//            SDKInitializer.initialize(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        if(!ECDevice.isInitialized()) {
//            ECDevice.initial(this, new ECDevice.InitListener() {
//                @Override
//                public void onInitialized() {
//                    // SDK已经初始化成功
//                    DLog.d(TAG,"ECDevice SDK已经初始化成功");
//                    ECHelper.getInstance().login(App.this);
//                }
//
//                @Override
//                public void onError(Exception exception) {
//                    DLog.e(TAG,"ECDevice initial onError");
//                    exception.printStackTrace();
//                    ECDevice.unInitial();
//
//                    // SDK 初始化失败,可能有如下原因造成
//                    // 1、可能SDK已经处于初始化状态
//                    // 2、SDK所声明必要的权限未在清单文件（AndroidManifest.xml）里配置、
//                    //    或者未配置服务属性android:exported="false";
//                    // 3、当前手机设备系统版本低于ECSDK所支持的最低版本（当前ECSDK支持
//                    //    Android Build.VERSION.SDK_INT 以及以上版本）
//                }
//            });


        OkHttpUtils.getInstance().debug("testDebug");
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
