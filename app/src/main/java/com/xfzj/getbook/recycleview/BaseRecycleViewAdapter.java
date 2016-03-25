package com.xfzj.getbook.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import static android.support.v7.widget.RecyclerView.OnClickListener;
import static android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * Created by zhoujia on 16/1/6.
 */
public abstract class BaseRecycleViewAdapter<T> extends RecyclerView.Adapter {
    protected List<T> datas;
    protected Context context;
    private OnRecycleViewItemClickListener onRecycleViewItemClickListener;

    public BaseRecycleViewAdapter(List<T> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    public void setOnRecycleViewItemClickListener(OnRecycleViewItemClickListener<T> onRecycleViewItemClickListener) {
        this.onRecycleViewItemClickListener = onRecycleViewItemClickListener;
    }

    public void add(T item) {
        add(item, datas.size());
    }

    public void add(T item, int position) {
        datas.add(position, item);
        notifyItemInserted(getItemCount());
    }

    public void addFirst(T item) {
        add(item, 0);

    }

    public void addLast(T item) {
        add(item);

    }

    public void remove(T item) {
        remove(datas.indexOf(item));

    }

    public List<T> getAll() {
        return datas;
    }

    public void remove(int position) {
        if (position == -1) {
            return;
        }
        datas.remove(position);
        notifyItemRemoved(position);
        
    }

    public void addAll(List<T> lists) {
        datas.addAll(lists);
        notifyDataSetChanged();
    }

    public void addFirst(List<T> lists) {
        datas.addAll(0, lists);
        notifyItemRangeInserted(0, lists.size());
    }

    public void addLast(List<T> lists) {
        addAll(lists);
    }

    public void clear() {
        if (getItemCount() > 0) {
            datas.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        return getViewHolder(getView(), 1);
    }

    protected abstract View getView();

    protected abstract ViewHolder getViewHolder(View view, int viewType);

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ((BaseViewHolder) holder).setItem(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return null != datas && datas.size() > 0 ? datas.size() : 0;
    }

    public interface OnRecycleViewItemClickListener<T> {
        void setOnRecycleViewItemClickListener(View view, T tag);
    }

    protected abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder implements OnClickListener {
        private View itemView;
        private T item;
        private int viewType;

        public BaseViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.itemView.setOnClickListener(this);
        }

        public BaseViewHolder(View itemView, int viewType) {
            this(itemView);
            this.viewType = viewType;
        }

        public void setItem(T item) {
            if (null == item) {
                return;
            }
            this.item = item;
            setContent(itemView, item, viewType);
        }

        protected abstract void setContent(View itemView, T item, int viewType);

        @Override
        public void onClick(View v) {
            if (null != onRecycleViewItemClickListener) {
                onRecycleViewItemClickListener.setOnRecycleViewItemClickListener(itemView, item);
            }
        }
    }

}
