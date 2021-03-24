package com.spirited.support.application.observer;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;


import com.spirited.support.logger.Logger;
import com.spirited.support.utils.app.AppRuntimeUtil;

import java.util.ArrayList;

/**
 * activity状态改变监听器
 */

public class ActivityLifecycle {

    public ActivityLifecycle() {
        Logger.getLogger().d("ActivityLifecycle init");
    }

    private static ArrayList<ActivityLifecycleObserver> observers = new ArrayList<>();

    public static void attach(ActivityLifecycleObserver observer) {
        observers.add(observer);
        observer.attached();
    }

    public static void detach(ActivityLifecycleObserver observer) {
        if (observers.contains(observer)) {
            observers.remove(observer);
            observer.detached();
        }
    }

    private String previousActivityName;

    public Application.ActivityLifecycleCallbacks callbacks = new Application.ActivityLifecycleCallbacks() {
        private int activityCount = 0;

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (activityCount == 0) {
                for (ActivityLifecycleObserver observer : observers) {
                    observer.changeToForeground();
                }
                AppRuntimeUtil.getInstance().setFrontOrBack(true);
                previousActivityName = "Home";
            }
            ++activityCount;
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Logger.getLogger().d("打开页面：" + activity.getClass().getName());
            AppRuntimeUtil.getInstance().setCurrentActivity(activity);

            for (ActivityLifecycleObserver observer : observers) {
                observer.activityResumed(activity.getClass().getName(), previousActivityName, System.currentTimeMillis());
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            for (ActivityLifecycleObserver observer : observers) {
                observer.activityPaused(activity.getClass().getName(), previousActivityName, System.currentTimeMillis());
            }

            previousActivityName = activity.getClass().getName();
            Logger.getLogger().d("关闭页面：" + previousActivityName);
        }

        @Override
        public void onActivityStopped(Activity activity) {
            --activityCount;
            if (activityCount == 0) {
                for (ActivityLifecycleObserver observer : observers) {
                    observer.changeToBackground();
                }
                AppRuntimeUtil.getInstance().setFrontOrBack(false);
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };
}
