package com.lsm.barrister.module.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import com.androidquery.util.AQUtility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lsm.barrister.R;
import com.lsm.barrister.app.AppConfig;
import com.lsm.barrister.app.AppManager;
import com.lsm.barrister.app.Constants;
import com.lsm.barrister.app.UserHelper;
import com.lsm.barrister.app.VersionHelper;
import com.lsm.barrister.data.db.PushMessage;
import com.lsm.barrister.data.db.UserDbService;
import com.lsm.barrister.ui.activity.MainActivity;
import com.lsm.barrister.ui.activity.MyAccountActivity;
import com.lsm.barrister.ui.activity.OrderDetailActivity;
import com.lsm.barrister.ui.activity.WebViewActivity;
import com.lsm.barrister.ui.activity.WelcomeActivity;
import com.lsm.barrister.utils.DLog;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {

    private static final String TAG = "MyReceiver";

    /**
     * 接收消息回调
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();

        DLog.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);

            Log.d(TAG, "pushId:" + regId);

            Constants.PUSH_ID = regId;

            AppConfig.getInstance().setPushId(regId);

            //向服务器发送注册设备请求
            UserHelper.getInstance().uploadPushId(context, regId);

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {

            DLog.d(TAG, "自定义的消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

            handleCustomMessage(context, bundle);

        }
    }

    /**
     * 打印所有的 intent extra 数据
     *
     * @param bundle
     * @return
     */
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    /**
     * 处理推送消息,判断类型，判断显示类型
     * 新闻：根据新闻id发送获取新闻Item请求，成功后提示notification
     * 活动:
     * 版本更新：
     *
     * @param context
     * @param bundle
     */
    private void handleCustomMessage(Context context, Bundle bundle) {

//		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//		DLog.i(TAG, message);

        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        DLog.i(TAG, extras);

        if (!PushUtil.isEmpty(extras)) {
            try {
                Gson gson = new Gson();
                //解析
                PushMessage msg = gson.fromJson(extras, new TypeToken<PushMessage>() {}.getType());

                //通知
                String content = msg.getContent();
                String type = msg.getType();
                String contentId = msg.getContentId();

                if (msg.getType().equals(PushMessage.TYPE_ORDER_MONEY)) {
                    //订单服务费到账
                    String title = "订单服务费到账通知";
                    //消息入库
                    UserDbService.getInstance(context).getPushMessageAction().save(msg);

                    showNotification(context,title,content,type,contentId);

                }else if (msg.getType().equals(PushMessage.TYPE_ORDER_REWARD)) {

                    //打赏
                    String title = "打赏通知";
                    //消息入库
                    UserDbService.getInstance(context).getPushMessageAction().save(msg);

                    showNotification(context,title,content,type,contentId);

                }else if (msg.getType().equals(PushMessage.TYPE_ORDER_NEW)) {

                    //新订单
                    String title = "订单通知";
                    //消息入库
                    UserDbService.getInstance(context).getPushMessageAction().save(msg);

                    showNotification(context,title,content,type,contentId);

                }else if (msg.getType().equals(PushMessage.TYPE_ORDER_STATUS)) {

                    //订单状态
                    String title = "订单通知";
                    //消息入库
                    UserDbService.getInstance(context).getPushMessageAction().save(msg);

                    showNotification(context,title,content,type,contentId);

                }else if (msg.getType().equals(PushMessage.TYPE_RECEIVE_STAR)) {

                    //收到评价
                    String title = "订单评价通知";
                    //消息入库
                    UserDbService.getInstance(context).getPushMessageAction().save(msg);

                    showNotification(context,title,content,type,contentId);

                }else if (msg.getType().equals(PushMessage.TYPE_RECHARGE)) {

                    //充值
                    String title = "充值通知";
                    //消息入库
                    UserDbService.getInstance(context).getPushMessageAction().save(msg);

                    showNotification(context,title,content,type,contentId);

                }else if (msg.getType().equals(PushMessage.TYPE_VERIFY)) {

                    //律师段认证通知
                    String title = "认证通知";
                    //消息入库
                    UserDbService.getInstance(context).getPushMessageAction().save(msg);

                    showNotification(context,title,content,type,contentId);

                }else if (msg.getType().equals(PushMessage.TYPE_LEARNING)) {

                    //学习中心
                    String title = "学习中心";
                    //消息入库
                    UserDbService.getInstance(context).getPushMessageAction().save(msg);

//                    showNotification(context,title,content,type,contentId);

                }else if (msg.getType().equals(PushMessage.TYPE_GET_MONEY)) {

                    //提现
                    String title = "提现通知";
                    //消息入库
                    UserDbService.getInstance(context).getPushMessageAction().save(msg);

                    showNotification(context,title,content,type,contentId);

                }else if (msg.getType().equals(PushMessage.TYPE_ORDER_BACK_MONEY)) {

                    //订单取消退款
                    String title = "退款通知";
                    //消息入库
                    UserDbService.getInstance(context).getPushMessageAction().save(msg);

                    showNotification(context,title,content,type,contentId);

                }else if (msg.getType().equals(PushMessage.TYPE_HANG_OFF)) {

                    //挂断电话，提示律师
                    String title = "系统通知";
                    //消息入库
                    UserDbService.getInstance(context).getPushMessageAction().save(msg);

                    showNotification(context,title,content,type,contentId);

                }
                //========================COMMON=================================
                else if (msg.getType().equals(PushMessage.TYPE_SYSTEM_MSG)) {

                    //处理
                    String title = "系统通知";
                    //消息入库
                    UserDbService.getInstance(context).getPushMessageAction().save(msg);

                    showNotification(context,title,content,type,contentId);

                }else if (msg.getType().equals(PushMessage.TYPE_FORCE_UPDATE)) {
                    //强制更新
                    AppConfig.getInstance().setForceUpdate(true);

                    if (AppManager.isMainActivityRunning()) {
                        VersionHelper.instance().check(AppManager.getAppManager().currentActivity(), true);
                    }

                }  else if (msg.getType().equals(PushMessage.TYPE_CLEAR_APP_DATA)) {

                    //清除数据
                    handleClearDataCMD(context, msg);

                } else if (msg.getType().equals(PushMessage.TYPE_UPLOAD_PUSHID)) {

                    handleUploadPushIdCMD(context, msg);

                } else {
                    DLog.w(TAG, "未定义的消息类型");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理上传pushId命令
     * @param msg
     */
    private void handleUploadPushIdCMD(Context context, PushMessage msg) {

        String pushId = AppConfig.getInstance().getPushId(context);

        if (TextUtils.isEmpty(pushId)) {
            JPushInterface.getRegistrationID(context);
        } else {
            UserHelper.getInstance().uploadPushId(context, pushId);
        }
    }

    /**
     * 清除数据
     *
     * @param context
     * @param msg
     */
    private void handleClearDataCMD(final Context context, PushMessage msg) {
        new Thread() {
            public void run() {

                try {
                    AppConfig.removeUser(context);
                    AQUtility.cleanCacheAsync(context, 0, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 显示消息
     *
     * @param context
     */
    public void showNotification(Context context, String title, String digest, String type, String contentId) {

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent targetIntent = null;

        if (type.equals(PushMessage.TYPE_ORDER_STATUS) || type.equals(PushMessage.TYPE_ORDER_NEW) || type.equals(PushMessage.TYPE_HANG_OFF)) {

            //订单状态
            targetIntent = new Intent(context, OrderDetailActivity.class);
            targetIntent.putExtra("id", contentId);

        } else if (type.equals(PushMessage.TYPE_LEARNING)) {

            //推送消息是广告
            targetIntent = new Intent(context, WebViewActivity.class);
            targetIntent.putExtra(WebViewActivity.KEY_TITLE, "");
            targetIntent.putExtra(WebViewActivity.KEY_URL, digest);

        }else if (type.equals(PushMessage.TYPE_ORDER_REWARD)||
                type.equals(PushMessage.TYPE_RECHARGE)||
                type.equals(PushMessage.TYPE_GET_MONEY)||
                type.equals(PushMessage.TYPE_ORDER_BACK_MONEY)||
                type.equals(PushMessage.TYPE_RECEIVE_STAR)) {

            targetIntent = new Intent(context, MyAccountActivity.class);

        }else if(type.equals(PushMessage.TYPE_SYSTEM_MSG)){
            targetIntent = new Intent(context, MainActivity.class);
        }

        int requestCode = (int) SystemClock.uptimeMillis();

        PendingIntent contentIntent = null;

        if (targetIntent != null) {

            if (AppManager.isMainActivityRunning() || WelcomeActivity.isRunning) {
                targetIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                //程序运行中，弹出消息防止闪屏
                contentIntent = PendingIntent.getActivity(context, requestCode ,targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            } else {
                targetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                //程序未运行，弹出消息后，退出消息详情返回主页面
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

                if (type.equals(PushMessage.TYPE_ORDER_STATUS) ||
                        type.equals(PushMessage.TYPE_ORDER_NEW) ||
                        type.equals(PushMessage.TYPE_HANG_OFF)) {

                    // Adds the back stack
                    stackBuilder.addParentStack(OrderDetailActivity.class);

                } else if (type.equals(PushMessage.TYPE_LEARNING)) {
                    // Adds the back stack
                    stackBuilder.addParentStack(WebViewActivity.class);

                }else if (type.equals(PushMessage.TYPE_ORDER_REWARD)||
                        type.equals(PushMessage.TYPE_RECHARGE)||
                        type.equals(PushMessage.TYPE_GET_MONEY)||
                        type.equals(PushMessage.TYPE_ORDER_BACK_MONEY)) {

                    stackBuilder.addParentStack( MyAccountActivity.class);

                }else if(type.equals(PushMessage.TYPE_SYSTEM_MSG)){
                    stackBuilder.addParentStack( MainActivity.class);
                }

                stackBuilder.addNextIntent(targetIntent);

                contentIntent = stackBuilder.getPendingIntent(requestCode, PendingIntent.FLAG_UPDATE_CURRENT);

            }
        }

        title = title.replaceAll("&quot;", "\"");

        if (!AppManager.isMainActivityRunning() && !AppConfig.getInstance().isPushMsgOfflineEnabled()) {
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(android.os.Build.VERSION.SDK_INT > 20 ? R.drawable.ic_launcher : R.drawable.ic_launcher);
        builder.setColor(context.getResources().getColor(R.color.colorPrimary));
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
        builder.setTicker(title);
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setContentTitle(Html.fromHtml(title));
        builder.setContentText(Html.fromHtml(digest));

        if (contentIntent != null)
            builder.setContentIntent(contentIntent);

        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(digest));

        Notification notification = builder.build();

        manager.notify(requestCode, notification);

    }


}
