package com.spirited.support.application.observer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * 网络变化状态广播注册器
 */

public class NetworkState extends Subject<NetworkStateObserver> {
    private NetworkStateReceiver receiver = null;

    public NetworkState() {
        receiver = new NetworkStateReceiver();
    }

    public NetworkStateReceiver getReceiver() {
        return receiver;
    }

    private class NetworkStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                //获取联网状态的NetworkInfo对象
                NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (info != null) {
                    //如果当前的网络连接成功并且网络连接可用
                    if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                            for (NetworkStateObserver observer : observers) {
                                observer.wifiNetConnect();
                            }
                        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                            for (NetworkStateObserver observer : observers) {
                                observer.mobileNetConnect();
                            }
                        }
                    } else {
                        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                            for (NetworkStateObserver observer : observers) {
                                observer.wifiNetDisConnect();
                            }
                        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                            for (NetworkStateObserver observer : observers) {
                                observer.mobileNetDisConnect();
                            }
                        }
                    }
                }
            }
        }
    }
}