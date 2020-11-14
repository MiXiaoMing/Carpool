package com.spirited.carpool.constants;

import com.baidu.geofence.GeoFenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.spirited.carpool.R;

public class BaiduMapConfig {

    /**
     * 百度地址缩放级别及比例
     */
//            4	1000000
//            5	500000
//            6	200000
//            7	100000
//            8	50000
//            9	25000
//            10	20000
//            11	10000
//            12	5000
//            13	2000
//            14	1000
//            15	500
//            16	200
//            17	100
//            18	50
//            19	20
//            20	10
//            21	5

    /**
     * 设置 地图 缩放级别
     */
    public static void setLevel(BaiduMap baiduMap, float level) {
        if (baiduMap == null) {
            return;
        }
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(level);
        baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    /**
     * 设置 locationClient参数
     */
    public static void setLocationClientOption(LocationClient locationClient) {
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

    /**
     * 移动到 地图中间
     */
    public static void moveToMiddle(BaiduMap baiduMap, LatLng latLng) {
        if (baiduMap == null) {
            return;
        }

        MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(status);//动画的方式到中间
    }

    public static void moveToMiddle(BaiduMap baiduMap, Double latitude, Double longitude) {
        if (baiduMap == null) {
            return;
        }
        moveToMiddle(baiduMap, new LatLng(latitude, longitude));
    }

    /**
     * 添加 标记点 单例
     */
    public static void addOverlaySingle(BaiduMap baiduMap, Double latitude, Double longitude, int resID) {
        baiduMap.clear();

        LatLng point = new LatLng(latitude, longitude);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(resID);
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        baiduMap.addOverlay(option);
    }

    /**
     * 添加 标记点
     */
    public static void addOverlay(BaiduMap baiduMap, Double latitude, Double longitude, int resID) {
        LatLng point = new LatLng(latitude, longitude);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(resID);
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        baiduMap.addOverlay(option);
    }
}
