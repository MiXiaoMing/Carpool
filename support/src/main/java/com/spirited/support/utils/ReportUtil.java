package com.spirited.support.utils;

import com.spirited.support.application.AFApplication;
import com.umeng.analytics.MobclickAgent;

/**
 * 友盟 崩溃信息收集
 */
public class ReportUtil {
    public static void reportError(String error) {
        MobclickAgent.reportError(AFApplication.applicationContext, error);
    }

    public static void reportError(Throwable throwable) {
        MobclickAgent.reportError(AFApplication.applicationContext, throwable);
    }

}
