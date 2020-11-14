package com.spirited.support.utils;

import android.content.Context;
import android.widget.Toast;

import com.appframe.utils.logger.Logger;

/**
 * Toast 工具类
 */

public class ToastUtil {

    private static Toast toast;

    public static void show(Context context, String content) {
        if (context == null) {
            Logger.getLogger().e("---->context为空");
            return;
        }

        Context cont = context.getApplicationContext();
        if (toast == null) {
            toast = Toast.makeText(cont, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }

        toast.show();
    }

    public static void show(Context context, int contentStr) {
        if (context == null) {
            Logger.getLogger().e("---->context为空");
            return;
        }

        Context cont = context.getApplicationContext();
        String content = cont.getResources().getString(contentStr);

        if (toast == null) {
            toast = Toast.makeText(cont, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }
}
