package com.spirited.support.utils;

import android.content.Context;
import android.content.SharedPreferences;


import com.spirited.support.application.AFApplication;
import com.spirited.support.constants.Constants;

import java.io.Serializable;
import java.util.Set;


public class SharePreferences {

    /**
     * 获取sp句柄
     * @param storeName     存储名称
     * @return
     */
    private static SharedPreferences getSettingPreferences(String storeName) {
        SharedPreferences setting = null;
        setting = AFApplication.applicationContext.getSharedPreferences(storeName, Context.MODE_PRIVATE);
        return setting;
    }

    /*------------------- string -----------------------*/

    // 获取指定sp名称中相应key值
    private static String getString(String storeName, String key) {
        String value = null;
        getSettingPreferences(storeName).edit();
        if (getSettingPreferences(storeName).contains(key)) {
            value = getSettingPreferences(storeName).getString(key, null);
        }
        return value;
    }

    /**
     * 获取指定sp名称中相应key值 <br>
     * @param storeName
     * @param key
     * @param defaultValue      默认值
     * @return
     */
    private static String getString(String storeName, String key, String defaultValue) {
        String value = null;
        getSettingPreferences(storeName).edit();
        if (getSettingPreferences(storeName).contains(key)) {
            value = getSettingPreferences(storeName).getString(key, defaultValue);
        }else {
            value = defaultValue;
        }
        return value;
    }

    /**
     * 获取sp中key对应的值
     * 使用样例：<br>
     *     SPStore.getString("storeName")
     * @param key       key值
     * @return
     */
    public static String getString(String key) {
        return getString(Constants.sp_store_name, key);
    }

    public static String getStringWithDefault(String key, String defaultValue) {
        return getString(Constants.sp_store_name, key, defaultValue);
    }

    /**
     * 存放数据到指定sp中
     * @param storeName
     * @param key
     * @param value
     */
    private static void putString(String storeName, String key, Serializable value) {
        SharedPreferences.Editor edit = getSettingPreferences(storeName).edit();
        edit.putString(key, "" + value);
        edit.commit();
    }

    /**
     * 存放数据到sp中
     * @param key
     * @param value
     */
    public static void putString(String key, Serializable value) {
        putString(Constants.sp_store_name, key, value);
    }

    /*------------------- int -----------------------*/

    public static int getInt(String storeName, String key) {
        int value = -1;
        getSettingPreferences(storeName).edit();
        if (getSettingPreferences(storeName).contains(key)) {
            value = getSettingPreferences(storeName).getInt(key, -1);
        }
        return value;
    }

    public static int getInt(String storeName, String key, int defaultValue) {
        int value = -1;
        getSettingPreferences(storeName).edit();
        if (getSettingPreferences(storeName).contains(key)) {
            value = getSettingPreferences(storeName).getInt(key, defaultValue);
        }else {
            value = defaultValue;
        }
        return value;
    }

    public static int getInt(String key) {
        return getInt(Constants.sp_store_name, key);
    }

    public static int getInt(String key, int defaultValue) {
        return getInt(Constants.sp_store_name, key, defaultValue);
    }

