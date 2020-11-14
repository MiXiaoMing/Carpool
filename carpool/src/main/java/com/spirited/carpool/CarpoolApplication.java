package com.spirited.carpool;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.appframe.framework.config.MetaDataConfig;
import com.appframe.library.application.AFApplication;
import com.appframe.library.application.AFMultiDexApplication;
import com.appframe.utils.logger.Logger;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.spirited.support.constants.Constants;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.commonsdk.statistics.common.DeviceConfig;
import com.zhy.autolayout.config.AutoLayoutConifg;

import java.lang.reflect.Method;

public class CarpoolApplication extends AFMultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        // 设置日志打印级别
        Logger.getLogger().setWriteDebugLog(true);

        AutoLayoutConifg.getInstance().useDeviceSize();

        initData();
        initBugly();
        initUmeng();
        initBaiduMap();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Beta.installTinker();
    }

    private void initData() {
        new MetaDataConfig().initSPStoreName("carpool");
    }

    private void initBugly() {
        // 设置是否开启热更新能力，默认为true
        Beta.enableHotfix = true;
        // 设置是否自动下载补丁
        Beta.canAutoDownloadPatch = true;
        // 设置是否提示用户重启
        if (BuildConfig.APP_PUBLISH_TAG.equals(Constants.app_publish_tag_gray)) {
            Beta.canNotifyUserRestart = true;
            // 设置是否为开发设备
            Bugly.setIsDevelopmentDevice(this, true);
        } else {
            Bugly.setIsDevelopmentDevice(this, false);
        }
        // 设置是否自动合成补丁
        Beta.canAutoPatch = true;
        Beta.autoDownloadOnWifi = true;
        // 延迟初始化
        Beta.initDelay = 3 * 1000;

        Bugly.init(this, "fa6a585edc", true);
    }

    private void initUmeng() {
        //友盟
        UMConfigure.init(AFApplication.applicationContext, Constants.umeng_app_key, BuildConfig.APP_PUBLISH_TAG, UMConfigure.DEVICE_TYPE_PHONE, "");
        // 选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    private void initBaiduMap() {
        SDKInitializer.initialize(this);
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }

    public static void getDeviceInfo(Context context) {
        String[] deviceInfo = new String[2];
        try {
            if (context != null) {
                deviceInfo[0] = DeviceConfig.getDeviceIdForGeneral(context);
                deviceInfo[1] = DeviceConfig.getMac(context);
            }
            Logger.getLogger().e("{\"device_id\":\"" + deviceInfo[0] + "\",\"mac\":\"" + deviceInfo[1] + "\"}");
        } catch (Exception e) {

        }
    }

}
