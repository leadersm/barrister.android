package com.lsm.barrister.utils;

import android.util.Log;

import com.lsm.barrister.app.Constants;


public class DLog {

    public static void i(String tag, String s) {
        if (Constants.DEBUG) Log.i(tag, "" + s);
    }

    public static void d(String tag, String s) {
        if (Constants.DEBUG) Log.d(tag, "" + s);
    }

    public static void e(String tag, String s) {
        if (Constants.DEBUG) Log.e(tag, "" + s);
    }

    public static void v(String tag, String s) {
        if (Constants.DEBUG) Log.v(tag, "" + s);
    }

    public static void w(String tag, String s) {
        if (Constants.DEBUG) Log.w(tag, "" + s);
    }


    public static void printResult(String TAG, String url, String object) {
        v(TAG, "-------------------------------");
        i(TAG, "url:" + url);
        i(TAG, "result:" + object);
    }
}
