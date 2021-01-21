package com.spirited.support.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

/**
 * 图片工具类
 */

public class ImageUtil {
    public static void loadViewBg(Context context, int resID, final View view) {
        if(view==null){
            return;
        }
        Glide.with(context)
                .load(resID)
                .apply(new RequestOptions().centerCrop())
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable bitmapDrawable, @Nullable Transition<? super Drawable> transition) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            view.setBackground(bitmapDrawable);
                        }else {
                            view.setBackgroundDrawable(bitmapDrawable);
                        }
                    }
                });
    }

    public static void loadImageCenterInside(Context context, String filePath, int defResID, final ImageView view) {
        if (context != null && filePath != null && !filePath.isEmpty() && view != null) {

            if (filePath.toLowerCase().endsWith("gif")) {
                Glide.with(context)
                        .asGif()
                        .load(filePath)
                        .apply(new RequestOptions().dontAnimate().placeholder(defResID).error(defResID))
                        .into(view);
            } else {
                Glide.with(context)
                        .load(filePath)
                        .apply(new RequestOptions().fitCenter().dontAnimate().placeholder(defResID).error(defResID))
                        .into(view);
            }
        }else {
            Log.e("loadImageCenterInside", "参数错误，有空指针");
        }
    }

    public static void loadImageCenterInside(Context context, int resID, int defResID, final ImageView view) {
        if (context != null && view != null) {

            Glide.with(context)
                    .load(resID)
                    .apply(new RequestOptions().fitCenter().dontAnimate().placeholder(defResID).error(defResID))
                    .into(view);
        }else {
            Log.e("loadImageCenterInside", "参数错误，有空指针");
        }
    }

    public static void loadImageCircle(Context context, int resID, int defResID, final ImageView view) {
        if (context != null && view != null) {
            Glide.with(context)
                    .load(resID)
                    .apply(new RequestOptions().dontAnimate()
                            .placeholder(defResID)
                            .error(defResID)
                            .transform(new GlideCircleTransform()))
                    .into(view);
        }else {
            Log.e("loadImageCircle", "参数错误，有空指针");
        }
    }

    public static void loadImageCircle(Context context, String filePath, int defResID, final ImageView view) {
        if (context != null && filePath != null && !filePath.isEmpty() && view != null) {

            if (filePath.toLowerCase().endsWith("gif")) {
                Glide.with(context)
                        .asGif()
                        .load(filePath)
                        .apply(new RequestOptions().dontAnimate()
                                .placeholder(defResID).error(defResID)
                                .transform(new GlideCircleTransform()))
                        .into(view);
            } else {
                Glide.with(context)
                        .load(filePath)
                        .apply(new RequestOptions().dontAnimate()
                                .placeholder(defResID).error(defResID)
                                .transform(new GlideCircleTransform()))
                        .into(view);
            }
        }else {
            Log.e("loadImageCircle", "参数错误，有空指针");
        }
    }
}
