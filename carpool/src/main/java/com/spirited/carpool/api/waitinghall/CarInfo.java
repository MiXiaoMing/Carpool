package com.spirited.carpool.api.waitinghall;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 车辆 实体
 */
public class CarInfo implements Serializable {
    public String id, cover, contact, telephone;
    public String carNumber;
    public int totalOrderedCount;
    public int approvedLoadNumber;
    public String description;
    public ArrayList<String> pictures = new ArrayList<>();
}
