package com.xfzj.getbook.views.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.Score;

import java.util.List;

/**
 * Created by zj on 2016/3/14.
 */
public class WrapWrapScoreItemView extends FrameLayout {

    private Context context;
    private TextView tv;
    private WrapScoreItemView wrapScoreItemView;
    public WrapWrapScoreItemView(Context context) {
        this(context, null);
    }

    public WrapWrapScoreItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WrapWrapScoreItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public WrapWrapScoreItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.wrap_wrap_score_item, null);
        wrapScoreItemView = (WrapScoreItemView) view.findViewById(R.id.wrapScoreItemView);
        tv = (TextView) view.findViewById(R.id.tv);
        addView(view);
    }


    public void update(List<Score> scores) {
        if (null == scores || scores.size() == 0) {
            return ;
        }
        Score score = scores.get(0);
        String xq = score.getXQ();

        if ("1".equals(xq)) {
            tv.setText(score.getXN() + " " + "上学期");
        }else {
            tv.setText(score.getXN() + " " + "下学期");
        }
        wrapScoreItemView.update(scores);
    }
}
