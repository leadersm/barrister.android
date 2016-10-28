package com.lsm.barrister.module.push;

import android.app.Notification;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.lsm.barrister.R;
import com.lsm.barrister.app.AppConfig;
import com.lsm.barrister.utils.DLog;
import com.lsm.barrister.utils.NetworkManager;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class PushUtil {
    private static final String TAG = PushUtil.class.getSimpleName();
    public static final String KEY_APP_KEY = "JPUSH_APPKEY";


    private static PushUtil instance = null;

    private PushUtil() {

    }

    private Context context;

    public void init(Context context) {
        this.context = context;
    }

    public static PushUtil getInstance() {
        if (instance == null) {
            instance = new PushUtil();
        }

        return instance;
    }


    public static boolean isEmpty(String s) {
        if (null == s)
            return true;
        if (s.length() == 0)
            return true;
        return s.trim().length() == 0;
    }

    // 校验Tag Alias 只能是数字,英文字母和中文
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    // 取得AppKey
    public static String getAppKey(Context context) {
        Bundle metaData = null;
        String appKey = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai)
                metaData = ai.metaData;
            if (null != metaData) {
                appKey = metaData.getString(KEY_APP_KEY);
                if ((null == appKey) || appKey.length() != 24) {
                    appKey = null;
                }
            }
        } catch (NameNotFoundException e) {

        }
        return appKey;
    }


    public static String getImei(Context context, String imei) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        } catch (Exception e) {
            Log.e(PushUtil.class.getSimpleName(), e.getMessage());
        }
        return imei;
    }


    /**
     * 重置用户标签
     */
    public void resetTags() {

        Set<String> tagSet = new LinkedHashSet<String>();

        //调用JPush API设置Tag
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));
    }

    public void setTag(String tag) {
        Set<String> tagSet = new LinkedHashSet<String>();

        tagSet.add(tag);

        //调用JPush API设置Tag
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));
    }

    public void setAlias(String alias) {

        if (TextUtils.isEmpty(alias)) {
            DLog.e(TAG, "empty tag and alias");
            return;
        }

        if (!PushUtil.isValidTagAndAlias(alias)) {
            DLog.e(TAG, "invalid valid tag and alias");
            return;
        }

        //调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    /**
     * 设置通知提示方式 - 基础属性
     */
    public void setStyleBasic() {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
        builder.statusBarDrawable = R.drawable.ic_launcher;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
        builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
        JPushInterface.setPushNotificationBuilder(1, builder);
        DLog.d(TAG, "Custom Builder - 1");
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    DLog.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    DLog.i(TAG, logs);
                    if (NetworkManager.isNetworkConnected(context)) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    DLog.e(TAG, logs);
            }
        }

    };

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";

                    DLog.i(TAG, logs);

                    if (tags != null && !tags.isEmpty()) {

                        Iterator<String> iterator = tags.iterator();
                        String myTag = iterator.next();
                        AppConfig.getInstance().setPushTag(myTag);
                        DLog.i(TAG, "save:" + myTag);

                    }
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    DLog.i(TAG, logs);
                    if (NetworkManager.isNetworkConnected(getContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    DLog.e(TAG, logs);
            }
        }

    };

    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;
    private static final int MSG_DEL_TAGS = 1003;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    DLog.d(TAG, "Set alias in handler.");
                    JPushInterface.setAliasAndTags(getContext(), (String) msg.obj, null, mAliasCallback);
                    break;

                case MSG_SET_TAGS:
                    DLog.d(TAG, "Set tags in handler.");
                    JPushInterface.setAliasAndTags(getContext(), null, (Set<String>) msg.obj, mTagsCallback);
                    break;

                default:
                    DLog.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };


    /**
     * 设置允许接收通知时间
     */
    public static void setPushTime(Context context) {
        //调用JPush api设置Push时间
//		JPushInterface.setPushTime(context, days, startime, endtime);
        //save sp..
    }

    public Context getContext() {
        return context;
    }
}
