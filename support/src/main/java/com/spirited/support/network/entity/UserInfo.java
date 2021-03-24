package com.spirited.support.network.entity;


import com.spirited.support.network.result.BaseResult;

public class UserInfo extends BaseResult<UserInfo> {
    public String id, name, type, phoneNumber, region;
    public float balance;
}
