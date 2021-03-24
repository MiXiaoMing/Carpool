package com.spirited.support.network;


import com.spirited.support.network.interceptor.HttpLogger;
import com.spirited.support.network.interceptor.RetryInterceptor;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * httpClient基类
 */
// TODO: 2019/8/12 需要添加页面监听
public class AFHttpClient {
    private static OkHttpClient httpClient = getOkHttpClient();

    public static OkHttpClient getInstance() {
        return httpClient;
    }

    private static OkHttpClient getOkHttpClient() {
        if (httpClient == null) {
            final OkHttpClient.Builder builder = new OkHttpClient.Builder();

            builder.retryOnConnectionFailure(true);
            builder.connectTimeout(10, TimeUnit.SECONDS);
            builder.readTimeout(20, TimeUnit.SECONDS);
            builder.writeTimeout(20, TimeUnit.SECONDS);

            // 添加失败重连
            builder.addInterceptor(new RetryInterceptor());
            // 添加日志
            builder.addInterceptor(HttpLogger.getInstance());

            //ssl
            X509TrustManager trustAllCerts = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }


                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }


                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };

            try {
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, new TrustManager[]{trustAllCerts}, new SecureRandom());
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                builder.sslSocketFactory(sslSocketFactory);
                builder.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                e.printStackTrace();
            }

            httpClient = builder.build();
        }
        return httpClient;
    }

    public static void setHttpClient(OkHttpClient okHttpClient) {
        httpClient = okHttpClient;
    }
}
