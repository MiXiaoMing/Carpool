package com.spirited.carpool.api.other;

import com.spirited.carpool.api.MobileServerRetrofit;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 其他接口
 */
public class OtherDataManager {
    private OtherService service;

    public OtherDataManager() {
        service = MobileServerRetrofit.getInstance().getRetrofit().create(OtherService.class);
    }

    /**********  用户反馈  **********/

    public Observable<FeedbackEntity> feedback(String content) {
        return service.feedback(RequestBody.create(MediaType.parse("text/plain"), content));
    }
}
