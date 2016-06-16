package com.lsm.barrister.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;

public class ShakeDetector implements SensorEventListener {
    /**
     * 检测的时间间隔
     */
    static final int UPDATE_INTERVAL = 600;
    private static final String TAG = "ShakeDetector";
    /**
     * 上一次检测的时间
     */
    long mLastUpdateTime;

    /**
     * 上一次检测时，加速度在x、y、z方向上的分量，用于和当前加速度比较求差。
     */
    float mLastX, mLastY, mLastZ;
    Context mContext;
    SensorManager mSensorManager;
    ArrayList<OnShakeListener> mListeners;

    public ShakeDetector(Context context) {
        mContext = context;
        mSensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
        mListeners = new ArrayList<OnShakeListener>();
    }

    /**
     * 当摇晃事件发生时，接收通知
     */
    public interface OnShakeListener {
        /**
         * 当手机摇晃时被调用
         */
        void onShake();
    }

    /**
     * 注册OnShakeListener，当摇晃时接收通知
     *
     * @param listener
     */
    public void registerOnShakeListener(OnShakeListener listener) {
        if (mListeners.contains(listener))
            return;
        mListeners.add(listener);
    }

    /**
     * 移除已经注册的OnShakeListener
     *
     * @param listener
     */
    public void unregisterOnShakeListener(OnShakeListener listener) {
        mListeners.remove(listener);
    }

    /**
     * 启动摇晃检测
     */
    public void start() {
        if (mSensorManager == null) {
            Log.e(TAG, "getSensorManager failed");
            throw new UnsupportedOperationException();
        }
        Sensor sensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor == null) {
            Log.e(TAG, "getDefaultSensor failed");
            throw new UnsupportedOperationException();
        }
        boolean success = mSensorManager.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_GAME);
        if (!success) {
            Log.e(TAG, "registerListener failed");
            throw new UnsupportedOperationException();
        }
    }

    /**
     * 停止摇晃检测
     */
    public void stop() {
        if (mSensorManager != null)
            mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        long currentTime = System.currentTimeMillis();
        long diffTime = currentTime - mLastUpdateTime;

        if (diffTime < UPDATE_INTERVAL)
            return;

        mLastUpdateTime = currentTime;

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        mLastX = x;
        mLastY = y;
        mLastZ = z;

        int sensorType = event.sensor.getType();
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            int value = 15;// 摇一摇阀值,不同手机能达到的最大值不同,如某品牌手机只能达到20
            if (x >= value || x <= -value || y >= value || y <= -value || z >= value || z <= -value) {
                notifyListeners();
            }
        }

    }

    /**
     * 当摇晃事件发生时，通知所有的listener
     */
    private void notifyListeners() {
        for (OnShakeListener listener : mListeners) {
            listener.onShake();
        }
    }
}