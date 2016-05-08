package com.xfzj.getbook.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by zj on 2016/5/8.
 */
public class ResizeLayout extends LinearLayout {
    public ResizeLayout(Context context) {
        super(context);
    }

    public ResizeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResizeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ResizeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (null != onResizeListener) {
            onResizeListener.onResize(w, h, oldw, oldh);
        }
    }

    private OnResizeListener onResizeListener;

    public void setOnResizeListener(OnResizeListener onResizeListener) {
        this.onResizeListener = onResizeListener;
    }

    public interface OnResizeListener{
        void onResize(int w, int h, int oldw, int oldh);
    }
}
