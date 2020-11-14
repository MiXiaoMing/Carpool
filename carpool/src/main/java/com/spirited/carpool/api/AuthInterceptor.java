package com.spirited.carpool.api;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.appframe.utils.app.AppRuntimeUtil;
import com.appframe.utils.logger.Logger;
import com.spirited.carpool.LoginActivity;
import com.spirited.support.utils.AuthUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 判断返回代码： 401
 */
public class AuthInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        if (response.code() == 401) {
            Logger.getLogger().e("401，重新登录");
            AuthUtil.saveAuth("");
            AppRuntimeUtil.getInstance().getCurrentActivity().startActivity(
                    new Intent(AppRuntimeUtil.getInstance().getCurrentActivity(), LoginActivity.class));
        }
        return response;
    }
}