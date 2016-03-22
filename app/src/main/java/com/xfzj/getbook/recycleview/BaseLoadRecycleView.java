package com.xfzj.getbook.recycleview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.xfzj.getbook.R;
import com.xfzj.getbook.utils.MyUtils;

/**
 * Created by zj on 2016/1/26.
 */
public class BaseLoadRecycleView extends LinearLayout {
    private SwipeRefreshLayout refresh;
    private BaseLoadMoreRecycleView rc;
    private RefreshListener refreshListener;
    private boolean isRefresh;
private Context context;
    public BaseLoadRecycleView(Context context) {
        this(context, null);
    }

    public BaseLoadRecycleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseLoadRecycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseLoadRecycleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.baseloadrecycleview, null);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        rc = (BaseLoadMoreRecycleView) view.findViewById(R.id.rc);
        refresh.setColorSchemeColors(R.color.primary);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        });

        addView(view);
    }

    public void setAdapter(BaseRecycleViewAdapter adapter) {
        rc.setLoadMoreAdapter(adapter);
    }

    public void setOnLoadMoreListen(LoadMoreListen loadMoreListen) {
        rc.setLoadMoreListen(loadMoreListen);
    }

    public void setOnrefreshListener(RefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public interface RefreshListener {
        void onRefresh();
    }
    public void setRefreshFinish(){
        refresh.setRefreshing(false);
        isRefresh=false;
        rc.scrollToPosition(0);
    }
    public void setRefreshing() {
        refresh.setProgressViewOffset(false, 0, (int)MyUtils.dp2px(context,24.0f));
        refresh.setRefreshing(true);
    }
    public int getChileCount() {
        return rc.getChildCount();
    }

    public View getChileViewAt(int position) {
        
        return rc.getChildAt(position);
    }
    
    public void setRefreshAndLoadMoreUnable() {
        refresh.setRefreshing(false);
        rc.setCanLoadMore(false);
    }
    public void setLoadMoreFinish(){
        rc.LoadFinish();
    }
    public void setIsLoadingMore(boolean isLoadingMore) {
        rc.setIsLoadingMore(isLoadingMore);
    }
}
