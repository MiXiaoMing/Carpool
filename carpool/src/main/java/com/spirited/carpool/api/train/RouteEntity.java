package com.spirited.carpool.api.train;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 路途 实体
 */
public class RouteEntity implements Serializable {
    public Route route;
    public ArrayList<UserInfo> userInfoList = new ArrayList<>();
}
