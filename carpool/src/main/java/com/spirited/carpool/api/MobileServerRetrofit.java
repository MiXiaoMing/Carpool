package com.spirited.carpool.api;

import com.appframe.library.network.http.AFHttpClient;
import com.appframe.library.network.http.validate.DeserializeActionFactory;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * mobile sever 接口请求
 */
public class MobileServerRetrofit {

    private Retrofit retrofit = null;

    private static MobileServerRetrofit instance = new MobileServerRetrofit();

    public static MobileServerRetrofit getInstance() {
         return instance;
    }

    private MobileServerRetrofit() {
        resetApp();
    }

    public void resetApp() {
        OkHttpClient.Builder builder = AFHttpClient.getInstance().newBuilder();

        // 禁用代理
//        if (BuildConfig.PROXY_FORBID_STATUS != 1) {
//            builder.proxy(Proxy.NO_PROXY);
//        }

        List<Interceptor> interceptors = builder.interceptors();
        // 添加header信息
        interceptors.add(0, new CustomHeaderInterceptor());
        interceptors.add(new AuthInterceptor());

        retrofit = new Retrofit.Builder()
                .baseUrl(ServerConfig.host)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().registerTypeAdapterFactory(new DeserializeActionFactory()).create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
