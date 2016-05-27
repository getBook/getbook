package com.xfzj.getbook.views.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.Comment;
import com.xfzj.getbook.utils.FaceConversionUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zj on 2016/5/9.
 */
public class PostDeatilCommentView extends FrameLayout {

    @Bind(R.id.tvFloor)
    TextView tvFloor;
    @Bind(R.id.tvResponseFloor)
    TextView tvResponseFloor;
    @Bind(R.id.tvComment)
    TextView tvComment;
    @Bind(R.id.llResponse)
    LinearLayout llResponse;
    @Bind(R.id.cardView)
    CardView cardView;
    private Context context;
    private Comment comment;

    public PostDeatilCommentView(Context context) {
        this(context, null);
    }

    public PostDeatilCommentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PostDeatilCommentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }


    public PostDeatilCommentView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.post_detail_comment, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        ButterKnife.bind(this, view);
        addView(view);
    }

    public void update(Comment comment) {
        if (null == comment) {
            return;
        }
        this.comment = comment;
        tvFloor.setText(comment.getFloor() + "楼");
        if (null == comment.getComment()) {
            llResponse.setVisibility(GONE);
        } else {
            llResponse.setVisibility(VISIBLE);
            tvResponseFloor.setText(comment.getComment().getFloor() + "");
        }
        if (comment.getContent().contains("[em]")) {
            tvComment.setText(FaceConversionUtil.getInstace().getExpressionString(context, comment.getContent()));
        } else {
            tvComment.setText(comment.getContent());
        }
    }


    @OnClick(R.id.cardView)
    public void onClick() {
        if (null != onCommentClickListener) {
            onCommentClickListener.onCommentClick(comment);
        }
    }

    private OnCommentClickListener onCommentClickListener;

    public void setOnCommentClickListener(OnCommentClickListener onCommentClickListener) {
        this.onCommentClickListener = onCommentClickListener;
    }

    public interface OnCommentClickListener {
        void onCommentClick(Comment comment);
    }

}
