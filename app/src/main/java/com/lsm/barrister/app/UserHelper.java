package com.lsm.barrister.app;

import android.content.Context;
import android.os.AsyncTask;

import com.lsm.barrister.data.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户帮助类，用户相关操作，登出，改变用户头像……回调
 *
 * @author lsm
 */
public class UserHelper {


    private static UserHelper instance = null;

    private UserHelper() {

    }

    public static UserHelper getInstance() {
        if (instance == null) {
            instance = new UserHelper();
        }
        return instance;
    }

    public void init(Context context) {
        mUser = getUser(context);
    }

    /**
     * 同步用户信息
     *
     * @param context
     */
    public static void sync(Context context) {
        syncFavoriteNews(context);
        syncFavoritePosts(context);
        syncFavoriteChannels(context);
    }

    List<UserActionListener> actionListeners = new ArrayList<UserActionListener>();

    public void addOnUserActionListener(UserActionListener listener) {
        if (!actionListeners.contains(listener)) {
            actionListeners.add(listener);
        }
    }


    /**
     * 用户普通登录，第三方登录，注销，上传头像回调接口
     *
     * @author lsm
     */
    public interface UserActionListener {

        /**
         * 第三方登录回调
         *
         * @param user
         */
        void onSSOLoginCallback(User user);

        /**
         * 普通登陆回调
         *
         * @param user
         */
        void onLoginCallback(User user);

        /**
         * 登出
         */
        void onLogoutCallback();

        /**
         * 用户更换头像
         */
        void onUserIconNicknameChanged();

    }

    /**
     * Title: MainActivity.java
     * Description:登出任务，清除用户数据
     *
     * @author lsm
     * @date 2015-4-23
     */
    class LogoutTask extends AsyncTask<Void, Void, Void> {

        Context context;

        public LogoutTask(Context context) {
            super();
            this.context = context;

        }

        @Override
        protected Void doInBackground(Void... params) {

            mUser = null;

            // TODO 清除用户数据
            AppConfig.removeUser(context);
            //清除订阅号
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            for (UserActionListener temp : actionListeners) {
                temp.onLogoutCallback();
            }
        }

    }

    /**
     * 同步频道
     *
     * @param context
     */
    private static void syncFavoriteChannels(final Context context) {
        // TODO Auto-generated method stub
    }

    /**
     * 同步收藏跟帖
     *
     * @param context
     */
    private static void syncFavoritePosts(final Context context) {
        // TODO Auto-generated method stub
    }

    /**
     * 同步收藏新闻
     *
     * @param context
     */
    private static void syncFavoriteNews(final Context context) {
        // TODO Auto-generated method stub
    }

    public void clearListeners() {
        actionListeners.clear();
    }

    public void logout(Context context) {
        new LogoutTask(context).execute();
    }

    public void removeListener(UserActionListener listener) {
        if (actionListeners.contains(listener)) {
            actionListeners.remove(listener);
        }
    }

    public void notityUserIconOrNicknameChanged() {
        for (UserActionListener temp : actionListeners) {
            temp.onUserIconNicknameChanged();
        }
    }

    public void onLogin(User user) {

        for (UserActionListener temp : actionListeners) {
            temp.onLoginCallback(user);
        }
    }

    public void onSSOLoginCallback(User user) {

        for (UserActionListener temp : actionListeners) {
            temp.onSSOLoginCallback(user);
        }
    }

    User mUser;

    public User getUser(Context context) {

        if (mUser == null) {
            mUser = AppConfig.getUser(context);
        }

        return mUser;
    }

    /**
     * 上传PushId
     *
     * @param context
     * @param pushId
     */
    public void uploadPushId(Context context, String pushId) {
        User user = AppConfig.getUser(context);
        if (user != null) {

        }
    }

}
