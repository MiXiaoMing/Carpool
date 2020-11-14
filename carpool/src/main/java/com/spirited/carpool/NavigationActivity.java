package com.spirited.carpool;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appframe.library.component.image.ImageLoader;
import com.appframe.library.storage.SharePreferences;
import com.appframe.utils.logger.Logger;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
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
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spirited.carpool.adapter.RouteUserAdapter;
import com.spirited.carpool.api.CustomObserver;
import com.spirited.carpool.api.route.Route;
import com.spirited.carpool.api.route.RouteDataManager;
import com.spirited.carpool.api.route.RouteListEntity;
import com.spirited.carpool.api.route.UserInfo;
import com.spirited.carpool.constants.BaiduMapConfig;
import com.spirited.carpool.overlay.DrivingRouteOverlay;
import com.spirited.support.AutoBaseTitleActivity;
import com.spirited.support.constants.RouteConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 路线规划，设置行车路线
 */
public class NavigationActivity extends AutoBaseTitleActivity {

    private MapView mapView;

    private BaiduMap baiduMap;
    private LocationClient locationClient;
    private RoutePlanSearch routePlanSearch;

    private LinearLayout llyOne;
    private ImageView ivIcon;
    private TextView tvNick, tvNumber, tvWait, tvEnd, tvTelephone;

    private LinearLayout llyMultiple;
    private RouteUserAdapter userAdapter;

    private ArrayList<Route> routeList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation);

        initView();
        initBaiduMap();
        initLocation();
        initRoutePlanSearch();
        initData();

        getData();
    }

    private void initView() {
        findViewById(R.id.llyBack).setOnClickListener(clickListener);

        mapView = findViewById(R.id.mapView);

        llyOne = findViewById(R.id.llyOne);
        ivIcon = findViewById(R.id.ivIcon);
        tvNick = findViewById(R.id.tvNick);
        tvNumber = findViewById(R.id.tvNumber);
        tvWait = findViewById(R.id.tvWait);
        tvEnd = findViewById(R.id.tvEnd);
        tvTelephone = findViewById(R.id.tvTelephone);
        findViewById(R.id.rlyClose).setOnClickListener(clickListener);

        llyMultiple = findViewById(R.id.llyMultiple);
        ListView lvUsers = findViewById(R.id.lvUsers);
        userAdapter = new RouteUserAdapter(this);
        lvUsers.setAdapter(userAdapter);
        findViewById(R.id.tvClose).setOnClickListener(clickListener);
    }

    private void initBaiduMap() {
        baiduMap = mapView.getMap();
        // 普通地图
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 实时路况
        baiduMap.setTrafficEnabled(true);
        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);
        // 初始化
        BaiduMapConfig.setLevel(baiduMap, 15.0f);

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

    private void initRoutePlanSearch() {
        routePlanSearch = RoutePlanSearch.newInstance();
        routePlanSearch.setOnGetRoutePlanResultListener(planResultListener);
    }

    private void initData() {
        String json = SharePreferences.getStringWithDefault("route", null);
        if (!TextUtils.isEmpty(json)) {
            ArrayList<Route> routes = new Gson().fromJson(json, new TypeToken<ArrayList<Route>>() {}.getType());
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
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();

            if (id == R.id.llyBack) {
                finish();
            } else if (id == R.id.rlyClose) {
                llyOne.setVisibility(View.GONE);
            } else if (id == R.id.tvClose) {
                llyMultiple.setVisibility(View.GONE);
            }
        }
    };

    /**
     * 展示 单人 信息框
     */
    private void showSingleInfoDialog(UserInfo userInfo) {
        llyOne.setVisibility(View.VISIBLE);
        ImageLoader.normal(this, userInfo.avatar, R.drawable.default_image_white, ivIcon);
        tvNick.setText(userInfo.name);
        tvNumber.setText("第" + userInfo.count + "次同行");
        tvWait.setText("等车点：" + userInfo.waitDesc);
        tvEnd.setText("下车点：" + userInfo.endDesc);
        tvTelephone.setText(userInfo.telephone);

        llyMultiple.setVisibility(View.GONE);
    }

    /**
     * 展示 多人 信息框
     */
    private void showMultipleInfoDialog(ArrayList<UserInfo> userInfos) {
        llyOne.setVisibility(View.GONE);

        llyMultiple.setVisibility(View.VISIBLE);
        userAdapter.addAll(userInfos);
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
     * 展示标记点
     */
    private void showOverlays() {
        baiduMap.clear();

        for (int i = 0; i < routeList.size(); ++i) {
            if (!routeList.get(i).type.equals(RouteConstants.route_type_wait)) {
                continue;
            }

            int count = routeList.get(i).userInfoList.size();
            if (count == 0) {
                BaiduMapConfig.addOverlay(baiduMap, routeList.get(i).latitude, routeList.get(i).longitude, R.drawable.icon_route2);
            } else if (count == 1) {
                BaiduMapConfig.addOverlay(baiduMap, routeList.get(i).latitude, routeList.get(i).longitude, R.drawable.icon_mark1);
            } else if (count == 2) {
                BaiduMapConfig.addOverlay(baiduMap, routeList.get(i).latitude, routeList.get(i).longitude, R.drawable.icon_mark2);
            } else if (count == 3) {
                BaiduMapConfig.addOverlay(baiduMap, routeList.get(i).latitude, routeList.get(i).longitude, R.drawable.icon_mark3);
            } else if (count == 4) {
                BaiduMapConfig.addOverlay(baiduMap, routeList.get(i).latitude, routeList.get(i).longitude, R.drawable.icon_mark4);
            } else if (count == 5) {
                BaiduMapConfig.addOverlay(baiduMap, routeList.get(i).latitude, routeList.get(i).longitude, R.drawable.icon_mark5);
            } else if (count == 6) {
                BaiduMapConfig.addOverlay(baiduMap, routeList.get(i).latitude, routeList.get(i).longitude, R.drawable.icon_mark6);
            } else if (count == 7) {
                BaiduMapConfig.addOverlay(baiduMap, routeList.get(i).latitude, routeList.get(i).longitude, R.drawable.icon_mark7);
            } else if (count == 8) {
                BaiduMapConfig.addOverlay(baiduMap, routeList.get(i).latitude, routeList.get(i).longitude, R.drawable.icon_mark8);
            } else if (count == 9) {
                BaiduMapConfig.addOverlay(baiduMap, routeList.get(i).latitude, routeList.get(i).longitude, R.drawable.icon_mark9);
            } else {
                BaiduMapConfig.addOverlay(baiduMap, routeList.get(i).latitude, routeList.get(i).longitude, R.drawable.icon_mark10);
            }
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

            baiduMap.setMyLocationData(new MyLocationData
                    .Builder()
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude())
                    .accuracy(bdLocation.getRadius())
                    .direction(bdLocation.getDirection())
                    .build());

            BaiduMapConfig.moveToMiddle(baiduMap, bdLocation.getLatitude(), bdLocation.getLongitude());
        }
    };

    /**
     * 标记 单击事件
     */
    BaiduMap.OnMarkerClickListener markerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            Route route = null;
            // 路径 标记
            for (int i = 0; i < routeList.size(); ++i) {
                if (routeList.get(i).latitude == marker.getPosition().latitude &&
                        routeList.get(i).longitude == marker.getPosition().longitude) {
                    route = routeList.get(i);

                    if (route.userInfoList.size() == 1) {
                        showSingleInfoDialog(route.userInfoList.get(0));
                    } else if (route.userInfoList.size() > 1) {
                        showMultipleInfoDialog(route.userInfoList);
                    }
                }
            }

            // 行人 标记
            // TODO: 2020/11/14 行人 标志点击
