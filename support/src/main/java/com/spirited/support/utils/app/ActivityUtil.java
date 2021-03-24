package com.spirited.support.utils.app;

import android.app.Activity;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

/**
 * 页面 工具类
 */

public class ActivityUtil {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isAvailable(Activity activity) {
        if (activity != null && !activity.isDestroyed() && !activity.isFinishing()) {
            return true;
        }
        return false;
    }

    public static boolean isAvailable(View view) {
        if (view != null && view.isShown() && view.getParent() != null) {
            return true;
        }
        return false;
    }
}
