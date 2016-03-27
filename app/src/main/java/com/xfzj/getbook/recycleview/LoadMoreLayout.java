package com.xfzj.getbook.recycleview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by zj on 2016/3/24.
 */
public class LoadMoreLayout extends SwipeRefreshLayout {

    private int mTouchSlop;

    private RecyclerView recyclerView;

    private LoadMoreRVAdapter loadMoreRVAdapter;

    private LoadMoreListen loadMoreListen;

    private View footerView;

    private int mYDown;

    private int mLastY;

    private boolean isLoading = false;
    private Context context;
    private OnScrollCallBack onScrollCallBack;

    public void setLoadMoreListen(LoadMoreListen loadMoreListen) {
        this.loadMoreListen = loadMoreListen;
    }

    public LoadMoreLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public LoadMoreLayout(Context context) {
        this(context, null);
    }

    public void setOnScrollCallBack(OnScrollCallBack onScrollCallBack) {
        this.onScrollCallBack = onScrollCallBack;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (recyclerView == null) {
            getRecycleView();
        }
    }

    private void getRecycleView() {
        int childs = getChildCount();
        if (childs > 0) {
            View childView = getChildAt(0);
            if (childView instanceof RecyclerView) {
                recyclerView = (RecyclerView) childView;

                recyclerView.addOnScrollListener(new OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        if (null != onScrollCallBack) {
                            onScrollCallBack.onScroll(isPullUp());
                        }
                        if (canLoad()) {
                            loadData();
                        }
                    }
                });
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mYDown = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                mLastY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                if (canLoad()) {
                    loadData();
                }
                break;

            default:
                break;
        }


        return super.dispatchTouchEvent(ev);
    }

    private boolean canLoad() {
        return isBottom() && !isLoading && isPullUp() && loadMoreRVAdapter.getItemCount() > 1;
    }

    private boolean isPullUp() {
        return (mYDown - mLastY) >= mTouchSlop;

    }

    private boolean isBottom() {
        if (recyclerView != null && recyclerView.getAdapter() != null) {
            int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
            return lastPosition == (recyclerView.getAdapter().getItemCount() - 1);
        }
        return false;
    }

    private void loadData() {
        if (null != loadMoreListen) {
            setLoading(true);
            loadMoreListen.onLoadMore();

        }
    }

    public void setLoading(boolean b) {
        isLoading = b;
        LoadMoreRVAdapter.BaseFooterViewHolder baseFooterViewHolder = loadMoreRVAdapter.gettBaseFooterViewHolder();
        View view = baseFooterViewHolder.getFooterView();

        if (isLoading) {
            if (null != view) {
                view.setVisibility(VISIBLE);
            }
        } else {
            if (null != view) {
                view.setVisibility(GONE);
            }
            mYDown = 0;
            mLastY = 0;
        }

    }

    public void setAdapter(BaseRecycleViewAdapter adapter) {
        this.loadMoreRVAdapter = (LoadMoreRVAdapter) adapter;
    }

    public interface OnScrollCallBack {
        void onScroll(boolean b);
    }

}
