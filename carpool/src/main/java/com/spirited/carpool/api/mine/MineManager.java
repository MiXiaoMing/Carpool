package com.spirited.carpool.api.mine;

import com.spirited.carpool.api.MobileServerRetrofit;
import com.spirited.carpool.api.PageBody;
import com.spirited.carpool.api.waitinghall.CarStatisticsDataEntity;
import com.spirited.carpool.api.waitinghall.CarouselListEntity;
import com.spirited.carpool.api.waitinghall.TrainEntity;
import com.spirited.carpool.api.waitinghall.TrainInfoBody;
import com.spirited.carpool.api.waitinghall.TrainListEntity;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * 我的
 */

public class MineManager {
    private MineService service;

    public MineManager() {
        service = MobileServerRetrofit.getInstance().getRetrofit().create(MineService.class);
    }

    /**********  车辆  **********/

    public Observable<CarListEntity> getCarList(PageBody body) {
        return service.getCarList(body);
    }
}
