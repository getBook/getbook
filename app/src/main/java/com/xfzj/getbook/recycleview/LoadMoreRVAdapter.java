package com.xfzj.getbook.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by zhoujia on 16/1/8.
 */
public abstract class LoadMoreRVAdapter<T> extends BaseRecycleViewAdapter<T> {
    public static final int HEADER = 0;
    public static final int NORMAL = 1;
    public static final int FOOTER = 2;
    private int resourceHeader, resourceFooter;
    private View headerView, footerView;
    public LoadMoreRVAdapter(List<T> datas, Context context, int resourceNormal, int resourceHeader, int resourceFooter) {
        super(datas, context, resourceNormal);
            this.resourceHeader = resourceHeader;
            this.resourceFooter = resourceFooter;
    }

    @Override
    public int getItemViewType(int position) {
        int last = datas.size();
        if (position == 0&&resourceHeader!=0) {
            last++;
            return HEADER;
        } else if (position == last&&resourceFooter!=0) {
            return FOOTER;
        } else {
            return NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        int count=0;
        if (resourceHeader != 0) {
            count++;
        }
        if (resourceFooter != 0) {
            count++;
        }
        return super.getItemCount() + count;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER&&resourceHeader!=0) {
            headerView = layoutInflater.inflate(resourceHeader, parent, false);
            return getViewHolder(headerView, viewType);
        } else if (viewType == FOOTER&&resourceFooter!=0) {
            footerView = layoutInflater.inflate(resourceFooter, parent, false);
            return getViewHolder(footerView, viewType);
        } else if(viewType == NORMAL&&resource!=0){
            View view = layoutInflater.inflate(resource, parent, false);
            return getViewHolder(view, viewType);
        }else{
            return null;
        }
    }

    protected abstract RecyclerView.ViewHolder getViewHolder(View view, int viewType);
    
    public boolean hasHeader() {
        return resourceHeader != 0;
    }
    public boolean hasFooter() {
        return resourceFooter != 0;
    }
}
