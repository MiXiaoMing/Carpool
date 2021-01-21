package com.spirited.carpool.api.mine;

import com.spirited.carpool.api.PageBody;
import com.spirited.carpool.api.waitinghall.CarStatisticsDataEntity;
import com.spirited.carpool.api.waitinghall.CarouselListEntity;
import com.spirited.carpool.api.waitinghall.TrainEntity;
import com.spirited.carpool.api.waitinghall.TrainInfoBody;
import com.spirited.carpool.api.waitinghall.TrainListEntity;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 与我的相关接口
 */
public interface MineService {

    @POST("mine/getCarList")
    Observable<CarListEntity> getCarList(@Body PageBody body);
}
