package com.spirited.support.application.observer;

/**
 * AF网络状态注册类
 */

public interface NetworkStateObserver extends Observer {

    void mobileNetConnect();
    void mobileNetDisConnect();

    void wifiNetConnect();
    void wifiNetDisConnect();

}
