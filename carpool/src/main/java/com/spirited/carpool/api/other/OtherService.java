package com.spirited.carpool.api.other;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 与其他相关接口
 */
public interface OtherService {

    /**********  用户反馈  **********/


    @POST("others/addFeedback")
    Observable<FeedbackEntity> feedback(@Body RequestBody body);
}
