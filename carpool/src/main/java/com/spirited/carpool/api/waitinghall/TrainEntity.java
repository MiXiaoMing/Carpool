package com.spirited.carpool.api.waitinghall;

import com.spirited.carpool.api.train.RouteEntity;
import com.spirited.support.network.result.BaseResult;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 车次 详情
 */
public class TrainEntity extends BaseResult<TrainEntity> implements Serializable {
    public CarInfo carInfo;
    public Train train;
    public ArrayList<RouteEntity> routeEntities = new ArrayList<>();
    public boolean autoPublishType;
}
