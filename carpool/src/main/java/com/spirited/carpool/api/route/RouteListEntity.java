package com.spirited.carpool.api.route;

import com.spirited.support.common.BaseResult;

import java.util.ArrayList;

/**
 * 路途 实体
 */
public class RouteListEntity extends BaseResult<RouteListEntity> {
    public ArrayList<Route> routeList;
}
