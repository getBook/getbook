package com.xfzj.getbook.views.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.News;

/**
 * Created by zj on 2016/3/16.
 */
public class NewsShowView extends FrameLayout implements View.OnClickListener {
    private Context context;
    private TextView tvTitle, tvDate;
    private RelativeLayout rl;
    private OnNewsShowClickListener onNewsShowClickListener;
    private News news;

    public NewsShowView(Context context) {
        this(context, null);
    }

    public NewsShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewsShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public NewsShowView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.newsshow_item, null);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvDate = (TextView) view.findViewById(R.id.tvDate);
        rl = (RelativeLayout) view.findViewById(R.id.rl);
        rl.setOnClickListener(this);
        addView(view);
    }

    public void update(News news) {
        if (null == news) {
            return;
        }
        this.news = news;

        tvTitle.setText(news.getTitle());
        tvDate.setText(news.getDate());

    }

    public void setOnNewsShowClickListener(OnNewsShowClickListener onNewsShowClickListener) {
        this.onNewsShowClickListener = onNewsShowClickListener;
    }

    @Override
    public void onClick(View v) {
        if (null != onNewsShowClickListener) {
            onNewsShowClickListener.onClick(news);
        }

    }

    public interface OnNewsShowClickListener {
        void onClick(News news);

    }


}
