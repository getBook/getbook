package com.xfzj.getbook.views.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.action.CommentAction;
import com.xfzj.getbook.action.LikeAction;
import com.xfzj.getbook.activity.ViewPagerAty;
import com.xfzj.getbook.common.PicPath;
import com.xfzj.getbook.common.Post;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.utils.FaceConversionUtil;
import com.xfzj.getbook.utils.MyUtils;
import com.xfzj.getbook.views.gridview.PicAddView;
import com.xfzj.getbook.views.gridview.PicShowView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by zj on 2016/4/17.
 */
public class PostShowView extends FrameLayout implements View.OnClickListener {
    private Context context;
    @Bind(R.id.tvContent)
    TextView tvContent;
    @Bind(R.id.llTopic)
    LinearLayout llTopic;
    @Bind(R.id.tvLike)
    TextView tvLike;
    @Bind(R.id.tvComment)
    TextView tvComment;
    private Post post;
    @Bind(R.id.picShowView)
    PicShowView picShowView;
    private OnPostClickListener onPostClickListener;
    private List<ImageView> ivs;
    private List<PicPath> picPaths = new ArrayList<>();

    public PostShowView(Context context) {

        this(context, null);
    }

    public PostShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PostShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public PostShowView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.post_show_layout, null);
        ButterKnife.bind(this, view);
        tvComment.setOnClickListener(this);
        tvLike.setOnClickListener(this);
        tvContent.setOnClickListener(this);
        addView(view);
    }

    /**
     * 
     * @param post
     * @param onLikedListener 本人是否点赞的回调
     */
    public void update(final Post post,OnLikedListener onLikedListener) {
        if (null == post) {
            return;
        }
            this.post = post;
        SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(context, post.getContent());
        tvContent.setText(spannableString);
        tvLike.setText(post.getLikeCount() + "");
        tvComment.setText(post.getCommentCount() + "");
        if (post.getLikeState() == Post.UNKNOWNLIKESTATE) {
            setIsLiked(onLikedListener);
        }else{
            setLikeState(post.getLikeState());
        }
      
        if (this.post.getCommentCount()==-1) {
            setCommentCount();
        }
        String[] topics = this.post.getTopic();
        if (topics == null) {
            llTopic.setVisibility(GONE);
        } else {
            llTopic.removeAllViews();
            llTopic.setVisibility(VISIBLE);
            for (String topic : topics) {
                TextView tvTopic = new TextView(context);
                tvTopic.setText(topic);
                tvTopic.setBackgroundResource(R.drawable.tv_select);
                tvTopic.setTextColor(context.getResources().getColor(R.color.white));
                int padding = (int) MyUtils.dp2px(context, 2);
                tvTopic.setPadding(padding, padding, padding, padding);
                final String t = topic;
                tvTopic.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != onPostClickListener) {
                            onPostClickListener.onTopicClick(post, t);
                        }
                    }
                });
                llTopic.addView(tvTopic);
            }
        }
        List<BmobFile> pics = this.post.getFiles();
        if (null != pics && pics.size() > 0) {
            picShowView.deleteAll();
            picPaths.clear();
            for (BmobFile file : pics) {
                picPaths.add(new PicPath(PicPath.FLAG_ALBUM, file.getFileUrl(context)));
            }
            picShowView.setVisibility(VISIBLE);
            picShowView.addAll(picPaths);
            picShowView.removeLast();
            picShowView.setOnItemClick(new PicAddView.OnItemClick() {
                @Override
                public void onAddClick(int position, int size, int maxPics) {

                }

                @Override
                public void onPicClick(int position, String path) {
                    Intent intent = new Intent(context, ViewPagerAty.class);
                    intent.putExtra(ViewPagerAty.PATH, (Serializable) picPaths);
                    intent.putExtra(ViewPagerAty.INDEX, position);
                    intent.putExtra(ViewPagerAty.FROM, ViewPagerAty.VIEW);
                    context.startActivity(intent);
                }
            });
        } else {
            picShowView.setVisibility(GONE);
        }


    }

    /**
     * 设置点赞的状态
     * @param likeState
     */
    private void setLikeState(int likeState) {
        if (likeState == Post.NOLIKESTATE) {
            setNotLikedDrawable();
        } else if (likeState == Post.LIKEDSTATE) {
            setLikedDrawable();
        }
        
    }

    /**
     * 设置评论的数量
     */
    private void setCommentCount() {
        CommentAction commentAction = new CommentAction(context);
        commentAction.queryCount(post, new CountListener() {
            @Override
            public void onSuccess(int i) {
                tvComment.setText(i + "");
                post.setCommentCount(i);
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    /**
     * 点赞成功后的方法
     */
    public void setLikeSuccess() {
        setLikedDrawable();
        setLikeCount();
    }

    /**
     * 设置点赞后的图标颜色
     */
    private void setLikedDrawable() {
        Drawable drawable = ContextCompat.getDrawable(context, R.mipmap.like_press);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        tvLike.setCompoundDrawables(drawable, null, null, null);
        tvLike.setTextColor(context.getResources().getColor(R.color.primary));
    }

    /**
     * 设置未点赞的图标颜色
     */
    private void setNotLikedDrawable() {
        Drawable drawable = ContextCompat.getDrawable(context, R.mipmap.like);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        tvLike.setCompoundDrawables(drawable, null, null, null);
        tvLike.setTextColor(context.getResources().getColor(R.color.secondary_text));
    }

    /**
     * 点赞失败后的方法
     */
    public void setLikeFail() {
        setNotLikedDrawable();
        setLikeCount();
       
    }

    /**
     * 设置点赞的数量
     */
    private void setLikeCount() {
        LikeAction likeAction = new LikeAction(context);
        likeAction.queryLikeCount(post);
        likeAction.setOnLikeCountListener(new LikeAction.OnLikeCountListener() {
            @Override
            public void onLikeCount(int i) {
                tvLike.setText(i + "");
                post.setLikeCount(i);
            }
        });
    }

 

    public void setOnPostClickListener(OnPostClickListener onPostClickListener) {
        this.onPostClickListener = onPostClickListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvContent:
                if (null != onPostClickListener) {
                    onPostClickListener.onContentClick(post);

                }
                break;
            case R.id.tvLike:
                if (null != onPostClickListener) {
                    onPostClickListener.onLikeClick();

                }
                break;
            case R.id.tvComment:
                if (null != onPostClickListener) {
                    onPostClickListener.onCommentClick();

                }
                break;
        }
    }

    /**
     * 判断是否已经被本人点赞
     * @param onLikedListener
     */
    private void setIsLiked(final OnLikedListener onLikedListener) {
        LikeAction likeAction = new LikeAction(context);
        likeAction.querySelfLiked(this.post, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                if (null != list && list.size() > 0) {
                    setLikedDrawable();
                    if (null != onLikedListener) {
                        onLikedListener.isLiked();
                    }
                }else{
                    setNotLikedDrawable();
                    if (null != onLikedListener) {
                        onLikedListener.isNotLiked();
                    }
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    public interface OnPostClickListener {
        void onContentClick(Post post);

        void onTopicClick(Post post, String topic);

        void onLikeClick();

        void onCommentClick();
    }
    public interface OnLikedListener{
        void isLiked();

        void isNotLiked();
    }

}
