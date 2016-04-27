package com.xfzj.getbook.views.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
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


    public void update(final Post post) {
        if (null == post) {
            return;
        }
            this.post = post;
        tvContent.setText(this.post.getContent());
        tvLike.setText(this.post.getLikeCount() + "");
        tvComment.setText(this.post.getCommentCount() + "");
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

            for (BmobFile file : pics) {
                picPaths.add(new PicPath(PicPath.FLAG_ALBUM, file.getFileUrl(context)));
            }
//            ivs = new ArrayList<>();
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
//            for (int i = 0; i < pics.size(); i++) {
//                NetImageView iv = new NetImageView(context);
//                FrameLayout.LayoutParams p = new FrameLayout.LayoutParams((int) (MyUtils.dp2px(context, NetImageView.SMALL_WIDTH)), (int) (MyUtils.dp2px(context, NetImageView.SMALL_HEIGHT)));
//                iv.setLayoutParams(p);
//                iv.setBmobthumbnail(pics.get(i), NetImageView.SMALL_WIDTH, NetImageView.SMALL_HEIGHT);
//                iv.setAdjustViewBounds(true);
//                iv.setScaleType(ImageView.ScaleType.FIT_XY);
//                final int index = i;
//                iv.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(context, ViewPagerAty.class);
//                        intent.putExtra(ViewPagerAty.PATH, (Serializable) picPaths);
//                        intent.putExtra(ViewPagerAty.INDEX, index);
//                        intent.putExtra(ViewPagerAty.FROM, ViewPagerAty.VIEW);
//                        context.startActivity(intent);
//                    }
//                });
//                ivs.add(iv);
//                llPics.addView(iv);
//            }
        } else {
            picShowView.setVisibility(GONE);
        }


    }

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

    public void setLikeSuccess() {
        Drawable drawable = ContextCompat.getDrawable(context, R.mipmap.like_press);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        tvLike.setCompoundDrawables(drawable, null, null, null);
        setLikeCount();
        tvLike.setTextColor(context.getResources().getColor(R.color.primary));

    }

    public void setLikeFail() {
        Drawable drawable = ContextCompat.getDrawable(context, R.mipmap.like);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        tvLike.setCompoundDrawables(drawable, null, null, null);
        setLikeCount();
        tvLike.setTextColor(context.getResources().getColor(R.color.secondary_text));
    }

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

    @Override
    public Post getTag() {
        return post;
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
                    onPostClickListener.onLikeClick(post);

                }
                break;
            case R.id.tvComment:
                if (null != onPostClickListener) {
                    onPostClickListener.onCommentClick(post);

                }
                break;
        }
    }


    public interface OnPostClickListener {
        void onContentClick(Post post);

        void onTopicClick(Post post, String topic);

        void onLikeClick(Post post);

        void onCommentClick(Post post);
    }

}
