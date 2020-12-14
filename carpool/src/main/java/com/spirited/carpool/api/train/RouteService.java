package com.spirited.carpool.api.train;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 与路线相关接口
 */
public interface RouteService {

    @POST("route/getRouteList")
    Observable<RouteListEntity> getRouteList(@Body RequestBody body);
}
