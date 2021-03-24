package com.spirited.support.network.interceptor;

import android.text.TextUtils;

import com.spirited.support.logger.Logger;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 拦截http请求信息
 */
public class HttpLogger implements Interceptor {
    private final Charset UTF8 = Charset.forName("UTF-8");

    private static HttpLogger instance = null;

    private HttpLogger() {

    }

//   http 日志信息
    public static HttpLogger getInstance() {
        if (instance == null) {
            synchronized (HttpLogger.class) {
                instance = new HttpLogger();
            }
        }
        return instance;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String logMsg = "";

        // 请求信息
        Request request = chain.request();
//        请求体
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;
//        连接信息
        Connection connection = chain.connection();
//        请求协议
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
//        请求方式，请求地址，请求协议
        logMsg += "Request: \n  " + request.method() + ' ' + request.url() + "\n";
        logMsg += "  protocol: " + protocol + "\n";
        if (hasRequestBody) {
            logMsg += "Request Headers:\n";
//            contentType
            if (requestBody.contentType() != null) {
                logMsg += "  Content-Type: " + requestBody.contentType() + "\n";
            }
            if (requestBody.contentLength() != -1) {
                logMsg += "  Content-Length: " + requestBody.contentLength() + "\n";
            }
        }

        Headers headers = request.headers();
        String headerType = "";
        for (int i = 0; i < headers.size(); i++) {
            String name = headers.name(i);
            if (name.equals("type")){
                headerType = headers.value(i);
            }
//            打印其他头部信息
            if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                logMsg += "  " + name + ": " + headers.value(i) + "\n";
            }
        }

        if (!hasRequestBody) {
            logMsg += "Request Body: (0-byte body) \n\n";
        } else if (bodyEncoded(request.headers())) {
            logMsg += "Request Body: (encoded body omitted)\n\n";
        } else {
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
//                Logger.getLogger().d("—type："+contentType.type()+"，subType："+contentType.subtype()+"，string："+contentType.toString());
                if (contentType.subtype().equals("json") || contentType.subtype().equals("x-www-form-urlencoded")) {
                    Buffer buffer = new Buffer();
                    requestBody.writeTo(buffer);

                    Charset charset = contentType.charset(UTF8);
                    if (isPlaintext(buffer)) {
                        String content = buffer.readString(charset);
                        logMsg += "Request Body: (" + requestBody.contentLength() + "-byte body) \n";
                        // 判断类型是否需要打印content
                        if (!TextUtils.isEmpty(headerType) && headerType.equals("uploadLog")){
                            try {
                                if (content.length() > 200) {
                                    logMsg += "  " + content.substring(0, 200) + "......\n\n";
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {
                            logMsg += "  " + content + "\n\n";
                        }
                    } else {
                        logMsg += "Request Body: (binary "
                                + requestBody.contentLength() + "-byte body omitted)" + "\n\n";
                    }
                } else {
                    logMsg += "Request Body: (encoded body omitted)\n\n";
                }
            }else {
                logMsg += "Request Body: (contentType is null)\n\n";
            }
        }

        long startNs = System.nanoTime();

        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            logMsg += "Response: \n  HTTP FAILED: " + e + "\n";
            Logger.getLogger().e(logMsg);
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        logMsg += "Response: " + "(" + tookMs + "ms" + ")\n  " + response.code() + ' ' + response.message() + "\n";
        logMsg += "Response Headers:\n";
        Headers respHeaders = response.headers();
        for (int i = 0, count = respHeaders.size(); i < count; i++) {
            logMsg += "  " + respHeaders.name(i) + ": " + respHeaders.value(i) + "\n";
        }

        if (!HttpHeaders.hasBody(response)) {
            logMsg += "Response Body: (0-byte body)";
        } else if (bodyEncoded(response.headers())) {
            logMsg += "Response Body: (encoded body omitted)";
        } else {
            if (contentLength == -1) {
                logMsg += "Response Body: (chunked)\n" ;
            } else {
                logMsg += "Response Body: (" + contentLength + "-byte body)\n" ;
            }

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                if (contentType.subtype().equals("json") || contentType.subtype().equals("plain")) {
                    BufferedSource source = responseBody.source();
                    source.request(Long.MAX_VALUE); // Buffer the entire body.
                    Buffer buffer = source.buffer();

                    if (!isPlaintext(buffer)) {
                        logMsg += "" + "\n";
                        logMsg += "  (binary " + buffer.size() + "-byte body omitted)";
                        Logger.getLogger().d(logMsg);

                        return response;
                    }

                    if (contentLength != 0) {
                        String content = buffer.clone().readString(charset);
                        logMsg += content;
                    }
                } else {
                    logMsg += "";
                }
            }
        }
        Logger.getLogger().d(logMsg);

        return response;
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    private boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                if (Character.isISOControl(prefix.readUtf8CodePoint())) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }
}
