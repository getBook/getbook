package com.xfzj.getbook.views.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Created by zj on 2016/4/11.
 * 增加滑动监听
 */
public class BaseScrollView extends ScrollView {
    private int downY, lastY;
    private int mTouchSlop;
    private OnScrollCallBack onScrollCallBack;

    public BaseScrollView(Context context) {
        this(context, null);
    }

    public BaseScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public BaseScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (null != onScrollCallBack) {
            onScrollCallBack.onScroll(isPullUp());
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                lastY = (int) event.getRawY();
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setOnScrollCallBack(OnScrollCallBack onScrollCallBack) {
        this.onScrollCallBack = onScrollCallBack;
    }

    public interface OnScrollCallBack {
        void onScroll(boolean b);
    }

    private boolean isPullUp() {
        return (downY - lastY) > mTouchSlop;
    }
}
