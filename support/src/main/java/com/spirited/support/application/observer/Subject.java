package com.spirited.support.application.observer;

import java.util.ArrayList;

/**
 * 被监听对象基类
 * @param <T>
 */
public class Subject<T extends Observer> {

    protected ArrayList<T> observers = new ArrayList<T>();

    public void attach(T observer) {
        observers.add(observer);
        observer.attached();
    }

    public void detach(T observer) {
        if (observers.contains(observer)) {
            observers.remove(observer);
            observer.detached();
        }
    }
}
