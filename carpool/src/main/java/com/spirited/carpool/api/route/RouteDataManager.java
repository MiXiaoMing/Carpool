package com.spirited.carpool.api.route;

import com.spirited.carpool.api.MobileServerRetrofit;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * 商品信息
 */

public class RouteDataManager {
    private RouteService service;

    public RouteDataManager() {
        service = MobileServerRetrofit.getInstance().getRetrofit().create(RouteService.class);
    }

    /**********  路线  **********/

    public Observable<RouteListEntity> getRouteList(RequestBody body) {
        return service.getRouteList(body);
    }
}
