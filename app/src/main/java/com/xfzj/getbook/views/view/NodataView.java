package com.xfzj.getbook.views.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.xfzj.getbook.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zj on 2016/5/1.
 */
public class NodataView extends FrameLayout {

    @Bind(R.id.btn)
    Button btn;
    @Bind(R.id.llError)
    LinearLayout llError;
    private Context context;
    private OnClickListener onClickListener;

    public NodataView(Context context) {
        this(context, null);
    }

    public NodataView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NodataView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public NodataView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.no_data, null);
        ButterKnife.bind(this, view);
        addView(view);

    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void show() {
        llError.setVisibility(VISIBLE);
    }
    @OnClick(R.id.btn)
    public void onClick() {
        if (null != onClickListener) {
            onClickListener.onClick(btn);
        }
    }
    
    
}
