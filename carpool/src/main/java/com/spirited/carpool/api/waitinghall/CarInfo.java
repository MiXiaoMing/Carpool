package com.spirited.carpool.api.waitinghall;

import java.io.Serializable;

/**
 * 车辆 实体
 */
public class CarInfo implements Serializable {
    public String id, avatar, contact, telephone;
    public int totalOrderedCount;
    public int approvedLoadNumber;
    public String description;
}
