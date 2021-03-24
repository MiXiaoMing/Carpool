package com.spirited.support.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络连接工具类
 */
public class NetUtil {

    // 检查是否 连接网络
    public static boolean isConnect(Context context) {
        return checkNet(context, ConnectivityManager.TYPE_WIFI) || checkNet(context, ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * 检查是否 连接网络
     */
    private static boolean checkNet(Context context, int type) { // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivity = (ConnectivityManager) context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        }

        // 获取网络连接管理的对象
        NetworkInfo info = connectivity.getNetworkInfo(type);

        if (info != null && info.getState().equals(NetworkInfo.State.CONNECTED)) {
            return true;
        }
        return false;
    }
}
