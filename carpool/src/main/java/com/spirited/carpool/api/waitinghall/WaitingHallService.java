package com.spirited.carpool.api.waitinghall;

import com.spirited.carpool.api.PageBody;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 与候车大厅相关接口
 */
public interface WaitingHallService {

    @POST("carousel/getCarouselList")
    Observable<CarouselListEntity> getCarouselList();

    @POST("waitingHall/getTrainList")
    Observable<TrainListEntity> getTrainList(@Body PageBody body);

    @POST("waitingHall/getTrainInfo")
    Observable<TrainEntity> getTrainInfo(@Body TrainInfoBody body);

    @POST("waitingHall/getCarStatistics")
    Observable<CarStatisticsDataEntity> getCarStatistics(@Body RequestBody body);
}
