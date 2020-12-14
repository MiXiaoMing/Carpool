package com.spirited.carpool.api.waitinghall;

import java.io.Serializable;

/**
 * 车次 实体
 */
public class Train implements Serializable {
    public String id;

    public String startTime;
    public int occupiedTime, distance;

    public float price;
    public int orderedNumber;
}