//            showInfoDialog();
            return true;
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

    /*****  网络请求  ******/

    private void getData() {
        final String pathID = getIntent().getStringExtra("pathID");
        Logger.getLogger().d("获取路径点列表, pathID -> " + pathID);

        new RouteDataManager().getRouteList(RequestBody.create(MediaType.parse("text/plain"), pathID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<RouteListEntity>() {

                    @Override
                    public void onError(String message) {
                        Random random = new Random();

                        for (int i = 0; i < routeList.size(); ++i) {
                            if (routeList.get(i).type.equals(RouteConstants.route_type_wait)) {
                                int count = 1;
                                ArrayList<UserInfo> userInfos = routeList.get(i).userInfoList;
                                for (int j = 0; j < count; ++j) {
                                    UserInfo userInfo = new UserInfo();
                                    userInfo.avatar = "http://192.168.1.47/jbh/image/icon_clean_daily.jpg";
                                    userInfo.count = random.nextInt(10);
                                    userInfo.waitDesc = "昌平区于辛庄";
                                    userInfo.endDesc = "保定易县";
                                    userInfo.name = "测试账号" + random.nextInt(10);
                                    userInfo.telephone = "13718863263";
                                    userInfos.add(userInfo);
                                }
                            }
                        }

                        showOverlays();
                    }

                    @Override
                    public void onSuccess(RouteListEntity result) {

                    }
                });
    }
}