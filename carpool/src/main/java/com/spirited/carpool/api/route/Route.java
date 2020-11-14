package com.spirited.carpool.api.route;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 路途 实体
 */
public class Route implements Serializable {
    public String type;
    public double latitude = 0D, longitude = 0D;
    public String description;
    public String time = new SimpleDateFormat("HH:mm").format(new Date());

    public ArrayList<UserInfo> userInfoList = new ArrayList<>();
}
