package com.spirited.support.utils;

import android.content.Context;
import android.widget.ImageView;

import com.spirited.support.R;
import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        ImageUtil.normal(context, (String) path, R.drawable.default_image_white, imageView);
    }
}
