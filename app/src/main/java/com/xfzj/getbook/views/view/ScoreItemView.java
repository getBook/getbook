package com.xfzj.getbook.views.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.Score;

/**
 * Created by zj on 2016/3/14.
 */
public class ScoreItemView extends FrameLayout {

    private Context context;
    private TextView tvCourse, tvXueFen, tvScore;

    public ScoreItemView(Context context) {
        this(context, null);
    }

    public ScoreItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScoreItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ScoreItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.score_item, null);
        tvCourse = (TextView) view.findViewById(R.id.tvCourse);
        tvXueFen = (TextView) view.findViewById(R.id.tvXueFen);
        tvScore = (TextView) view.findViewById(R.id.tvScore);
        addView(view);
    }

    public void update(Score score) {
        if (null == score) {
            return;
        }
        tvCourse.setText(score.getKCMC());
        tvXueFen.setText(score.getXF());
        String str = score.getCJ();
        try {
            int i = Integer.parseInt(str);
            if (i < 60) {
                tvScore.setTextColor(Color.RED);
            }else if (i >= 90) {
                tvScore.setTextColor(context.getResources().getColor(R.color.primary));
            }else {
                tvScore.setTextColor(context.getResources().getColor(R.color.primary_text));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            if ("优秀".equals(str)) {
                tvScore.setTextColor(context.getResources().getColor(R.color.primary));
            } else  {
                tvScore.setTextColor(context.getResources().getColor(R.color.primary_text));
            }
        }
        tvScore.setText(str);
    }
}
