package com.spirited.support.application;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

import com.spirited.support.application.observer.ActivityLifecycle;
import com.spirited.support.application.observer.NetworkState;
import com.spirited.support.logger.Logger;


/**
 * afapplication基类，继承与application
 * 实现了application applicationContext静态化
 */

public class AFApplication extends Application {
    private static final String TAG = "AFApplication ";

    public static AFApplication application = null;
    public static Context applicationContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.getLogger().e(TAG + "onCreate.");
        application = (AFApplication) getApplicationContext();
        applicationContext = getApplicationContext();

        // 初始化网络状态广播
        initNetworkConnect();
        // 初始化页面生命周期
        initActivityLifecycle();
    }

    /**
     * 注册网络状态变化广播
     */
    private void initNetworkConnect() {
        NetworkState networkState = new NetworkState();

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkState.getReceiver(), filter);
    }

    /**
     * 监听页面生命周期回调
     */
    private void initActivityLifecycle() {
        ActivityLifecycle activityLifecycle = new ActivityLifecycle();

        registerActivityLifecycleCallbacks(activityLifecycle.callbacks);
    }
}
