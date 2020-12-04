package com.spirited.carpool.api.waitinghall;

import com.spirited.support.common.BaseResult;

import java.util.ArrayList;

/**
 * 车次 详情
 */
public class TrainInfoEntity extends BaseResult<TrainInfoEntity> {
    public CarInfo carInfo;
    public Train train;
}
