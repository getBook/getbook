package com.xfzj.getbook.views.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ListView;

/**
 * Created by zj on 2016/4/11.
 */
public class BaseListView extends ListView {

    private int mTouchSlop;
    private int downY,lastY;
    private OnScrollCallBack onScrollCallBack;
    public BaseListView(Context context) {
        this(context,null);
    }

    public BaseListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BaseListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public BaseListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public void setOnScrollCallBack(OnScrollCallBack onScrollCallBack) {
        this.onScrollCallBack = onScrollCallBack;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                lastY = (int) ev.getRawY();
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
            if (null != onScrollCallBack) {
                onScrollCallBack.onScroll(isPullUp());
            }
    }

    public interface OnScrollCallBack {
        void onScroll(boolean b);
    }

    private boolean isPullUp() {
        if ((downY - lastY) > mTouchSlop) {
            lastY = 0;
            return true;
        }
        return false;
    }
}
