package com.spirited.support.component;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.View;

public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
    private Context context;
    private int interval;

    public RecyclerViewItemDecoration(Context context, int interval) {
        this.context = context;
        this.interval = interval;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        // 找到最后一个完全可见项的位置
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            // 获取item在span中的下标
            int spanIndex = params.getSpanIndex();
            int interval = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    this.interval, context.getResources().getDisplayMetrics());
            // 中间间隔
            if (spanIndex % 2 == 0) {
                outRect.left = 0;
            } else {
                // item为奇数位，设置其左间隔为5dp
                outRect.left = interval;
            }
            // 下方间隔
            outRect.bottom = interval;
        } else if (layoutManager instanceof LinearLayoutManager) {
            if (parent.getChildAdapterPosition(view) != 0)
                outRect.top = interval;
        }
    }
}