    public static void putInt(String storeName, String key, int value) {
        SharedPreferences.Editor edit = getSettingPreferences(storeName).edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static void putInt(String key, int value) {
        putInt(Constants.sp_store_name, key, value);
    }

    /*------------------- long -----------------------*/

    public static long getLong(String storeName, String key) {
        long value = -1l;
        getSettingPreferences(storeName).edit();
        if (getSettingPreferences(storeName).contains(key)) {
            value = getSettingPreferences(storeName).getLong(key, -1l);
        }
        return value;
    }

    public static long getLong(String storeName, String key, long defaultValue) {
        long value = -1l;
        getSettingPreferences(storeName).edit();
        if (getSettingPreferences(storeName).contains(key)) {
            value = getSettingPreferences(storeName).getLong(key, defaultValue);
        }else {
            value = defaultValue;
        }
        return value;
    }

    public static long getLong(String key) {
        return getLong(Constants.sp_store_name, key);
    }

    public static long getLong(String key, long defaultValue) {
        return getLong(Constants.sp_store_name, key, defaultValue);
    }

    public static void putLong(String storeName, String key, long value) {
        SharedPreferences.Editor edit = getSettingPreferences(storeName).edit();
        edit.putLong(key, value);
        edit.commit();
    }

    public static void putLong(String key, long value) {
        putLong(Constants.sp_store_name, key, value);
    }

    /*------------------- float -----------------------*/

    public static float getFloat(String storeName, String key) {
        float value = -1f;
        getSettingPreferences(storeName).edit();
        if (getSettingPreferences(storeName).contains(key)) {
            value = getSettingPreferences(storeName).getFloat(key, -1f);
        }
        return value;
    }

    public static float getFloat(String storeName, String key, float defaultValue) {
        float value = -1f;
        getSettingPreferences(storeName).edit();
        if (getSettingPreferences(storeName).contains(key)) {
            value = getSettingPreferences(storeName).getFloat(key, defaultValue);
        }else {
            value = defaultValue;
        }
        return value;
    }

    public static float getFloat(String key) {
        return getFloat(Constants.sp_store_name, key);
    }

    public static float getFloat(String key, float defaultValue) {
        return getFloat(Constants.sp_store_name, key, defaultValue);
    }

    public static void putFloat(String storeName, String key, float value) {
        SharedPreferences.Editor edit = getSettingPreferences(storeName).edit();
        edit.putFloat(key, value);
        edit.commit();
    }

    public static void putFloat(String key, float value) {
        putFloat(Constants.sp_store_name, key, value);
    }

    /*------------------- bool -----------------------*/

    public static boolean getBoolean(String storeName, String key) {
        boolean value = false;
        getSettingPreferences(storeName).edit();
        if (getSettingPreferences(storeName).contains(key)) {
            value = getSettingPreferences(storeName).getBoolean(key, false);
        }
        return value;
    }

    public static boolean getBoolean(String storeName, String key, boolean defaultValue) {
        boolean value = false;
        getSettingPreferences(storeName).edit();
        if (getSettingPreferences(storeName).contains(key)) {
            value = getSettingPreferences(storeName).getBoolean(key, defaultValue);
        }else {
            value = defaultValue;
        }
        return value;
    }

    public static boolean getBoolean(String key) {
        return getBoolean(Constants.sp_store_name, key);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return getBoolean(Constants.sp_store_name, key, defaultValue);
    }

    public static void putBoolean(String storeName, String key, boolean value) {
        SharedPreferences.Editor edit = getSettingPreferences(storeName).edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static void putBoolean(String key, boolean value) {
        putBoolean(Constants.sp_store_name, key, value);
    }

    /*------------------- set string -----------------------*/

    public static Set<String> getStringSet(String storeName, String key) {
        Set<String> value = null;
        getSettingPreferences(storeName).edit();
        if (getSettingPreferences(storeName).contains(key)) {
            value = getSettingPreferences(storeName).getStringSet(key, null);
        }
        return value;
    }

    public static Set<String> getStringSet(String key) {
        return getStringSet(Constants.sp_store_name, key);
    }

    public static void putStringSet(String storeName, String key, Set<String> value) {
        SharedPreferences.Editor edit = getSettingPreferences(storeName).edit();
        edit.putStringSet(key, value);
        edit.commit();
    }

    public static void putStringSet(String key, Set<String> value) {
        putStringSet(Constants.sp_store_name, key, value);
    }

    /*------------------- object -----------------------*/

    // TODO: 16/7/6 未实现
    
//    public static <T> void getObject(String storeName, String key, T resObj) {
//        String value = null;
//        getSettingPreferences(storeName).edit();
//        if (getSettingPreferences(storeName).contains(key)) {
//            value = getSettingPreferences(storeName).getString(key, null);
//        }
//        Gson gson = new GsonBuilder().create();
//        resObj = gson.fromJson(value, T.class);
//    }
//
//    public static String getString(String key) {
//        return getString(STORE_NAME_SETTING, key);
//    }
//
//    public static void putString(String storeName, String key, Serializable value) {
//        SharedPreferences.Editor edit = getSettingPreferences(storeName).edit();
//        edit.putString(key, "" + value);
//        edit.commit();
//    }
//
//    public static void putString(String key, Serializable value) {
//        putString(STORE_NAME_SETTING, key, value);
//    }

    /**
     * 获取所有数据
     * @return
     */
    public static Set<String> getAllKey() {
        return getSettingPreferences(Constants.sp_store_name).getAll().keySet();
    }


    public static Set<String> getAllKey(String storeName) {
        return getSettingPreferences(storeName).getAll().keySet();
    }

    /**
     * 清理所有数据
     * @param storeName
     */
    public static void clearStoreName(String storeName) {
        SharedPreferences.Editor edit = getSettingPreferences(storeName).edit();
        edit.clear();
        edit.apply();
    }
}
