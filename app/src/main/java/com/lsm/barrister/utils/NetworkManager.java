package com.lsm.barrister.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.lsm.barrister.app.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class NetworkManager {

    private static final String TAG = NetworkManager.class.getSimpleName();

    public static final int TYPE_WIFI = 0x01;
    public static final int TYPE_CMWAP = 0x02;
    public static final int TYPE_CMNET = 0x03;

    public interface NetworkListener {

        void onNetworkDisconnect();

        void onNetworkConnected(int type);

    }

    List<NetworkListener> listeners = new ArrayList<NetworkListener>();

    public void addOnNetworkListener(NetworkListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    public void removeNetworkListener(NetworkListener listener) {
        if (listeners.contains(listener))
            listeners.remove(listener);
    }

    Context context;

    private NetworkManager(Context context) {
        this.context = context;
    }

    private static NetworkManager instance = null;

    public static boolean enable = false;

    public static NetworkManager getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkManager(context);
        }
        return instance;
    }

    public void init() {
        if (isNetworkConnected(context) && getNetworkType() != 0) {
            enable = true;
        }
        readLocalMacAddress();
    }


    public void readLocalMacAddress() {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        Constants.mac = info.getMacAddress();
    }

    /**
     * 网络是否可用
     *
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络   2：WAP网络    3：NET网络
     */
    public int getNetworkType() {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!TextUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase(Locale.getDefault()).equals("cmnet")) {
                    netType = TYPE_CMNET;
                } else {
                    netType = TYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = TYPE_WIFI;
        }
        return netType;
    }

    public boolean isWifi() {

        return getNetworkType() == TYPE_WIFI;

    }

    NetworkChangedReceiver ncr = new NetworkChangedReceiver();

    public void registerNetworkReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        context.registerReceiver(ncr, filter);
    }

    public void unregisterNetworkReceiver(Context context) {
        context.unregisterReceiver(ncr);
    }


    /**
     * 网络状态接收器
     *
     * @author louis.lv
     */
    class NetworkChangedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            DLog.d(TAG, action);
            if (getNetworkType() == 0) {
                NetworkManager.enable = false;
                DLog.d(TAG, "没有网络");
                for (NetworkListener lis : listeners) {
                    lis.onNetworkDisconnect();
                }
                return;
            }

            if (getNetworkType() == 1) {
                wifiAction(intent, action);
            } else {
                wapAction(intent, action);
            }

        }

        public void wapAction(Intent intent, String action) {
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                //如果是在开启wifi连接和有网络状态下
                if (NetworkInfo.State.CONNECTED == info.getState()) {
                    //连接状态
                    DLog.e(TAG, "有网络连接");
                    NetworkManager.enable = true;
                    for (NetworkListener lis : listeners) {
                        lis.onNetworkConnected(getNetworkType());
                    }
                } else {
                    DLog.e(TAG, "无网络连接");
                    NetworkManager.enable = false;

                    for (NetworkListener lis : listeners) {
                        lis.onNetworkDisconnect();
                    }
                }
            }
        }

        public void wifiAction(Intent intent, String action) {
            if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (info != null) {
                    DLog.v(TAG, info.getDetailedState().toString());
                    if (info.getDetailedState() == DetailedState.CONNECTED) {
                        NetworkManager.enable = true;
                        DLog.d(TAG, "网络已连接");

                        //获取更新--网络断开后没有到达的消息通过这个方法获取

                        //开启推送服务

                        for (NetworkListener lis : listeners) {
                            lis.onNetworkConnected(getNetworkType());
                        }
                    }
                }
            } else if (action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
                DetailedState state = WifiInfo.getDetailedStateOf((SupplicantState) intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE));
                DLog.i(TAG, state.toString());
                if (state == DetailedState.DISCONNECTED) {//切换网络
                    NetworkManager.enable = false;
                    DLog.d(TAG, "网络断开");
                    for (NetworkListener lis : listeners) {
                        lis.onNetworkDisconnect();
                    }
                }
            } else if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {//开关wifi
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
                    NetworkManager.enable = false;
                    DLog.d(TAG, "关闭网络");
                    //发送通知给activity 显示toast提示
                    for (NetworkListener lis : listeners) {
                        lis.onNetworkDisconnect();
                    }
                }
            }
        }

    }
}
