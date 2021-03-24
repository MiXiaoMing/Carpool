package com.spirited.support.network.interceptor;

import com.spirited.support.logger.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 重试拦截器，对错误返回值，进行三次重试
 */
public class RetryInterceptor implements Interceptor {

    int RetryCount = 3;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // try the request
        Response response = chain.proceed(request);

        int tryCount = 0;
        while (!response.isSuccessful() && !isSpecial(response.code()) && tryCount < RetryCount) {

            Logger.getLogger().d("请求失败，重试第" + (tryCount+1)+"次请求");

            tryCount++;

            // retry the request
            response = chain.proceed(request);
        }

        // otherwise just pass the original response on
        return response;
    }

    private boolean isSpecial(int code) {
        if (code == 401) {
            return true;
        }
        return false;
    }
}
