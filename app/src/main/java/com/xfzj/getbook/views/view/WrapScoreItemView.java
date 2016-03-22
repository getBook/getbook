package com.xfzj.getbook.views.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.Score;
import com.xfzj.getbook.utils.MyUtils;

import java.util.List;

/**
 * Created by zj on 2016/3/14.
 */
public class WrapScoreItemView extends FrameLayout {

    private Context context;
    private LinearLayout ll;
    public WrapScoreItemView(Context context) {
        this(context, null);
    }

    public WrapScoreItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WrapScoreItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public WrapScoreItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.wrap_score_item, null);
        ll = (LinearLayout) view.findViewById(R.id.ll);
        addView(view);
    }


    public void update(List<Score> scores) {
        if (null == scores || scores.size() == 0) {
            return ;
        }
        for (int i=0;i<scores.size();i++) {
            ScoreItemView scoreItemView = new ScoreItemView(context);
            scoreItemView.update(scores.get(i));
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int margin =(int) MyUtils.dp2px(context, 15f);
            lp.setMargins(margin, margin / 2, margin, margin / 2);
            scoreItemView.setLayoutParams(lp);
            
            
            ll.addView(scoreItemView);
            if (i != scores.size() - 1) {
                ImageView imageView = new NetImageView(context);
                FrameLayout.LayoutParams p = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) MyUtils.dp2px(context, 1f));
                imageView.setLayoutParams(p);
                imageView.setBackgroundColor(context.getResources().getColor(R.color.divider));
                ll.addView(imageView);
            }
        }
        
        
        
        
        
    }
}
