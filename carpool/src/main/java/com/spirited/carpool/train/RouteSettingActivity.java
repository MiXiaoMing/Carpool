package com.spirited.carpool.train;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.appframe.library.component.notify.AFToast;
import com.appframe.library.storage.SharePreferences;
import com.appframe.utils.logger.Logger;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spirited.carpool.R;
import com.spirited.carpool.api.route.Route;
import com.spirited.carpool.constants.BaiduMapConfig;
import com.spirited.carpool.overlay.DrivingRouteOverlay;
import com.spirited.support.AutoBaseTitleActivity;
import com.spirited.support.constants.RouteConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * 路线规划，设置行车路线
 */
public class RouteSettingActivity extends AutoBaseTitleActivity {

    private MapView mapView;
    private LinearLayout llyRoutes;

    private LinearLayout llyRouteSetting;
    private EditText etAddress;
    private TextView tvLatLng, tvTime, tvDelete;
    private View viewDelete;
    private TimePicker timePicker;

    private BaiduMap baiduMap;
    private LocationClient locationClient;
    private SuggestionSearch suggestionSearch;
    private RoutePlanSearch routePlanSearch;

    private boolean isMiddle = false;
    private ArrayList<Route> routeList = new ArrayList<>();

    private double latitude = 0, longitude = 0;
    private String currentType = "";
    private Route currentRoute = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_route_setting);

        initView();
        initBaiduMap();
        initLocation();
        initSuggestionSearch();
        initRoutePlanSearch();
        initData();
    }

    private void initView() {
        findViewById(R.id.llyBack).setOnClickListener(clickListener);
        findViewById(R.id.tvSave).setOnClickListener(clickListener);

        mapView = findViewById(R.id.mapView);

        llyRoutes = findViewById(R.id.llyRoutes);
        findViewById(R.id.tvStart).setOnClickListener(clickListener);
        findViewById(R.id.tvWait).setOnClickListener(clickListener);
        findViewById(R.id.tvRoute).setOnClickListener(clickListener);
        findViewById(R.id.tvEnd).setOnClickListener(clickListener);

        llyRouteSetting = findViewById(R.id.llyRouteSetting);
        etAddress = findViewById(R.id.etAddress);
        tvLatLng = findViewById(R.id.tvLatLng);
        tvTime = findViewById(R.id.tvTime);
        tvDelete = findViewById(R.id.tvDelete);
        viewDelete = findViewById(R.id.viewDelete);
        timePicker = findViewById(R.id.timePicker);

        llyRouteSetting.setOnClickListener(clickListener);
        tvTime.setOnClickListener(clickListener);
        tvDelete.setOnClickListener(clickListener);
        findViewById(R.id.tvSearch).setOnClickListener(clickListener);
        findViewById(R.id.tvSure).setOnClickListener(clickListener);

        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(timeChangedListener);
    }

    private void initBaiduMap() {
        baiduMap = mapView.getMap();
        // 普通地图
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 实时路况
//        baiduMap.setTrafficEnabled(true);
        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);
        // 初始化
        BaiduMapConfig.setLevel(baiduMap, 13.0f);

        // 单击事件
        baiduMap.setOnMapClickListener(mapClickListener);
        // 标记点击事件
        baiduMap.setOnMarkerClickListener(markerClickListener);
    }

    /**
     * 位置信息
     */
    private void initLocation() {
        locationClient = new LocationClient(getApplicationContext());
        BaiduMapConfig.setLocationClientOption(locationClient);

        locationClient.registerLocationListener(locationListener);

        locationClient.start();
    }

    private void initSuggestionSearch() {
        suggestionSearch = SuggestionSearch.newInstance();
        suggestionSearch.setOnGetSuggestionResultListener(suggestionResultListener);
    }

    private void initRoutePlanSearch() {
        routePlanSearch = RoutePlanSearch.newInstance();
        routePlanSearch.setOnGetRoutePlanResultListener(planResultListener);
    }

    private void initData() {
        String json = SharePreferences.getStringWithDefault("route", null);
        if (!TextUtils.isEmpty(json)) {
            ArrayList<Route> routes = new Gson().fromJson(json, new TypeToken<ArrayList<Route>>(){}.getType());
            if (routes != null && routes.size() > 0) {
                routeList = routes;
                showRoutePath();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationClient != null) {
            locationClient.stop();
        }
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        suggestionSearch.destroy();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();

            if (id == R.id.llyBack) {
                finish();
            } else if (id == R.id.tvSave) {
                SharePreferences.putString("route", new Gson().toJson(routeList));
                finish();
            } else if (id == R.id.tvStart) {
                currentType = RouteConstants.route_type_start;

                currentRoute = null;
                for (int i = 0; i < routeList.size(); ++i) {
                    if (routeList.get(i).type.equals(RouteConstants.route_type_start)) {
                        currentRoute = routeList.get(i);
                    }
                }

                if (currentRoute == null) {
                    addEmptyRoute();
                }

                showRouteSetting(true);
            } else if (id == R.id.tvWait) {
                currentType = RouteConstants.route_type_wait;

                ArrayList<Route> routes = new ArrayList<>();
                for (int i = 0; i < routeList.size(); ++i) {
                    if (routeList.get(i).type.equals(RouteConstants.route_type_wait)) {
                        routes.add(routeList.get(i));
                    }
                }

                if (routes.size() <= 0) {
                    addEmptyRoute();
                    showRouteSetting(true);
                } else {
                    // 展示所有 候车点
                    baiduMap.clear();
                    for (int i = 0; i < routes.size(); ++i) {
                        BaiduMapConfig.addOverlay(baiduMap, routes.get(i).latitude, routes.get(i).longitude, R.drawable.icon_wait);
                    }
                    BaiduMapConfig.moveToMiddle(baiduMap, routes.get(0).latitude, routes.get(0).longitude);
                }
            } else if (id == R.id.tvRoute) {
                currentType = RouteConstants.route_type_route;

                ArrayList<Route> routes = new ArrayList<>();
                for (int i = 0; i < routeList.size(); ++i) {
                    if (routeList.get(i).type.equals(RouteConstants.route_type_route)) {
                        routes.add(routeList.get(i));
                    }
                }

                if (routes.size() <= 0) {
                    addEmptyRoute();
                    showRouteSetting(true);
                } else {
                    // 展示所有 途经地点
                    baiduMap.clear();
                    for (int i = 0; i < routes.size(); ++i) {
                        BaiduMapConfig.addOverlay(baiduMap, routes.get(i).latitude, routes.get(i).longitude, R.drawable.icon_route2);
                    }
                    BaiduMapConfig.moveToMiddle(baiduMap, routes.get(0).latitude, routes.get(0).longitude);
                }
            } else if (id == R.id.tvEnd) {
                currentType = RouteConstants.route_type_end;

                currentRoute = null;
                for (int i = 0; i < routeList.size(); ++i) {
                    if (routeList.get(i).type.equals(RouteConstants.route_type_end)) {
                        currentRoute = routeList.get(i);
                    }
                }

                if (currentRoute == null) {
                    addEmptyRoute();
                }

                showRouteSetting(true);
            } else if (id == R.id.llyRouteSetting) {
                // 什么都不做
            } else if (id == R.id.tvSearch) {
                String address = etAddress.getText().toString();
                if (TextUtils.isEmpty(address)) {
                    AFToast.showShort(RouteSettingActivity.this, "请填写正确地点名称");
                    return;
                }

                suggestionSearch.requestSuggestion(new SuggestionSearchOption()
                        .city("北京")
                        .keyword(address));
            } else if (id == R.id.tvTime) {
                if (timePicker.getVisibility() == View.VISIBLE) {
                    timePicker.setVisibility(View.GONE);
                } else {
                    timePicker.setVisibility(View.VISIBLE);
                }
            } else if (id == R.id.tvDelete) {
                routeList.remove(currentRoute);
                showRouteSetting(false);
            } else if (id == R.id.tvSure) {
                showRouteSetting(false);
            }
        }
    };

    /**
     * 新增 Route
     */
    private void addEmptyRoute() {
        currentRoute = new Route();
        currentRoute.type = currentType;
        currentRoute.latitude = latitude;
        currentRoute.longitude = longitude;

        routeList.add(currentRoute);
    }

    /**
     * 显示 标记点设置
     */
    private void showRouteSetting(boolean isShow) {
        if (isShow) {
            llyRoutes.setVisibility(View.GONE);
            llyRouteSetting.setVisibility(View.VISIBLE);
            timePicker.setVisibility(View.GONE);

            if (currentType.equals(RouteConstants.route_type_wait) || currentType.equals(RouteConstants.route_type_route)) {
                tvDelete.setVisibility(View.VISIBLE);
                viewDelete.setVisibility(View.VISIBLE);
            } else if (currentType.equals(RouteConstants.route_type_start) || currentType.equals(RouteConstants.route_type_end)) {
                tvDelete.setVisibility(View.GONE);
                viewDelete.setVisibility(View.GONE);
            }

            updateRouteSetting();
        } else {
            currentType = "";
            currentRoute = null;

            baiduMap.clear();
            llyRoutes.setVisibility(View.VISIBLE);
            llyRouteSetting.setVisibility(View.GONE);

            showRoutePath();
        }
    }

    /**
     * 更新 标记点设置
     */
    private void updateRouteSetting() {
        tvTime.setText(currentRoute.time);
        tvLatLng.setText("经纬度：" + String.valueOf(currentRoute.latitude).substring(0, 8) + "   " + String.valueOf(currentRoute.longitude).substring(0, 8));
        addOverLaySingle();
        BaiduMapConfig.moveToMiddle(baiduMap, currentRoute.latitude, currentRoute.longitude);
    }

    /**
     * 地图 添加标志
     */
    private void addOverLaySingle() {
        if (currentType.equals(RouteConstants.route_type_start)) {
            BaiduMapConfig.addOverlaySingle(baiduMap, currentRoute.latitude, currentRoute.longitude, R.drawable.icon_start);
        } else if (currentType.equals(RouteConstants.route_type_wait)) {
            BaiduMapConfig.addOverlaySingle(baiduMap, currentRoute.latitude, currentRoute.longitude, R.drawable.icon_wait);
        } else if (currentType.equals(RouteConstants.route_type_route)) {
            BaiduMapConfig.addOverlaySingle(baiduMap, currentRoute.latitude, currentRoute.longitude, R.drawable.icon_route2);
        } else if (currentType.equals(RouteConstants.route_type_end)) {
            BaiduMapConfig.addOverlaySingle(baiduMap, currentRoute.latitude, currentRoute.longitude, R.drawable.icon_end);
        }
    }

    /**
     * 展示 路线图
     */
    private void showRoutePath() {
        PlanNode startRoute = null, endRoute = null;
        List<PlanNode> passList = new ArrayList<>();
        for (int i = 0; i < routeList.size(); ++i) {
            Route route = routeList.get(i);
            if (route.type.equals(RouteConstants.route_type_start)) {
                startRoute = PlanNode.withLocation(new LatLng(route.latitude, route.longitude));
            } else if (route.type.equals(RouteConstants.route_type_wait)) {
                passList.add(PlanNode.withLocation(new LatLng(route.latitude, route.longitude)));
            } else if (route.type.equals(RouteConstants.route_type_route)) {
                passList.add(PlanNode.withLocation(new LatLng(route.latitude, route.longitude)));
            } else if (route.type.equals(RouteConstants.route_type_end)) {
                endRoute = PlanNode.withLocation(new LatLng(route.latitude, route.longitude));
            }
        }

        if (startRoute != null && endRoute != null) {
            DrivingRoutePlanOption planOption = new DrivingRoutePlanOption()
                    .from(startRoute)
                    .to(endRoute);

            if (passList.size() > 0) {
                planOption.passBy(passList);
            }

            routePlanSearch.drivingSearch(planOption);
        }
    }

    /**
     * 经纬度 信息监听
     */
    BDAbstractLocationListener locationListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (locationClient == null || mapView == null) {
                return;
            }

            latitude = bdLocation.getLatitude();
            longitude = bdLocation.getLongitude();

            baiduMap.setMyLocationData(new MyLocationData
                    .Builder()
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude())
                    .accuracy(bdLocation.getRadius())
                    .direction(bdLocation.getDirection())
                    .build());

            if (!isMiddle) {
                isMiddle = true;
                BaiduMapConfig.moveToMiddle(baiduMap, bdLocation.getLatitude(), bdLocation.getLongitude());
            }
        }
    };

    /**
     * 地图 单击事件
     */
    BaiduMap.OnMapClickListener mapClickListener = new BaiduMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            Logger.getLogger().d("点击经纬度：" + latLng.latitude + "..." + latLng.longitude);

            if (TextUtils.isEmpty(currentType)) {
                return;
            }

            // 新增 等车点 途经点
            if (currentRoute == null && (currentType.equals(RouteConstants.route_type_wait) || currentType.equals(RouteConstants.route_type_route))) {
                addEmptyRoute();
            }

            currentRoute.latitude = latLng.latitude;
            currentRoute.longitude = latLng.longitude;

            showRouteSetting(true);
        }

        @Override
        public void onMapPoiClick(MapPoi mapPoi) {
            Logger.getLogger().d("点击poi：" + mapPoi.getName()
                    + "..." + mapPoi.getPosition().latitude
                    + "..." + mapPoi.getPosition().longitude);

            if (TextUtils.isEmpty(currentType)) {
                return;
            }

            if (currentRoute == null && (currentType.equals(RouteConstants.route_type_wait) || currentType.equals(RouteConstants.route_type_route))) {
                addEmptyRoute();
            }

            currentRoute.latitude = mapPoi.getPosition().latitude;
            currentRoute.longitude = mapPoi.getPosition().longitude;

            showRouteSetting(true);
        }
    };

    /**
     * 标记 单击事件
     */
    BaiduMap.OnMarkerClickListener markerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            for (int i = 0; i < routeList.size(); ++i) {
                if (routeList.get(i).latitude == marker.getPosition().latitude &&
                        routeList.get(i).longitude == marker.getPosition().longitude) {
                    currentRoute = routeList.get(i);
                }
            }

            showRouteSetting(true);
            return true;
        }
    };

    /**
     * 搜索 监听
     */
    OnGetSuggestionResultListener suggestionResultListener = new OnGetSuggestionResultListener() {
        @Override
        public void onGetSuggestionResult(SuggestionResult suggestionResult) {
            List<SuggestionResult.SuggestionInfo> suggestionInfos = suggestionResult.getAllSuggestions();
            if (suggestionInfos == null || suggestionInfos.size() <= 0) {
                return;
            }

            BaiduMapConfig.moveToMiddle(baiduMap, suggestionInfos.get(0).pt);
            BaiduMapConfig.setLevel(baiduMap, 13.0f);
        }
    };

    /**
     * 驾车路线规划
     */
    OnGetRoutePlanResultListener planResultListener = new OnGetRoutePlanResultListener() {
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

        }

        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
            DrivingRouteOverlay overlay = new DrivingRouteOverlay(baiduMap);
            if (drivingRouteResult.getRouteLines().size() > 0) {
                overlay.setData(drivingRouteResult.getRouteLines().get(0));
                overlay.addToMap();
            }
        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

        }
    };

    /**
     * 时间改变 监听器
     */
    TimePicker.OnTimeChangedListener timeChangedListener = new TimePicker.OnTimeChangedListener() {
        @Override
        public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
            Logger.getLogger().d("时间：" + hour + "..." + minute);
            tvTime.setText(hour + ":" + minute);
            currentRoute.time = hour + ":" + minute;
        }
    };
}
