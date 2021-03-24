package com.spirited.support.utils;

/**
 * auth数据工具类
 */

public class AuthUtil {

    //保存token值
    public static void saveAuth(String token) {
        SharePreferences.putString("OAuth", token);
    }

    //获取token值
    public static String getAuth() {
        return SharePreferences.getStringWithDefault("OAuth", "");
    }
}
