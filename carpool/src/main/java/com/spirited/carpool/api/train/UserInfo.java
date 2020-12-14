package com.spirited.carpool.api.train;

import java.io.Serializable;

/**
 * 用户信息 实体
 */
public class UserInfo implements Serializable {
    public String avatar, name, telephone;
    public String waitDesc, endDesc;
    public int count;
    public double latitude = 0D, longitude = 0D;
}
