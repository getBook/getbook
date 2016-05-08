package com.xfzj.getbook.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xfzj.getbook.BaseApplication;
import com.xfzj.getbook.Constants;
import com.xfzj.getbook.R;
import com.xfzj.getbook.action.LikeAction;
import com.xfzj.getbook.action.QueryAction;
import com.xfzj.getbook.common.Post;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.utils.FaceConversionUtil;
import com.xfzj.getbook.views.recycleview.FooterLoadMoreRVAdapter;
import com.xfzj.getbook.views.recycleview.LoadMoreLayout;
import com.xfzj.getbook.views.recycleview.LoadMoreListen;
import com.xfzj.getbook.views.recycleview.LoadMoreView;
import com.xfzj.getbook.views.view.PostShowView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zj on 2016/4/16.
 */
public class PostFrag extends BaseFragment implements View.OnClickListener, LoadMoreLayout.OnScrollCallBack, LoadMoreView.RefreshListener, LoadMoreListen {

    public static final String PARAM = "PostFrag";
    private String param;
    @Bind(R.id.loadMoreView)
    LoadMoreView loadMoreView;
    @Bind(R.id.llError)
    LinearLayout llError;
    @Bind(R.id.btn)
    Button btn;
    private List<Post> posts = new ArrayList<>();
    private PostAdapter postAdapter;
    private int skip = 0;
    private QueryAction queryAction;
    private User user;

    public static PostFrag newInstance(String param) {
        PostFrag postFrag = new PostFrag();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM, param);
        postFrag.setArguments(bundle);
        return postFrag;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            param = getArguments().getString(PARAM);
        }
        user = ((BaseApplication) getActivity().getApplicationContext()).getUser();
        FaceConversionUtil.getInstace().getFileText(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        ButterKnife.bind(this, view);
        btn.setOnClickListener(this);
        postAdapter = new PostAdapter(posts, getActivity());
        loadMoreView.setAdapter(postAdapter);
        loadMoreView.setOnrefreshListener(this);
        loadMoreView.setOnLoadMoreListen(this);
        loadMoreView.setOnScrollCallBack(this);
        queryAction = new QueryAction(getActivity());
        onRefresh();
        return view;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onScroll(boolean b) {

    }

    @Override
    public void onRefresh() {
        skip = 0;
        loadMoreView.setRefreshing();
        queryAction.queryPost(user.getObjectId(), Constants.POST_LIMIT, skip, new QueryAction.OnQueryListener<List<Post>>() {


            @Override
            public void onSuccess(List<Post> posts) {
                loadMoreView.setRefreshFinish();
                postAdapter.clear();
                postAdapter.addAll(posts);

                loadMoreView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFail() {
                loadMoreView.setRefreshFinish();
                llError.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onLoadMore() {
        queryAction.queryPost(user.getObjectId(),Constants.POST_LIMIT, ++skip, new QueryAction.OnQueryListener<List<Post>>() {


            @Override
            public void onSuccess(List<Post> posts) {
                loadMoreView.setLoadMoreFinish();
                if (null != posts && posts.size() > 0) {
                    postAdapter.addAll(posts);
                }

            }

            @Override
            public void onFail() {
                loadMoreView.setLoadMoreFinish();
            }
        });

    }

    private class PostAdapter extends FooterLoadMoreRVAdapter<Post> {


        public PostAdapter(List<Post> datas, Context context) {
            super(datas, context);
        }

        @Override
        protected View getNormalView() {
            return new PostShowView(context);
        }

        @Override
        protected RecyclerView.ViewHolder getNormalViewHolder(final View view, int viewType) {
            return new NormalViewHolder<Post>(view, viewType) {

                @Override
                protected void setNormalContent(final View itemView, final Post item, int viewType) {

                    if (itemView instanceof PostShowView) {
                        ((PostShowView) itemView).update(item, new PostShowView.OnLikedListener() {
                            @Override
                            public void isLiked() {
                                item.setLikeState(Post.LIKEDSTATE);
                                
                            }

                            @Override
                            public void isNotLiked() {
                                item.setLikeState(Post.NOLIKESTATE);
                            }
                        });
                        ((PostShowView) itemView).setOnPostClickListener(new PostShowView.OnPostClickListener() {
                            @Override
                            public void onContentClick(Post post) {
                            }

                            @Override
                            public void onTopicClick(Post post, String topic) {
                            }

                            @Override
                            public void onLikeClick() {
                                LikeAction likeAction = new LikeAction(getActivity());
                                likeAction.excute(item);
                                likeAction.setOnLikeListener(new LikeAction.OnLikeListener() {
                                    @Override
                                    public void onDoLikeSuccess() {
                                        ((PostShowView) itemView).setLikeSuccess();
                                        item.setLikeState(Post.LIKEDSTATE);
                                    }

                                    @Override
                                    public void onCancelLikeSuccess() {
                                        ((PostShowView) itemView).setLikeFail();
                                        item.setLikeState(Post.NOLIKESTATE);
                                    }

                                    @Override
                                    public void onLikeFail() {
                                        ((PostShowView) itemView).setLikeFail();
                                        item.setLikeState(Post.UNKNOWNLIKESTATE);
                                    }
                                });
                            }

                            @Override
                            public void onCommentClick() {
                            }
                        });
                    }
                }
            };
        }
    }
}
