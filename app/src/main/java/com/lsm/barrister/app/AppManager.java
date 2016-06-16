package com.lsm.barrister.app;

import android.app.Activity;
import android.content.Context;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.util.AQUtility;
import com.lsm.barrister.R;
import com.lsm.barrister.ui.UIHelper;

import java.util.Stack;

import cn.jpush.android.api.JPushInterface;

public class AppManager {

    private static final int CLEAN_TO = 30000000;

    private static final int CLEAN_MAX = 50000000;

    private static final String TAG = AppManager.class.getSimpleName();

    private static Stack<Activity> activityStack;

    private static AppManager manager = null;

    public static AppManager getAppManager() {
        if (manager == null) {
            manager = new AppManager();
        }
        return manager;
    }

    static boolean isMainActivityRunning = false;

    public static boolean isMainActivityRunning() {
        return isMainActivityRunning;
    }

    public static void setMainActivityRunning(boolean b) {
        isMainActivityRunning = b;
    }

    /**
     * �?��程序，停止加载，清缓�?
     *
     * @param context
     */
    public void AppExit(Context context) {
        try {

            AjaxCallback.cancel();

            AQUtility.cleanCacheAsync(context, CLEAN_MAX, CLEAN_TO);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加Activity到堆�?
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中�?���?��压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中�?���?��压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        if (activity != null) {
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束�?��Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    private long ctime = 0;

    /**
     * 双击返回�?��
     *
     * @param mContext
     * @return
     */
    public boolean exit(Context mContext) {
        if (ctime == 0) {

            ctime = System.currentTimeMillis();

            UIHelper.showToast(mContext, R.string.tip_exit_again);

            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(3000);
                        ctime = 0;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

            return false;
        }

        if (System.currentTimeMillis() - ctime < 1500) {


            if (!AppConfig.getInstance().isPushMsgOfflineEnabled()) {

                JPushInterface.stopPush(mContext);

            }

            AppExit(mContext);

            return true;
        }

        return false;
    }

}
