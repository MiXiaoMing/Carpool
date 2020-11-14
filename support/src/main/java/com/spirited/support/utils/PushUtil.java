package com.spirited.support.utils;


import com.appframe.library.storage.SharePreferences;

public class PushUtil {
    private static String key_cid = "cid";  //从个推获取的设备唯一ID

    public static String getCID() {
        return SharePreferences.getString(key_cid);
    }

    public static void setCID(String cid) {
        SharePreferences.putString(key_cid, cid);
    }
}
