package com.spirited.support.component;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.util.SimpleArrayMap;

/**
 * 避免ttf加载内存泄漏，实现缓存方式
 */

public class TypefaceHelper {
    private static final SimpleArrayMap<String, Typeface> TYPEFACE_CACHE = new SimpleArrayMap<String, Typeface>();

    public static Typeface get(Context context, String name) {
        synchronized (TYPEFACE_CACHE) {
            if (!TYPEFACE_CACHE.containsKey(name)) {
                try {
                    Typeface t = Typeface.createFromAsset(context.getAssets(), name);
                    TYPEFACE_CACHE.put(name, t);
                } catch (Exception e) {
                    return null;
                }
            }
            return TYPEFACE_CACHE.get(name);
        }
    }

    public static Typeface get(Context context) {
        return get(context,"SIMLI.TTF");
    }

    public static Typeface getExam(Context context){
        return get(context,"SIMLI.TTF");
    }
}
