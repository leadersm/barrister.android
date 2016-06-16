package com.lsm.barrister.data.io;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.androidquery.util.AQUtility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lsm.barrister.app.AppConfig;
import com.lsm.barrister.app.Constants;
import com.lsm.barrister.app.VersionHelper;
import com.lsm.barrister.data.entity.User;
import com.lsm.barrister.ui.activity.BaseActivity;
import com.lsm.barrister.utils.DLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import okhttp3.Call;

public abstract class Action {


    public static final int POST = 1;
    public static final int GET = 2;

    public String TAG = getName();

    public Action(Context context) {
        this.context = context;
    }

    public HashMap<String, String> params = new HashMap<>();

    public HashMap<String, File> files = new HashMap<>();

    public void params(String key, String value) {
        params.put(key, value);
    }

    public void addFile(String key, File file) {
        files.put(key, file);
    }

    int retry = 0;

    public Action setRetry(int retry) {
        this.retry = retry;
        return this;
    }

    private long expire = 0;

    public Action setExpire(long expire) {
        this.expire = expire;
        return this;
    }

    private boolean loading = false;

    public boolean isLoading() {
        return loading;
    }

    public abstract String getName();

    public abstract String url();//请求url

    public abstract CommonResult parse(String json) throws Exception;//解析

    public abstract int method();

    /**
     * Title: Request.java
     * Description:请求回调
     *
     * @param <T>
     * @author lsm
     * @date 2015-5-13
     */
    public interface Callback<T> {

        void progress();

        void onError(int errorCode, String msg);

        void onCompleted(T t);
    }


    public Callback callback;

    public Context context;

    RequestCall mCall;

    /**
     * 执行请求
     *
     * @param callback
     */
    public <T> void execute(final Callback<T> callback) {

        this.callback = callback;

        loading = true;

        if (callback != null) {

            try {
                callback.progress();
            } catch (Exception e) {

            }
        }

        final String url = url();

        for(String key:params.keySet()){
            DLog.d(getName(),"key:"+key+",value:"+params.get(key));
        }

        if (method() == POST) {
            PostFormBuilder builder = OkHttpUtils.post().url(url).headers(getHeaders(context)).params(this.params);
            if(!files.isEmpty()){
                for(String key:files.keySet()){
                    builder.addFile(key, files.get(key).getName(),files.get(key));
                }
            }
            mCall = builder.build();
        } else {
            mCall = OkHttpUtils.get().url(url).headers(getHeaders(context)).params(params).build();
        }

        final long startTime = System.currentTimeMillis();

        mCall.execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                loading = false;

                DLog.e(TAG, "url:" + url);
                DLog.e(TAG, e.getMessage());
                DLog.v(TAG, "====" + getName() + "=========本次连接耗时" + getDuration(startTime) + "毫秒============");

                callback.onError(ErrorCode.ERROR_NETWORK, e.getMessage());

            }

            @Override
            public void onResponse(String s) {

                loading = false;
                DLog.d(TAG, "url:" + url());
                DLog.i(TAG, s);
                DLog.v(TAG, "====" + getName() + "=========本次连接耗时" + getDuration(startTime) + "毫秒============");

                new ParseTask<T>(s, callback).execute();

            }
        });

    }


    private long getDuration(long startTime) {
        return System.currentTimeMillis() - startTime;
    }

    public void cancel() {
        if (mCall != null) {
            DLog.d(TAG, getName() + ".取消请求!!!");
            mCall.cancel();
        }
    }

    public static HashMap<String, String> headers = new HashMap<>();

    public static HashMap<String, String> getHeaders(Context context) {
        //deviceId，设备唯一标识
        headers.put("X-DEVICE-NUM", Constants.deviceId);
//		应用版本:X-VERSION
        String versionName = VersionHelper.instance().getVersionName();
        headers.put("X-VERSION", "android-" + versionName);
//		设备型号:X-TYPE
        headers.put("X-TYPE", Build.MODEL);
//		系统:X-SYSTEM
        headers.put("X-SYSTEM", "android");
//		系统:X-SYSTEM-VERSION
        headers.put("X-SYSTEM-VERSION", String.valueOf(Build.VERSION.SDK_INT));
        //屏幕大小 1280*720
        headers.put("X-SCREEN", Constants.screenSize);
        //渠道，IOS填写 appleStore
        headers.put("X-MARKET", String.valueOf(Constants.MARKET));
//		userId:X-USERID INTEGER  登陆后调用
        User user = AppConfig.getUser(context);
        if (user != null) {
            headers.put("X-UID", user.getId());
        }

//        String cookie = AppConfig.getInstance().getCookie();
//
//        if (!TextUtils.isEmpty(cookie)) {
//
//            headers.put("Cookie", cookie);
//
//            headers.put("sessionId", cookie);
//        }

        return headers;
    }


    /**
     * 字符串url编码
     *
     * @param s
     * @return
     */
    public static String encode(String s) {
        try {
            s = URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return s;
    }


    /**
     * Title: Request.java
     * Description: 解析任务
     *
     * @param <T>
     * @author lsm
     * @date 2015-5-5
     */
    class ParseTask<T> extends AsyncTask<Void, Void, CommonResult> {

        String json;
        Callback<T> callback;

        public ParseTask(String json, Callback<T> callback) {
            super();
            this.json = json;
            this.callback = callback;
        }

        @Override
        protected CommonResult doInBackground(Void... arg0) {


            try {

                CommonResult commonResult = parse(json);

                return commonResult;

            } catch (Exception e) {

                e.printStackTrace();

            } finally {

                json = null;
                System.gc();

            }

            return null;
        }

        @Override
        protected void onPostExecute(CommonResult result) {
            super.onPostExecute(result);


            try {

                if (result != null) {

                    if (result.resultCode != 200) {
                        callback.onError(result.resultCode, result.resultMsg);
                        return;
                    }

                } else {
                    callback.onError(ErrorCode.ERROR_UNKNOWN, "未知错误");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public <T> T getFromGson(String json, TypeToken<T> typeToken) {
        Gson gson = new Gson();
        return gson.fromJson(json, typeToken.getType());
    }

    public int getTotalPage(int total, int pageSize) {
        return (int) Math.ceil((float) total / pageSize);
    }


    /**
     * 公共返回值
     */
    public static class CommonResult {
        public int resultCode;
        public String resultMsg;
    }


    /**
     * 安全回调
     *
     * @param t
     */
    public void onSafeCompleted(final Object t) {

        //Activity 触发的请求并且Activity已经关闭则不再传递数据，释放Activity
        if (context != null && context instanceof BaseActivity) {

            BaseActivity mActivity = (BaseActivity) context;

            if (mActivity.isFinished()) {

                context = null;

                return;
            }
        }

        AQUtility.post(new Runnable() {
            @Override
            public void run() {
                try {
                    callback.onCompleted(t);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void addUserParams() {


        User user = AppConfig.getUser(context);
        if(user==null){
            System.err.println("未登录，无法提交请求。。。");
            return;
        }

        params("userId", user.getId());
        params("verifyCode", user.getVerifyCode());
        DLog.d(TAG,"userId:"+user.getId()+",verifyCode:"+user.getVerifyCode());

    }

    public CommonResult parseCommonResult(String json) throws Exception {
        final Action.CommonResult result = getFromGson(json, new TypeToken<Action.CommonResult>() { });

        if (result != null) {

            if (result.resultCode == 200) {

                onSafeCompleted(true);

            }

            return result;

        } else {
            throw new Exception("解析错误");
        }
    }

}
