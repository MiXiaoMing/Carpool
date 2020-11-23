package com.spirited.carpool.api.waitinghall;

import com.spirited.support.common.BaseResult;

import java.util.ArrayList;

/**
 * 车次 列表
 */
public class TrainListEntity extends BaseResult<TrainListEntity> {
    public ArrayList<Train> trainList;
}
