package com.xfzj.getbook.views.view;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.Post;
import com.xfzj.getbook.common.UnreadPost;
import com.xfzj.getbook.utils.FaceConversionUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zj on 2016/3/28.
 */
public class UnreadPostView extends FrameLayout {

    @Bind(R.id.tvCount)
    TextView tvCount;
    @Bind(R.id.tvContent)
    TextView tvContent;
    @Bind(R.id.rl)
    RelativeLayout rl;
    private Context context;
    private UnreadPost unreadPost;


    public UnreadPostView(Context context) {
        this(context, null);
    }

    public UnreadPostView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UnreadPostView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public UnreadPostView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.unreadpost, null);
        ButterKnife.bind(this, view);
        addView(view);
    }


    public void update(UnreadPost unreadPost) {
        if (null == unreadPost) {
            return;
        }
        this.unreadPost = unreadPost;
        tvCount.setText(context.getString(R.string.comment_count, unreadPost.getUnReadCount()));
        Post post = unreadPost.getPost();
        if (null == post) {
            tvContent.setText("");
        } else {
            String str = post.getContent();
            if (TextUtils.isEmpty(str)) {
                tvContent.setText("");
            } else {
                if(str.contains("[em]")) {
                    SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(context, str);
                    tvContent.setText(spannableString);
                }else{
                    tvContent.setText(str);
                }
            }
        }
    }

    @OnClick(R.id.rl)
    public void onClick() {
        if (null == unreadPost) {
            return;
        }
        Post post = unreadPost.getPost();
        if (null == post || TextUtils.isEmpty(post.getObjectId())) {
            if (null != onUnreadPostClick) {
                onUnreadPostClick.onUnreadPostClick(unreadPost, "");
            }
        }else{
            if (null != onUnreadPostClick) {
                onUnreadPostClick.onUnreadPostClick(unreadPost, post.getObjectId());
            }
        }
        
    }

    private OnUnreadPostClick onUnreadPostClick;

    public void setOnUnreadPostClick(OnUnreadPostClick onUnreadPostClick) {
        this.onUnreadPostClick = onUnreadPostClick;
    }

    public interface OnUnreadPostClick{
        void onUnreadPostClick(UnreadPost unreadPost, String id);
    }
    
    
    
}
