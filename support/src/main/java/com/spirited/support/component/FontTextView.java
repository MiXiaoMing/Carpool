package com.spirited.support.component;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 自定义的字体显示组件
 */

public class FontTextView extends TextView {

    public FontTextView(Context context) {
        super(context);
        init();
    }

    public FontTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FontTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
        init();
    }

    private void init() {
        Typeface tf = TypefaceHelper.get(this.getContext(), "SIMLI.TTF");
        setTypeface(tf);
    }
}
