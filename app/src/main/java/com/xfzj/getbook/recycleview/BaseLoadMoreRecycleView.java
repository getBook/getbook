package com.xfzj.getbook.recycleview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by zhoujia on 16/1/8.
 */
public class BaseLoadMoreRecycleView extends RecyclerView {
    private BaseRecycleViewAdapter adapter;

    private boolean isLoadingMore=false;//是否正处于加载中
    private boolean canLoadMore=true;
    private LoadMoreListen loadMoreListen;//上拉加载更多的接口
    private int lastPosition;//最后一个可见的位置
    private Context context;
    public BaseLoadMoreRecycleView(Context context) {
        super(context);
        init(context);
    }

    public BaseLoadMoreRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    public void setLoadMoreAdapter(BaseRecycleViewAdapter adapter) {
        this.adapter = adapter;
        setAdapter(adapter);
    }

    public BaseLoadMoreRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    public void setLoadMoreListen(LoadMoreListen loadMoreListen) {
        this.loadMoreListen = loadMoreListen;
    }

    public boolean getIsLoadingMore() {
        return isLoadingMore;
    }

    public void setIsLoadingMore(boolean isLoadingMore) {
        this.isLoadingMore = isLoadingMore;
    }

    /**
     * 初始化上拉加载更多
     */
    private void init(Context context) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        setLayoutManager(mLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        setHasFixedSize(true);
        super.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (null != loadMoreListen && !getIsLoadingMore() && dy > 0&&canLoadMore) {
                    
                    int lastPos = getLastPosition();
                    if (lastPos + 1 == adapter.getItemCount()) {
                        setIsLoadingMore(true);
                        lastPosition = lastPos;
                        loadMoreListen.onLoadMore();
                    }
                }
            }
        });
    }

    /**
     * 获取最后一个可见的item位置
     *
     * @return
     */
    private int getLastPosition() {
        return ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
    }

    /**
     * 加载完成之后的调用方法
     */
    public void LoadFinish() {
        setIsLoadingMore(false);
    }
}
