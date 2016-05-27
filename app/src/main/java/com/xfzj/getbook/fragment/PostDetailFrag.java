package com.xfzj.getbook.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.action.CommentAction;
import com.xfzj.getbook.action.LikeAction;
import com.xfzj.getbook.action.QueryAction;
import com.xfzj.getbook.common.Comment;
import com.xfzj.getbook.common.Post;
import com.xfzj.getbook.utils.InputMethodManagerUtils;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.utils.MyUtils;
import com.xfzj.getbook.utils.SharedPreferencesUtils;
import com.xfzj.getbook.views.ResizeFrameLayout;
import com.xfzj.getbook.views.recycleview.LoadMoreLayout;
import com.xfzj.getbook.views.recycleview.LoadMoreView;
import com.xfzj.getbook.views.recycleview.SubHFLoadMoreRVAdapter;
import com.xfzj.getbook.views.view.EmojiView;
import com.xfzj.getbook.views.view.PostDeatilCommentView;
import com.xfzj.getbook.views.view.PostShowView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zj on 2016/3/21.
 */
public class PostDetailFrag extends BaseFragment implements QueryAction.OnQueryListener<List<Comment>>, LoadMoreView.RefreshListener, LoadMoreLayout.OnScrollCallBack, ResizeFrameLayout.OnKeyBoardListener, EmojiView.OnCorpusSelectedListener, TextWatcher, PostDeatilCommentView.OnCommentClickListener, View.OnFocusChangeListener {

    public static final String PARAM = "PostDetailFrag.class";
    @Bind(R.id.loadMoreView)
    LoadMoreView loadMoreView;
    @Bind(R.id.etComment)
    EditText etComment;
    @Bind(R.id.ivEmoji)
    ImageView ivEmoji;
    @Bind(R.id.btnSend)
    TextView btnSend;
    @Bind(R.id.llComment)
    LinearLayout llComment;
    @Bind(R.id.frame)
    ResizeFrameLayout frame;
    @Bind(R.id.emojiView)
    EmojiView emojiView;
    private String param;


    private String title;
    private Post post;
    private PostDetailAdapter postDetailAdapter;
    private List<Comment> lists = new ArrayList<>();
    /**
     * emoji表情是否显示
     */
    private boolean isEmojiShow;
    /**
     * 键盘是否弹出
     */
    private boolean isKeyBoardShow;
    private int maxLength = 140;
    /**
     * 点击的评论
     */
    private Comment commemt;
    private PostShowView postShowView;

    private static PostDetailFrag postDetailFrag;

    public PostDetailFrag() {

    }


    public static PostDetailFrag newInstance(String param) {
        if (null == postDetailFrag) {
            postDetailFrag = new PostDetailFrag();
            Bundle bundle = new Bundle();
            bundle.putString(PARAM, param);
            postDetailFrag.setArguments(bundle);
        }
        return postDetailFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            param = getArguments().getString(PARAM);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);
        ButterKnife.bind(this, view);
        postDetailAdapter = new PostDetailAdapter(lists, getActivity());
        loadMoreView.setAdapter(postDetailAdapter);
        loadMoreView.setCanLoadMore(false);
        loadMoreView.setOnrefreshListener(this);
        loadMoreView.setOnScrollCallBack(this);
        MyUtils.getSoftKeyBoardHeight(getActivity(), frame, etComment);
        frame.setOnKeyBoardListener(this);
        emojiView.setOnCorpusSelectedListener(this);
        etComment.addTextChangedListener(this);
        etComment.setOnFocusChangeListener(this);
        emojiView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                emojiView.getViewTreeObserver().removeOnPreDrawListener(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.height = SharedPreferencesUtils.getSoftKeyBoard(getActivity());
                emojiView.setLayoutParams(lp);
                return true;
            }

        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void setPost(Post post) {
        if (null == post) {
            getParentFragment().getFragmentManager().popBackStack();
            return;
        }
        this.post = post;
        onRefresh();
    }

