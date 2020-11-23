package com.spirited.carpool.api.waitinghall;

import com.spirited.carpool.api.MobileServerRetrofit;
import com.spirited.carpool.api.PageBody;

import io.reactivex.Observable;

/**
 * 候车大厅 信息
 */

public class WaitingHallManager {
    private WaitingHallService service;

    public WaitingHallManager() {
        service = MobileServerRetrofit.getInstance().getRetrofit().create(WaitingHallService.class);
    }

    /**********  路线  **********/

    public Observable<TrainListEntity> getTrainList(PageBody body) {
        return service.getTrainList(body);
    }
}
