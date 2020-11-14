package com.spirited.support.common;


public abstract class Callback<T> {
    public void success(T data) {}
    public void fail(String data) {}
}