    /**
     * 获取评论数据
     */
    private void queryComment() {
        QueryAction queryAction = new QueryAction(getActivity());
        queryAction.queryComment(post, PostDetailFrag.this);
    }

    @Override
    public void onSuccess(List<Comment> comments) {
        if (null == loadMoreView) {
            return;
        }
        loadMoreView.setRefreshFinish();
        postDetailAdapter.clear();
        postDetailAdapter.addAll(comments);

    }

    @Override
    public void onFail() {

    }

    @Override
    public void onRefresh() {
        if (null != loadMoreView) {
            loadMoreView.setRefreshing();
        }
        queryComment();
    }

    @Override
    public void onScroll(boolean b) {
        if (b) {
            llComment.setVisibility(View.GONE);
        } else {
            llComment.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.ivEmoji, R.id.btnSend})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivEmoji:
                if (emojiView.getVisibility() == View.VISIBLE) {
                    emojiView.setVisibility(View.GONE);
                    isEmojiShow = false;
                } else {
                    emojiView.setVisibility(View.VISIBLE);
                    isEmojiShow = true;
                    if (isKeyBoardShow) {
                        InputMethodManagerUtils.hide(getActivity(), etComment);
                    }
                }
                break;
            case R.id.btnSend:
                String content = etComment.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    MyToast.show(getActivity(), getActivity().getString(R.string.please_to_input, "评论"));
                    return;
                }
                uploadComment(content);
                break;
        }
    }

    /**
     * 评论成功时，刷新帖子信息
     */
    private void refreshPost() {
        LikeAction likeAction = new LikeAction(getActivity());
        likeAction.queryLikeCount(post, new LikeAction.OnLikeCountListener() {
            @Override
            public void onLikeCount(int i) {
                post.setLikeCount(i);
                CommentAction commentAction = new CommentAction(getActivity());
                commentAction.queryCount(post, new CommentAction.OnCountListener() {
                    @Override
                    public void onCommentCount(int i) {
                        post.setCommentCount(i);
                        updateHeadView(postShowView);
                        SharedPreferencesUtils.saveFocusPost(getActivity().getApplicationContext(), post.getObjectId(), i);
                    }
                });
            }
        });

    }

    private void uploadComment(String content) {
        final ProgressDialog pd = ProgressDialog.show(getActivity(), "", "正在发表...");
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
        Comment comment = new Comment(content, post, commemt);
        CommentAction commentAction = new CommentAction(getActivity());
        commentAction.upload(comment, post, new CommentAction.UpLoadCommentListener() {
            @Override
            public void onCommentSucc() {
                if (null != pd) {
                    pd.dismiss();
                }
                if (null == etComment) {
                    return;
                }
                hideEmoji();
                InputMethodManagerUtils.hide(getActivity(), etComment);
                etComment.setText("");
                queryComment();
                refreshPost();

            }

            @Override
            public void onCommentFail() {
                if (null != pd) {
                    pd.dismiss();
                }
                MyToast.show(getActivity(), "回复失败");
            }
        });
    }

    @Override
    public void onKeyBoardShow() {
        hideEmoji();
        isKeyBoardShow = true;
    }

    @Override
    public void onKeyBoardHide() {
        isKeyBoardShow = false;
        if (isEmojiShow) {
            emojiView.post(new Runnable() {
                @Override
                public void run() {
                    emojiView.setVisibility(View.VISIBLE);
                }
            });

        }
    }

    public void hideEmoji() {
        if (null != emojiView && emojiView.getVisibility() == View.VISIBLE) {
            emojiView.post(new Runnable() {
                @Override
                public void run() {
                    emojiView.setVisibility(View.GONE);
                    isEmojiShow = false;
                }
            });
        }
    }

    public boolean isEmojiShow() {
        if (null == emojiView) {
            return false;
        }
        return emojiView.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onCorpusSelected(SpannableString emoji) {
        int index = etComment.getSelectionStart();
        Editable edit = etComment.getEditableText();
        if (index < 0 || index >= edit.length()) {
            edit.append(emoji);
        } else {
            edit.insert(index, emoji);
        }
    }

    @Override
    public void onCorpusDeleted() {
        MyUtils.deleteSpannableString(etComment, "[em]", "[/em]");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        etComment.removeTextChangedListener(this);
        MyUtils.judgeDeleteWord(etComment, s, maxLength);
        long length = MyUtils.calculateLength(etComment.getText().toString());
        if (length >= maxLength) {
            MyToast.show(getActivity(), getActivity().getString(R.string.max_word_count, maxLength));
        }
        etComment.addTextChangedListener(this);
    }

    @Override
    public void onCommentClick(Comment comment) {
        this.commemt = comment;
        etComment.setHint(getActivity().getString(R.string.response_floor, comment.getFloor()));
        etComment.requestFocus();
        llComment.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            this.commemt = null;
            etComment.setHint("匿名评论");
            etComment.setText("");
            hideEmoji();
        }
    }


    private void updateHeadView(final PostShowView headView) {
        headView.update(post, new PostShowView.OnLikedListener() {
            @Override
            public void isLiked() {
                post.setLikeState(Post.LIKEDSTATE);
            }

            @Override
            public void isNotLiked() {
                post.setLikeState(Post.NOLIKESTATE);
            }
        });
        headView.setOnPostClickListener(new PostShowView.OnPostClickListener() {
            @Override
            public void onContentClick(Post post) {

            }

            @Override
            public void onTopicClick(Post post, String topic) {

            }

            @Override
            public void onLikeClick() {
                LikeAction likeAction = new LikeAction(getActivity());
                likeAction.excute(post);
                likeAction.setOnLikeListener(new LikeAction.OnLikeListener() {
                    @Override
                    public void onDoLikeSuccess() {
                        headView.setLikeSuccess();
                        post.setLikeState(Post.LIKEDSTATE);
                    }

                    @Override
                    public void onCancelLikeSuccess() {
                        headView.setLikeFail();
                        post.setLikeState(Post.NOLIKESTATE);
                    }

                    @Override
                    public void onLikeFail() {
                        headView.setLikeFail();
                        post.setLikeState(Post.UNKNOWNLIKESTATE);
                    }
                });
            }

            @Override
            public void onCommentClick() {

            }
        });
    }

    private class PostDetailAdapter extends SubHFLoadMoreRVAdapter<Comment> {

        public PostDetailAdapter(List<Comment> datas, Context context) {
            super(datas, context);
        }

        @Override
        protected View getHeadView() {
            postShowView = new PostShowView(context);
            postShowView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    postShowView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) postShowView.getLayoutParams();
                    int height = MyUtils.getScreenMetrics(context).heightPixels*2/5;
                    lp.height = height;
                    postShowView.setMinimumHeight(height);
//                    postShowView.setLayoutParams(lp);
                }
            });
            return postShowView;
        }

        @Override
        protected View getView() {
            return new PostDeatilCommentView(context);
        }

        @Override
        protected RecyclerView.ViewHolder getViewHolder(View view, int viewType) {
            return new HFViewHolder<Comment>(view, viewType) {
                @Override
                protected void setContent(View itemView, Comment item, int viewType) {
                    if (itemView instanceof PostDeatilCommentView) {
                        ((PostDeatilCommentView) itemView).update(item);
                        ((PostDeatilCommentView) itemView).setOnCommentClickListener(PostDetailFrag.this);

                    }
                }

                @Override
                public void handlefooter() {

                }

                @Override
                protected void handleHead(final View headView) {
                    if (headView instanceof PostShowView) {
                        updateHeadView((PostShowView) headView);
                    }
                }
            };
        }
    }
}
