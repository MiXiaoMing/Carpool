package com.spirited.carpool.train;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.appframe.utils.logger.Logger;
import com.baidu.geofence.GeoFence;
import com.baidu.geofence.GeoFenceClient;
import com.baidu.geofence.GeoFenceListener;
import com.baidu.geofence.model.DPoint;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.spirited.carpool.R;
import com.spirited.carpool.mine.LoginActivity;
import com.spirited.support.AutoBaseTitleActivity;

import java.util.List;


public class LocationActivity extends AutoBaseTitleActivity {

    private LocationClient locationClient;
    private NotifyListener notifyListener = new NotifyListener();

    private GeoFenceClient geoFenceClient;

    public static final String GEOFENCE_BROADCAST_ACTION = "com.location.apis.geofencedemo.broadcast";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initView();
        initLocation();
        initGeoFence();
    }

    private void initView() {
//        findViewById(R.id.btnLogin).setOnClickListener(clickListener);
    }

    /**
     * 位置信息
     */
    private void initLocation() {
        locationClient = new LocationClient(getApplicationContext());

        {
            LocationClientOption option = new LocationClientOption();
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
            option.setCoorType(GeoFenceClient.BD09LL);
            option.setScanSpan(1000);
            option.setOpenGps(true);
            option.setLocationNotify(true);
            option.setIgnoreKillProcess(false);
            option.SetIgnoreCacheException(false);
            option.setWifiCacheTimeOut(5 * 60 * 1000);
            option.setEnableSimulateGps(false);
            option.setNeedNewVersionRgc(true);

            option.setIsNeedAddress(true);
            option.setNeedNewVersionRgc(true);

            option.setIsNeedLocationDescribe(true);

            locationClient.setLocOption(option);
        }

        locationClient.registerLocationListener(locationListener);

        {
            initLocationNotify();
            locationClient.registerNotify(notifyListener);
        }

        locationClient.start();
    }

    private void initLocationNotify() {
        notifyListener.SetNotifyLocation(0D, 0D, 200, GeoFenceClient.BD09LL);
    }

    /**
     * 地理围栏
     */
    private void initGeoFence() {
        geoFenceClient = new GeoFenceClient(getApplicationContext());
        geoFenceClient.setActivateAction(GeoFenceClient.GEOFENCE_IN_OUT_STAYED);
        geoFenceClient.setTriggerCount(3, 3, 3);
        geoFenceClient.setStayTime(5 * 60);

        geoFenceClient.setGeoFenceListener(geoFenceListener);
        addGeoFence();

        geoFenceClient.createPendingIntent(GEOFENCE_BROADCAST_ACTION);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(GEOFENCE_BROADCAST_ACTION);
        registerReceiver(mGeoFenceReceiver, filter);
    }

    /**
     * 添加 圆形围栏
     */
    private void addGeoFence() {
        geoFenceClient.addGeoFence(new DPoint(40.051D, 116.300D), GeoFenceClient.BD09LL, 100, "候车地点：" + "123");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (locationClient != null) {
            locationClient.removeNotifyEvent(notifyListener);
            locationClient.stop();
        }

        if (geoFenceClient != null) {
            geoFenceClient.removeGeoFence();
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();

//            if (id == R.id.btnLogin) {
//                startActivity(new Intent(LocationActivity.this, LoginActivity.class));
//            }
        }
    };

    /**
     * 经纬度 信息监听
     */
    BDAbstractLocationListener locationListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            double latitude = bdLocation.getLatitude();    //获取纬度信息
            double longitude = bdLocation.getLongitude();    //获取经度信息
            float radius = bdLocation.getRadius();    //获取定位精度，默认值为0.0f

            Logger.getLogger().e("经纬度：" + latitude + "..." + longitude + "..." + radius);

            String addr = bdLocation.getAddrStr();    //获取详细地址信息
            String country = bdLocation.getCountry();    //获取国家
            String province = bdLocation.getProvince();    //获取省份
            String city = bdLocation.getCity();    //获取城市
            String district = bdLocation.getDistrict();    //获取区县
            String street = bdLocation.getStreet();    //获取街道信息
            String adcode = bdLocation.getAdCode();    //获取adcode
            String town = bdLocation.getTown();    //获取乡镇信息

            Logger.getLogger().e("地址：" + country + "..." + province + "..." + city + "..." + district
                    + "..." + street + "..." + adcode + "..." + town + "..." + addr);

            Logger.getLogger().e("描述：" + bdLocation.getLocationDescribe());
            // 错误码
            int errorCode = bdLocation.getLocType();
        }
    };

    /**
     * 地址围栏 创建回调
     */
    private GeoFenceListener geoFenceListener = new GeoFenceListener() {
        @Override
        public void onGeoFenceCreateFinished(List<GeoFence> geoFenceList, int errorCode, String message) {
            if (errorCode == GeoFence.ADDGEOFENCE_SUCCESS) {
                Logger.getLogger().d("添加围栏成功：" + geoFenceList.size());
            } else {
                Logger.getLogger().e("添加围栏失败：" + errorCode + "..." + message);
            }
        }
    };

    /**
     * 地理围栏 监听
     */
    private BroadcastReceiver mGeoFenceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(GEOFENCE_BROADCAST_ACTION)) {
                Bundle bundle = intent.getExtras();
                int status = bundle.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS);
                String customId = bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
                String fenceId = bundle.getString(GeoFence.BUNDLE_KEY_FENCEID);
                GeoFence fence = bundle.getParcelable(GeoFence.BUNDLE_KEY_FENCE);
                Logger.getLogger().d("出发地理围栏：" + status + "..." + customId + "..." + fenceId + "..." + fence.toString());
            }
        }
    };

    /**
     * 位置提醒
     */
    public class NotifyListener extends BDNotifyListener {

        public void onNotify(BDLocation location, float distance) {
            //已到达设置监听位置附近
            Logger.getLogger().d("位置提醒：" + location.toString() + "..." + distance);
        }
    };
}
