package com.xfzj.getbook.views.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by zhoujia on 16/1/8.
 */
public abstract class LoadMoreRVAdapter<T>
        extends BaseRecycleViewAdapter<T> {
    public static final int NORMAL = 1;
    public static final int FOOTER = 2;
    private View headerView, footerView;

    protected BaseFooterViewHolder<T> tBaseFooterViewHolder;
    public LoadMoreRVAdapter(List<T> datas, Context context) {
        super(datas, context);

    }

    @Override
    public int getItemViewType(int position) {
        if (null == datas) {
            return NORMAL;
        }
        int last = datas.size();
        if (position == last) {
            return FOOTER;
        } else {
            return NORMAL;
        }
    }

    public BaseFooterViewHolder<T> gettBaseFooterViewHolder() {
        return tBaseFooterViewHolder;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER) {
            return getViewHolder(getFooterView(), viewType);
        } else if (viewType == NORMAL) {
            return getViewHolder(getView(), viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //有尾
        this.tBaseFooterViewHolder = (BaseFooterViewHolder<T>) holder;
        
        if (position == datas.size()) {
            ((BaseFooterViewHolder) holder).handlefooter();

        } else {
            ((BaseFooterViewHolder) holder).setItem(datas.get(position));
        }
    }

    protected abstract View getFooterView();

    protected abstract RecyclerView.ViewHolder getViewHolder(View view, int viewType);

    public View getViewHolder() {
        return null;
    }

    protected abstract class BaseFooterViewHolder<T> extends BaseViewHolder<T> {
        private View footerView;

        public BaseFooterViewHolder(View itemView) {
            super(itemView);
        }

        public BaseFooterViewHolder(View itemView, int viewType) {
            super(itemView, viewType);
            if (viewType == FOOTER) {
                footerView = itemView;
            }
            
        }

        public View getFooterView() {
            return footerView;
        }

        public abstract void handlefooter();

        
    }

}
