package com.spirited.support.utils;

import com.appframe.library.application.AFApplication;
import com.umeng.analytics.MobclickAgent;

public class ReportUtil {
    public static void reportError(String error) {
        MobclickAgent.reportError(AFApplication.applicationContext, error);
    }

    public static void reportError(Throwable throwable) {
        MobclickAgent.reportError(AFApplication.applicationContext, throwable);
    }

}
