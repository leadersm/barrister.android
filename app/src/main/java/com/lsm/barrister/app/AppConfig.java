package com.lsm.barrister.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.os.Environment;


import com.lsm.barrister.data.entity.User;
import com.lsm.barrister.utils.ComplexPreferences;
import com.lsm.barrister.utils.DLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AppConfig {

    private static final String TAG = AppConfig.class.getSimpleName();

    private static AppConfig appConfig;

    // file name
    private static final String SP_NAME = "jjrb.config";
    // 版本
    public static final String VERSION = "version";
    // 设备登记
    public static final String DEVICE_REGISTERED = "device_registered";
    // 夜间模式
    public static final String DARK_THEME = "dark_theme?";
    // 非wifi状况不加载图�?
    public static final String NO_IMAGE = "no_wifi_no_image?";
    // 自动�?��更新
    public static final String AUTO_CHECK_UPDATE = "auto_check_update?";
    // 正文字号大小 �?-�?-�?
    public static final String NEWS_TEXT_SIZE = "news_text_size";
    // �?��后是否接受推送消�?
    public static final String MODE_PUSH_MSG_OFFLINE_ENABLED = "push_offline_enable";
    // apk 下载地址
    public static final String APK_URL = "apk_url";
    // 用户信息
    private static final String USER_PREFS = "userPrefs";
    // 推�?id
    private static final String PUSH_REG_ID = "push_id";

    public static final String TEXT_SIZE_SMALL = "small";
    public static final String TEXT_SIZE_MIDDLE = "middle";
    public static final String TEXT_SIZE_LARGE = "large";

    private static final String TEXT_SIZE = "text_size";

    private static final String COOKIE = "cookie";
    private static final String COOKIE_USER = "u.name";
    private static final String COOKIE_PWD = "u.key";
    private static final String COOKIE_USER_TICKET = "u.ticket";// 用于第三方登�?

    private static final String FORCE_UPDATE = "forceUpdate";

    private static final String BATCH_NUM = "batchNum";

    private static final String UNREAD_MSG_COUNT = "unreadMsgCount";


    Context context;

    public void init(Context context) {

        this.context = context;

        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }

        if (editor == null) {
            editor = sp.edit();
        }
    }

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public static AppConfig getInstance() {
        if (appConfig == null) {
            appConfig = new AppConfig();
        }
        return appConfig;
    }

    public static boolean isSDCardEnabled() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static File getDir(String name) {

        File dir = new File(Environment.getExternalStorageDirectory(), name);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 消息是否toast提示
     *
     * @return
     */
    public boolean isNotificationToastEnabled() {
        return false;
    }

    /**
     * 接到消息是否发出声音
     *
     * @return
     */
    public boolean isNotificationSoundEnabled() {
        return true;
    }

    /**
     * 接到消息是否震动
     *
     * @return
     */
    public boolean isNotificationVibrateEnabled() {
        return true;
    }

    /**
     * 清空数据�?
     *
     * @param context
     */
    public static void cleanDatabases(Context context) {

    }

    /**
     * 判断当前系统声音是否为正常模�?
     *
     * @return
     */
    public boolean isAudioNormal(Context context) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
    }

    /**
     * 判断当前版本是否兼容目标版本
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    /**
     * 获取app信息
     *
     * @param context
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 获取版本�?
     *
     * @param context
     * @return
     */
    public int getSavedVersionCode(Context context) {
        return sp.getInt(DEVICE_REGISTERED, 0);
    }

    /**
     * 保存版本�?
     *
     * @param context
     * @param version
     */
    public void saveVersionCode(int version) {
        editor.putInt(DEVICE_REGISTERED, version).commit();
    }


    /**
     * 保存APK url
     *
     * @param url
     */
    public void saveApkUrl(String url) {
        editor.putString(APK_URL, url).commit();
    }

    /**
     * 获取APK url地址
     *
     * @return
     */
    public String getApkUrl() {
        if (sp.contains(APK_URL))
            return sp.getString(APK_URL, "");
        return "";
    }





    /**
     * 是否自动�?��更新
     *
     * @param ctx
     * @return
     */
    public boolean isAutoCheckUpdate(Context ctx) {
        return sp.getBoolean(AUTO_CHECK_UPDATE, false);
    }

    public void setAutoCheckUpdate(boolean b) {
        sp.edit().putBoolean(AUTO_CHECK_UPDATE, b).commit();
    }

    /**
     * 获取正文文字大小
     *
     * @param ctx
     * @return
     */
    public int getNewsTextSize(Context ctx) {
        return sp.getInt(NEWS_TEXT_SIZE, 1);
    }

    /**
     * 设置正文文字大小
     *
     * @param ctx
     * @param size
     */
    public void setNewsTextSize(Context ctx, int size) {
        sp.edit().putInt(NEWS_TEXT_SIZE, size).commit();
    }

    /**
     * 离线是否推�?消息
     *
     * @param ctx
     * @return
     */
    public boolean isPushMsgOfflineEnabled() {
        return sp.getBoolean(MODE_PUSH_MSG_OFFLINE_ENABLED, true);
    }

    /**
     * 设置离线推�?消息�?��
     *
     * @param ctx
     * @param b
     */
    public void setPushMsgOfflineEnabled(boolean b) {
        sp.edit().putBoolean(MODE_PUSH_MSG_OFFLINE_ENABLED, b).commit();
    }

    /**
     * �?��设备是否登记
     */
    public boolean isDeviceRegistered() {
        return sp.contains(DEVICE_REGISTERED) ? false : sp.getBoolean(DEVICE_REGISTERED, false);
    }

    /**
     * 设置设备注册标志
     *
     * @param ctx
     * @param b
     */
    public void setDeviceRegistered(boolean b) {
        sp.edit().putBoolean(DEVICE_REGISTERED, b).commit();
    }

    /**
     * 保存推�?id
     *
     * @param ctx
     * @param regId
     */
    public void setPushId(String regId) {
        sp.edit().putString(PUSH_REG_ID, regId).commit();
    }

    /**
     * 获取推�?id
     *
     * @param ctx
     * @return
     */
    public String getPushId(Context ctx) {
        return sp.getString(PUSH_REG_ID, null);
    }

    /**
     * 获取正文字体大小
     *
     * @param ctx
     * @return
     */
    public String getTextSize(Context ctx) {
        return sp.getString(TEXT_SIZE, TEXT_SIZE_MIDDLE);
    }

    /**
     * 设置正文字体大小
     *
     * @param ctx
     * @param TEXTSIZE
     */
    public void setTextSize(Context ctx, String TEXTSIZE) {
        DLog.d(TAG, "setTextSize:" + TEXTSIZE);
        sp.edit().putString(TEXT_SIZE, TEXTSIZE).commit();
    }

    /**
     * 保存Cookie
     *
     * @param cookie
     */
    public void saveCookie(String cookie) {
        DLog.d(TAG, "saveCookie:" + cookie);
        sp.edit().putString(COOKIE, cookie).commit();
    }

    /**
     * 获取Cookie
     *
     * @param ctx
     * @return
     */
    public String getCookie() {
        return sp.getString(COOKIE, "");
    }

    /**
     * 登陆成功后保存密码md5Cookie
     *
     * @param md5pwd
     */
    public void saveUserPwdCookie(String md5pwd) {
        DLog.d(TAG, "saveUserPwdCookie:" + md5pwd);
        editor.putString(COOKIE_PWD, md5pwd).commit();
    }

    /**
     * 获取密码MD5Cookie
     *
     * @param context
     */
    public String getUserPwdCookie() {
        return sp.getString(COOKIE_PWD, null);
    }

    /**
     * 保存第三方id
     *
     * @param context
     */
    public void saveCookieTicketKey(String ticket) {
        DLog.d(TAG, "saveCookieTicketKey:" + ticket);
        editor.putString(COOKIE_USER_TICKET, ticket).commit();

    }

    /**
     * 获取第三方id
     *
     * @param context
     * @return
     */
    public String getCookieTicketKey() {
        return sp.getString(COOKIE_USER_TICKET, null);
    }

    public String get(String key) {
        return sp.getString(key, null);
    }

    public void set(String key, String value) {
        sp.edit().putString(key, value).commit();
    }

    /**
     * 设置强制更新
     *
     * @param context
     * @param b
     */
    public void setForceUpdate(boolean b) {
        editor.putBoolean(FORCE_UPDATE, b).commit();
    }

    /**
     * 是否强制更新
     *
     * @param context
     * @return
     */
    public boolean isForceUpdate(Context context) {
        return sp.getBoolean(FORCE_UPDATE, false);
    }

    /**
     * 移除
     *
     * @param context
     * @param key
     */
    public void remove(String key) {

        if (sp.contains(key)) {
            editor.remove(key).commit();
        }

    }

    public void saveNewsBatchNumber(String batchNumber) {
        sp.edit().putString(BATCH_NUM, batchNumber).commit();
    }

    public String getNewsBatchNumber() {
        return sp.getString(BATCH_NUM, null);
    }

    public void saveStartTime(String startTime) {
        // TODO Auto-generated method stub
        sp.edit().putString("startTime", startTime).commit();

    }

    public String getStartTime() {
        return sp.getString("startTime", null);
    }

    public void saveEndTime(String endTime) {
        // TODO Auto-generated method stub
        sp.edit().putString("endTime", endTime).commit();

    }

    public String getEndTime() {
        return sp.getString("endTime", null);
    }

    public void clearStartTime() {
        sp.edit().remove("startTime").remove("endTime").commit();
    }

    /**
     * 设置用户配置信息
     *
     * @param context
     * @param user
     */
    public static void setUser(Context context, User user) {
        ComplexPreferences cp = ComplexPreferences.getComplexPreferences(context, USER_PREFS, Context.MODE_PRIVATE);
        cp.putObject("user", user);
        cp.commit();
    }

    /**
     * 获取用户配置信息
     *
     * @param context
     * @return
     */
    public static User getUser(Context context) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(context, USER_PREFS,
                Context.MODE_PRIVATE);
        User user = complexPreferences.getObject("user", User.class);
        return user;
    }

    public static void removeUser(Context context) {

        ComplexPreferences cp = ComplexPreferences.getComplexPreferences(context, USER_PREFS, Context.MODE_PRIVATE);

        cp.remove("user");

        cp.commit();

    }

    /**
     * 增加未读消息数
     */
    public void increaseUnreadCount() {
        int unread = getUnreadMsgCount();
        sp.edit().putInt(UNREAD_MSG_COUNT, unread + 1).commit();
    }

    /**
     * 重置0
     */
    public void resetUnreadMsgCount() {
        sp.edit().putInt(UNREAD_MSG_COUNT, 0).commit();
    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    public int getUnreadMsgCount() {
        return sp.getInt(UNREAD_MSG_COUNT, 0);
    }


    public void initBankIcons() {
        try {

            Document doc = Jsoup.parse(AssetsUtils.loadText(context, Constants.DOC_BANKS));
            List<Element> bankEs = doc.getElementsByTag("Bank");

            if(bankEs!=null)
                for(Element e : bankEs){

                    Bank bank = new Bank();

                    String name = e.attr("name");
                    String pinyin = e.attr("pinyin");
                    String value = e.ownText();

                    bank.icon = value;
                    bank.name = name;
                    bank.pinyin = pinyin;

                    banks.add(bank);

                    System.out.println("====>"+bank.toString());
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Bank> getBanks() {
        return banks;
    }

    public List<Bank> banks = new ArrayList<>();

    public static class Bank{
        public String name;
        public String pinyin;
        public String icon;

        @Override
        public String toString() {
            return "Bank{" +
                    "name='" + name + '\'' +
                    ", pinyin='" + pinyin + '\'' +
                    ", icon='" + icon + '\'' +
                    '}';
        }
    }
}
