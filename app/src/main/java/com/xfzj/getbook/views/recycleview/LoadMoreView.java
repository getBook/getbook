package com.xfzj.getbook.views.recycleview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.xfzj.getbook.R;
import com.xfzj.getbook.utils.MyUtils;

/**
 * Created by zj on 2016/3/24.
 */
public class LoadMoreView extends LinearLayout implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListen, LoadMoreLayout.OnScrollCallBack {
    private Context context;
    private LoadMoreLayout loadMoreLayout;
    private RecyclerView recyclerView;
    private RefreshListener refreshListener;
    private boolean isRefresh;
    private LoadMoreListen loadMoreListen;
    private LoadMoreLayout.OnScrollCallBack onScrollCallBack;
    private boolean isInitRefresh = false;
    public LoadMoreView(Context context) {
        this(context, null);
    }

    public LoadMoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public LoadMoreView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void setOnScrollCallBack(LoadMoreLayout.OnScrollCallBack onScrollCallBack) {
        this.onScrollCallBack = onScrollCallBack;
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.loadmorelayout, null);
        loadMoreLayout = (LoadMoreLayout) view.findViewById(R.id.loadMoreLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        loadMoreLayout.setColorSchemeResources(R.color.primary);
//        loadMoreLayout.setColorSchemeColors(R.color.primary);
        loadMoreLayout.setOnRefreshListener(this);
        loadMoreLayout.setLoadMoreListen(this);
        loadMoreLayout.setOnScrollCallBack(this);
        addView(view);
    }

    public void setAdapter(BaseRecycleViewAdapter adapter) {
        recyclerView.setAdapter(adapter);
        loadMoreLayout.setAdapter(adapter);
    }

    public void setOnrefreshListener(RefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public void setOnLoadMoreListen(LoadMoreListen loadMoreListen) {
        this.loadMoreListen = loadMoreListen;
    }

    @Override
    public void onRefresh() {
        if (isRefresh) {
            return;
        }
        isRefresh = true;
        if (null != refreshListener) {
            refreshListener.onRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (null != loadMoreListen) {
            loadMoreListen.onLoadMore();
        }
    }

    public void setRefreshing() {
        if (isInitRefresh) {
            loadMoreLayout.setRefreshing(true);
            return;
        }
        isInitRefresh=true;
        loadMoreLayout.setProgressViewOffset(false, 0, (int) MyUtils.dp2px(context, 26f));
        loadMoreLayout.setRefreshing(true);
    }


    public RecyclerView getRecycleView() {
        return recyclerView;
    }

    @Override
    public void onScroll(boolean b) {
        if (null != onScrollCallBack) {
            onScrollCallBack.onScroll(b);
        }
    }

    public interface RefreshListener {
        void onRefresh();
    }


    public void setRefreshFinish() {
        loadMoreLayout.setRefreshing(false);
        isRefresh = false;
        recyclerView.scrollToPosition(0);
    }

    public void setLoadMoreFinish() {
        loadMoreLayout.setLoading(false);

    }

    public void setCanLoadMore(boolean canLoadMore) {
        loadMoreLayout.setCanLoadMore(canLoadMore);

    }

    public void clearRefresh() {
        if (loadMoreLayout != null) {
            loadMoreLayout.setRefreshing(false);
            loadMoreLayout.destroyDrawingCache();
            loadMoreLayout.clearAnimation();
        }
    }
}
