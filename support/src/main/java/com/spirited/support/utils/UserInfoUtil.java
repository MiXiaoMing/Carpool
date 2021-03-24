package com.spirited.support.utils;

import android.text.TextUtils;

import com.spirited.support.network.entity.UserInfo;

/**
 * 当前用户信息
 */

public class UserInfoUtil {
    private static String storeName = "-User";

    private static String key_userID = "id";
    private static String key_userName = "userName";
    private static String key_userType = "userType";
    private static String key_cellphone = "cellphone";
    private static String key_car_total_count = "carTotalCount";
    private static String key_region = "region";

    public static void setUserID(String id) {
        SharePreferences.putString(key_userID, id);
    }

    public static String getUserID() {
        return SharePreferences.getString(key_userID);
    }

    public static void setUserName(String userName) {
        SharePreferences.putString(key_userName, userName);
    }

    public static String getUserName() {
        return SharePreferences.getString(key_userName);
    }

    public static void setUserType(String userType) {
        SharePreferences.putString(key_userType, userType);
    }

    public static String getUserType() {
        return SharePreferences.getString(key_userType);
    }

    public static void setCellphone(String cellphone) {
        SharePreferences.putString(key_cellphone, cellphone);
    }

    public static String getCellphone() {
        return SharePreferences.getString(key_cellphone);
    }

    public static void setCarTotalCount(float balance) {
        SharePreferences.putFloat(key_car_total_count, balance);
    }

    public static float getCarTotalCount() {
        float balance = SharePreferences.getFloat(key_car_total_count);
        if (balance == 1.0) {
            return 0;
        }
        return balance;
    }

    public static String getRegion() {
        return SharePreferences.getString(key_region);
    }

    public static void setRegion(String region) {
        SharePreferences.putString(key_region, region);
    }

    public static void updateUser(UserInfo userInfo) {
        clear();
        if (userInfo != null) {
            UserInfoUtil.setUserID(userInfo.id);
            UserInfoUtil.setUserName(userInfo.name);
            UserInfoUtil.setUserType(userInfo.type);
            UserInfoUtil.setCellphone(userInfo.phoneNumber);
            UserInfoUtil.setCarTotalCount(userInfo.balance);
            UserInfoUtil.setRegion(userInfo.region);
        }
    }

    public static void clear() {
        UserInfoUtil.setUserID("");
        UserInfoUtil.setUserName("");
        UserInfoUtil.setUserType("");
        UserInfoUtil.setCellphone("");
        UserInfoUtil.setCarTotalCount(0);
        UserInfoUtil.setRegion("");
    }

    public static boolean isLogin() {
        if (!TextUtils.isEmpty(AuthUtil.getAuth()) && !TextUtils.isEmpty(getUserID())) {
            return true;
        }
        return false;
    }
}
