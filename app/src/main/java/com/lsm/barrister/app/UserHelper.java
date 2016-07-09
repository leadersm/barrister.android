package com.lsm.barrister.app;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.lsm.barrister.data.entity.Account;
import com.lsm.barrister.data.entity.User;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;
import com.lsm.barrister.data.io.app.GetMyAccountReq;
import com.lsm.barrister.data.io.app.UpdateUserInfoReq;

import java.util.ArrayList;
import java.util.HashMap;
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
        void onUpdateUser();

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

    public void notifyUpdateUser() {
        for (UserActionListener temp : actionListeners) {
            temp.onUpdateUser();
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
     * @param context
     * @param pushId
     */
    public void uploadPushId(Context context, String pushId) {
        User user = AppConfig.getUser(context);
        if (user != null) {

            HashMap<String,String> params = new HashMap<>();
            params.put("pushId",pushId);

            new UpdateUserInfoReq(context,params).execute(new Action.Callback<IO.GetUpdateUserResult>() {
                @Override
                public void progress() {

                }

                @Override
                public void onError(int errorCode, String msg) {

                }

                @Override
                public void onCompleted(IO.GetUpdateUserResult result) {

                }
            });
        }
    }

    List<OnAccountUpdateListener> onAccountUpdateListeners = new ArrayList<>();
    public void addOnAccountUpdateListener(OnAccountUpdateListener listener){
        if(!onAccountUpdateListeners.contains(listener)){
            onAccountUpdateListeners.add(listener);
        }
    }

    public void removeAccountListener(OnAccountUpdateListener listener){
        if(onAccountUpdateListeners.contains(listener)){
            onAccountUpdateListeners.remove(listener);
        }
    }

    public Account getAccount() {
        return mAccount;
    }

    public void setAccount(Account account) {
        if(account!=null){
            mAccount = null;
            mAccount = account;
        }
    }

    Account mAccount;

    public void updateAccount(){

        if(mAccount==null)
            return;

        for(OnAccountUpdateListener listener:onAccountUpdateListeners){
            listener.onUpdateAccount(mAccount);
        }
    }

    public interface OnAccountUpdateListener {
        public void onUpdateAccount(Account account);
    }

    public void syncAccount(Activity context){
        new GetMyAccountReq(context).execute(new Action.Callback<IO.GetAccountResult>() {
            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {

            }

            @Override
            public void onCompleted(IO.GetAccountResult result) {
                setAccount(result.account);
                updateAccount();
            }
        });
    }

}
