package com.xfzj.getbook.views.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;

/**
 * Created by zj on 2016/2/26.
 */
public class BaseToolBar extends RelativeLayout {
    private TextView tv1, tv2, tv3;


    private Toolbar toolbar;
    private Context context;

    public TextView getTv1() {
        return tv1;
    }

    public TextView getTv2() {
        return tv2;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public TextView getTv3() {
        return tv3;
    }

    public BaseToolBar(Context context) {
        this(context, null);
    }

    public BaseToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public BaseToolBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.basetoolbar, null);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        tv1 = (TextView) v.findViewById(R.id.tvPublish);
        tv2 = (TextView) v.findViewById(R.id.tvSelect);
        tv3 = (TextView) v.findViewById(R.id.tvMiddle);
        this.context = context;
        addView(v);
    }


    public void initToolbar(AppCompatActivity aty, String title) {
        toolbar.setTitle(title);
        aty.setSupportActionBar(toolbar);


    }

    public void canBack(AppCompatActivity aty) {
        aty.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


}
