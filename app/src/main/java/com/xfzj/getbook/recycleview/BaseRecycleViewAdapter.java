package com.xfzj.getbook.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
    private Context context;
    protected LayoutInflater layoutInflater;
    protected int resource;
    private OnRecycleViewItemClickListener onRecycleViewItemClickListener;

    public BaseRecycleViewAdapter(List<T> datas, Context context, int resource) {
        this.datas = datas;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.resource = resource;
    }

    public void setOnRecycleViewItemClickListener(OnRecycleViewItemClickListener onRecycleViewItemClickListener) {
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

    public void remove(int position) {
        if (position == -1) {
            return;
        }
        datas.remove(position);
        if (this instanceof LoadMoreRVAdapter) {
            if (((LoadMoreRVAdapter) this).hasHeader()) {
                notifyItemRemoved(position + 1);
            }else{
                notifyItemRemoved(position);
            }
        } else {
            notifyItemRemoved(position);
        }
    }

    public void addAll(List<T> lists) {
        datas.addAll(lists);
        notifyItemInserted(getItemCount());
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
        View view = layoutInflater.inflate(resource, parent, false);

        return getViewHolder(view, 1);
    }

    protected abstract ViewHolder getViewHolder(View view, int viewType);

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (this instanceof LoadMoreRVAdapter) {
            int headFlag = 0, footFlag = 0;
            if (((LoadMoreRVAdapter) this).hasHeader()) {
                headFlag++;
            }
            if (((LoadMoreRVAdapter) this).hasFooter()) {
                footFlag++;
            }
            //有头
            if (headFlag > 0 && position >= headFlag && position < datas.size()+headFlag) {
                ((BaseViewHolder) holder).setItem(datas.get(position - 1));

            }
            //有尾
            else if (footFlag > 0 && headFlag == 0 && position >= headFlag && position < datas.size()) {
                ((BaseViewHolder) holder).setItem(datas.get(position));

            } else {
                ((BaseViewHolder) holder).setItem(datas.get(position));
            }
        } else {
            ((BaseViewHolder) holder).setItem(datas.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return null != datas && datas.size() > 0 ? datas.size() : 0;
    }

    public interface OnRecycleViewItemClickListener {
        void setOnRecycleViewItemClickListener(View view, Object tag);
    }

    protected abstract class BaseViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
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
