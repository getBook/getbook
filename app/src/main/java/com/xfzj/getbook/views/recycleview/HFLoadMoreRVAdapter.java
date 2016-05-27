package com.xfzj.getbook.views.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by zj on 2016/5/8.
 */
public abstract class HFLoadMoreRVAdapter<T> extends LoadMoreRVAdapter<T> {
    public static final int HEAD = 0;
    public static final int NORMAL = 1;
    public static final int FOOTER = 2;

    public HFLoadMoreRVAdapter(List<T> datas, Context context) {
        super(datas, context);
    }

    @Override
    public int getItemViewType(int position) {
        if (null == datas) {
            return NORMAL;
        }
        int last = datas.size()+1;
        if (position == last) {
            return FOOTER;
        } else if (position == 0) {
            return HEAD;
        } else {
            return NORMAL;
        }
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
        } else if (viewType == HEAD) {
            return getViewHolder(getHeadView(), viewType);
        }
        return null;
    }

    protected abstract View getHeadView();

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        this.tBaseFooterViewHolder = (BaseFooterViewHolder<T>) holder;
        if (position == datas.size()+1) {
            ((BaseFooterViewHolder) holder).handlefooter();

        } else if (position == 0) {
            ((HFViewHolder) holder).handleHead();
        }else{
            ((BaseFooterViewHolder) holder).setItem(datas.get(position-1));
        }
    }

    protected abstract class HFViewHolder<T> extends BaseFooterViewHolder<T> {
        private  View headView;

        public HFViewHolder(View itemView, int viewType) {
            super(itemView, viewType);
            if (viewType == HEAD) {
                headView = itemView;
            }
        }

        public  void handleHead(){
            handleHead(headView);
        }

        protected abstract void handleHead(View headView);
    }
    
}
