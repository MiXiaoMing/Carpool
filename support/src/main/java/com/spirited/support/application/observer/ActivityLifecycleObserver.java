package com.spirited.support.application.observer;

/**
 * 页面生命周期 观察者
 */

public interface ActivityLifecycleObserver extends Observer{

    /**
     * activity页面显示完毕
     * @param activityName
     * @param previousActivityName
     * @param time
     */
    void activityResumed(String activityName, String previousActivityName, long time);

    /**
     * 页面开始关闭
     * @param activityName
     * @param previousActivityName
     * @param time
     */
    void activityPaused(String activityName, String previousActivityName, long time);

    /**
     * 切换到前台
     */
    public void changeToForeground();

    /**
     * 切换到后台
     */
    public void changeToBackground();

    /**
     * 获取当前app状态：
     * @return  true：前台  false：后台
     */
    public boolean frontOrBack();
}
