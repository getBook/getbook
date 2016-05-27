package com.xfzj.getbook.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by zj on 2016/5/8.
 */
public class ResizeFrameLayout extends FrameLayout {
    public ResizeFrameLayout(Context context) {
        super(context);
    }

    public ResizeFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResizeFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ResizeFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (h < oldh) {
            if (null != onKeyBoardListener) {
                onKeyBoardListener.onKeyBoardShow();
            }
        } else if (h > oldh) {
            if (null != onKeyBoardListener) {
                onKeyBoardListener.onKeyBoardHide();
            }
        }
      
    }

    private OnKeyBoardListener onKeyBoardListener;

    public void setOnKeyBoardListener(OnKeyBoardListener onKeyBoardListener) {
        this.onKeyBoardListener = onKeyBoardListener;
    }

    public interface OnKeyBoardListener{
        void onKeyBoardShow();

        void onKeyBoardHide();
    }
}
