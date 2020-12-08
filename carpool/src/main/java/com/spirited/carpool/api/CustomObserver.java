package com.spirited.carpool.api;

import android.content.Intent;
import android.text.TextUtils;

import com.appframe.library.component.notify.AFToast;
import com.appframe.utils.NetUtil;
import com.appframe.utils.app.ActivityUtil;
import com.appframe.utils.app.AppRuntimeUtil;
import com.spirited.carpool.mine.LoginActivity;
import com.spirited.carpool.R;
import com.spirited.support.common.Result;
import com.spirited.support.utils.AuthUtil;
import com.spirited.support.utils.ReportUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class CustomObserver<T extends Result> implements Observer<T> {

    public CustomObserver() {
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T result) {
        if (result != null) {
            if (result.success) {
                onSuccess(result);
            } else if (result.errCode.equals("401")) {
                AuthUtil.saveAuth("");
                if (ActivityUtil.isAvailable(AppRuntimeUtil.getInstance().getCurrentActivity())) {
                    AFToast.showShort(AppRuntimeUtil.getInstance().getCurrentActivity(), result.message);

                    // 跳转到登录页面
                    AppRuntimeUtil.getInstance().getCurrentActivity().startActivity(new Intent(
                            AppRuntimeUtil.getInstance().getCurrentActivity(), LoginActivity.class
                    ));
                }
            } else {
                if (ActivityUtil.isAvailable(AppRuntimeUtil.getInstance().getCurrentActivity())) {
                    if (!TextUtils.isEmpty(result.message))
                        AFToast.showShort(AppRuntimeUtil.getInstance().getCurrentActivity(), result.message);
                }
                onError(result.message);
            }
        } else {
            if (ActivityUtil.isAvailable(AppRuntimeUtil.getInstance().getCurrentActivity())) {
                AFToast.showShort(AppRuntimeUtil.getInstance().getCurrentActivity(), "无更多数据");
            }
            onError("无更多数据");
        }
    }

    @Override
    public void onError(Throwable e) {
        if (ActivityUtil.isAvailable(AppRuntimeUtil.getInstance().getCurrentActivity())) {
            if (e.toString().equals("com.google.gson.JsonSyntaxException: java.lang.IllegalStateException: Expected BEGIN_ARRAY but was STRING at line 1 column 76 path $.data")) {

            } else {
                if (!NetUtil.isConnect(AppRuntimeUtil.getInstance().getCurrentActivity())) {
                    AFToast.showShort(AppRuntimeUtil.getInstance().getCurrentActivity(), R.string.network_failed);
                }
            }
        }
        ReportUtil.reportError(e);
        onError(e.toString());
    }

    @Override
    public void onComplete() {

    }

    /**
     * 错误回调
     */
    public abstract void onError(String message);

    /**
     * 正确回调
     */
    public abstract void onSuccess(T result);
}
