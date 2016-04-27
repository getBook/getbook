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

import com.xfzj.getbook.Constants;
import com.xfzj.getbook.R;
import com.xfzj.getbook.action.LikeAction;
import com.xfzj.getbook.action.QueryAction;
import com.xfzj.getbook.common.Post;
import com.xfzj.getbook.recycleview.FooterLoadMoreRVAdapter;
import com.xfzj.getbook.recycleview.LoadMoreLayout;
import com.xfzj.getbook.recycleview.LoadMoreListen;
import com.xfzj.getbook.recycleview.LoadMoreView;
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
        queryAction.queryPost(Constants.POST_LIMIT, skip, new QueryAction.OnQueryListener<List<Post>>() {


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
        queryAction.queryPost(Constants.POST_LIMIT, ++skip, new QueryAction.OnQueryListener<List<Post>>() {


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
            PostShowView postShowView = new PostShowView(context);
            postShowView.setTag(postShowView);
            return postShowView;
        }

        @Override
        protected RecyclerView.ViewHolder getNormalViewHolder(final View view, int viewType) {
            return new NormalViewHolder<Post>(view, viewType) {

                @Override
                protected void setNormalContent(final View itemView, Post item, int viewType) {
                    Post post = (Post) itemView.getTag();
                    if (itemView instanceof PostShowView && (null == post || !post.equals(item))) {
                        ((PostShowView) itemView).update(item);
                        ((PostShowView) itemView).setOnPostClickListener(new PostShowView.OnPostClickListener() {
                            @Override
                            public void onContentClick(Post post) {
                            }

                            @Override
                            public void onTopicClick(Post post, String topic) {
                            }

                            @Override
                            public void onLikeClick(Post post) {
                                LikeAction likeAction = new LikeAction(getActivity());
                                likeAction.excute(post);
                                likeAction.setOnLikeListener(new LikeAction.OnLikeListener() {
                                    @Override
                                    public void onDoLikeSuccess() {
                                        ((PostShowView) itemView).setLikeSuccess();
                                    }

                                    @Override
                                    public void onCancelLikeSuccess() {
                                        ((PostShowView) itemView).setLikeFail();
                                    }

                                    @Override
                                    public void onLikeFail() {
                                        ((PostShowView) itemView).setLikeFail();
                                    }
                                });
                            }

                            @Override
                            public void onCommentClick(Post post) {
                            }
                        });
                    }
                }
            };
        }
    }
}
