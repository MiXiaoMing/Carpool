package com.spirited.carpool.api.waitinghall;

import java.io.Serializable;

/**
 * 车次 实体
 */
public class Train implements Serializable {
    public String id;
    public String startPoint, endPoint;
    public String startTime, endTime;
    public float price;
    public int orderedNumber;
}
