package com.spirited.support.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.spirited.support.R;
import com.spirited.support.constants.Constants;
import com.spirited.support.logger.Logger;

/**
 * 图片工具类
 */

public class ImageUtil {
    /**
     * 圆形图片：本地资源
     * @param context
     * @param resID
     * @param view
     */
    public static void normal(Context context, int resID, ImageView view) {
        if (context != null && view != null) {
            Glide.with(context).load(resID).apply((new RequestOptions()).dontAnimate()
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)).into(view);
        } else {
            Logger.getLogger().e("参数错误，有空指针");
        }
    }

    public static void normal(final Context context, final String filePath, final int defaultRes, final ImageView imageView) {
        if (context != null && filePath != null && !filePath.isEmpty() && imageView != null) {
            ViewTreeObserver vto = imageView.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                    int height = imageView.getMeasuredHeight();
                    int width = imageView.getMeasuredWidth();
                    if (width != 0 && height != 0) {
                        String finalFilePath = filePath;
                        if (filePath.startsWith("http")) {
                            finalFilePath = finalFilePath + "?rw=" + width + "&rh=" + height;
                        }

                        if (filePath.toLowerCase().endsWith("gif")) {
                            Glide.with(context).asGif().load(finalFilePath).apply((new RequestOptions()).override(width, height).centerCrop().dontAnimate().placeholder(defaultRes).error(defaultRes)).into(imageView);
                        } else {
                            Glide.with(context).load(finalFilePath).apply((new RequestOptions()).override(width, height).centerCrop().dontAnimate().placeholder(defaultRes).error(defaultRes)).into(imageView);
                        }

                        return true;
                    } else {
                        if (filePath.toLowerCase().endsWith("gif")) {
                            Glide.with(context).asGif().load(filePath).apply((new RequestOptions()).centerCrop().dontAnimate().placeholder(defaultRes).error(defaultRes)).into(imageView);
                        } else {
                            Glide.with(context).load(filePath).apply((new RequestOptions()).centerCrop().dontAnimate().placeholder(defaultRes).error(defaultRes)).into(imageView);
                        }

                        return true;
                    }
                }
            });
        } else {
            Logger.getLogger().e("参数错误，有空指针");
        }
    }

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
