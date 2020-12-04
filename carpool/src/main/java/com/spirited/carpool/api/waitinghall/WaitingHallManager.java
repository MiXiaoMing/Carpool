package com.spirited.carpool.api.waitinghall;

import com.spirited.carpool.api.MobileServerRetrofit;
import com.spirited.carpool.api.PageBody;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * 候车大厅 信息
 */

public class WaitingHallManager {
    private WaitingHallService service;

    public WaitingHallManager() {
        service = MobileServerRetrofit.getInstance().getRetrofit().create(WaitingHallService.class);
    }

    /**********  车次  **********/

    public Observable<TrainListEntity> getTrainList(PageBody body) {
        return service.getTrainList(body);
    }

    public Observable<TrainInfoEntity> getTrainInfo(TrainInfoBody body) {
        return service.getTrainInfo(body);
    }

    public Observable<CarStatisticsDataEntity> getCarStatistics(RequestBody body) {
        return service.getCarStatistics(body);
    }
}
