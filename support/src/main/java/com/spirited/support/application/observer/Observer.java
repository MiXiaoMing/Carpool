package com.spirited.support.application.observer;

/**
 * 监听者
 */
public interface Observer {

    /**
     * 绑定成功
     */
    public void attached();

    /**
     * 解绑成功
     */
    public void detached();
}
