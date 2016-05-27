package com.xfzj.getbook.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.action.QueryAction;
import com.xfzj.getbook.common.Post;
import com.xfzj.getbook.common.UnreadPost;
import com.xfzj.getbook.utils.MyLog;
import com.xfzj.getbook.utils.SharedPreferencesUtils;
import com.xfzj.getbook.views.recycleview.FooterLoadMoreRVAdapter;
import com.xfzj.getbook.views.view.UnreadPostView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by zj on 2016/3/25.
 */
public class NotificationFrag extends BaseFragment implements QueryAction.OnQueryListener<List<UnreadPost>> {


    public static final String PARAM = "NotificationFrag.class";

    private String param;


    private RecyclerView loadMoreView;
    private UnreadPostAdapter unreadPostAdapter;
    private List<UnreadPost> list = new ArrayList<>();
    private List<UnreadPost> unreadPosts;
    private RelativeLayout relativeLayout;
    private ProgressDialog progressDialog;

    public NotificationFrag() {

    }


    public static NotificationFrag newInstance(String param) {
        NotificationFrag notificationFrag = new NotificationFrag();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM, param);
        notificationFrag.setArguments(bundle);
        return notificationFrag;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            param = getArguments().getString(PARAM);
        }
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_payinfo, container, false);
        loadMoreView = (RecyclerView) view.findViewById(R.id.loadMoreView);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.rl);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        loadMoreView.setLayoutManager(layoutManager);
        unreadPostAdapter = new UnreadPostAdapter(list, getActivity());
        loadMoreView.setAdapter(unreadPostAdapter);
        queryNotification();
        return view;
    }

    public void setUnreadPosts(Set<UnreadPost> unreadPosts) {
        this.unreadPosts = new ArrayList<>();
        this.unreadPosts.addAll(unreadPosts);
    }


    private void queryNotification() {
        if (null == unreadPosts || unreadPosts.size() == 0) {
            onError();
            return;
        }
        if (null == progressDialog) {
            progressDialog = ProgressDialog.show(getActivity(), null, getActivity().getString(R.string.loading));
        }
        QueryAction queryAction = new QueryAction(getActivity(), this);
        queryAction.queryUnreadPostContent(unreadPosts);
    }

    private void onError() {
        if (null == loadMoreView) {
            return;
        }
        loadMoreView.setVisibility(View.GONE);

        TextView textView = new TextView(getActivity());
        textView.setText(R.string.no_notification);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(lp);
        if (null != relativeLayout) {
            relativeLayout.addView(textView);
        }
    }


    @Override
    public void onSuccess(List<UnreadPost> unreadPosts) {
        closeProgressDialog();
        if (null == unreadPosts || unreadPosts.size() == 0) {
            onError();
            return;
        }
        List<UnreadPost> unreadPosts1 = new ArrayList<>();
        for (UnreadPost unreadPost : unreadPosts) {
            if (null != unreadPost) {
                unreadPosts1.add(unreadPost);
            }
        }
        unreadPostAdapter.clear();
        unreadPostAdapter.addAll(unreadPosts1);
    }

    private void closeProgressDialog() {
        if (null != progressDialog && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onFail() {
        closeProgressDialog();
        onError();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_notification).setVisible(false);
    }

    private class UnreadPostAdapter extends FooterLoadMoreRVAdapter<UnreadPost> {

        public UnreadPostAdapter(List<UnreadPost> datas, Context context) {
            super(datas, context);
        }

        @Override
        protected View getNormalView() {
            return new UnreadPostView(getActivity());


        }

        @Override
        protected RecyclerView.ViewHolder getNormalViewHolder(View view, int viewType) {
            return new NormalViewHolder<UnreadPost>(view, viewType) {

                @Override
                protected void setNormalContent(View itemView, UnreadPost item, int viewType) {
                    if (itemView instanceof UnreadPostView) {
                        if (null != item) {
                            ((UnreadPostView) itemView).update(item);
                            ((UnreadPostView) itemView).setOnUnreadPostClick(new UnreadPostView.OnUnreadPostClick() {
                                @Override
                                public void onUnreadPostClick(UnreadPost unreadPost, String id) {
                                    MyLog.print("das0", unreadPost.getPost().getCommentCount() + "");
                                    SharedPreferencesUtils.saveFocusPost(context, id, unreadPost.getPost().getCommentCount());
                                    if (null != onUnreadPostClick) {
                                        onUnreadPostClick.onUnreadPostClick(unreadPost.getPost());
                                    }

                                }
                            });
                        }
                    }
                }
            };
        }
    }

    private OnUnreadPostClick onUnreadPostClick;

    public void setOnUnreadPostClick(OnUnreadPostClick onUnreadPostClick) {
        this.onUnreadPostClick = onUnreadPostClick;
    }

    public interface OnUnreadPostClick {
        void onUnreadPostClick(Post post);
    }

}
