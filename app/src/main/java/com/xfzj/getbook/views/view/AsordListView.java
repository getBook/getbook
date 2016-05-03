package com.xfzj.getbook.views.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.AsordBook;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zj on 2016/5/2.
 */
public class AsordListView extends FrameLayout {
    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.tvAuthor)
    TextView tvAuthor;
    @Bind(R.id.tvPublisher)
    TextView tvPublisher;
    @Bind(R.id.tvDate)
    TextView tvDate;
    @Bind(R.id.tvState)
    TextView tvState;
    private Context context;
    private AsordBook asordBook;

    public AsordListView(Context context) {
        this(context, null);
    }

    public AsordListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AsordListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public AsordListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.asordlist, null);
        ButterKnife.bind(this, view);
        addView(view);
    }

    public void update(AsordBook asordBook) {
        if (null == asordBook) {
            return;
        }
        this.asordBook = asordBook;
        tvName.setText(asordBook.getName());
        tvAuthor.setText(asordBook.getAuthor());
        tvPublisher.setText(asordBook.getPublisher());
        tvDate.setText(asordBook.getDate());
        tvState.setText(asordBook.getState());
    }
}
