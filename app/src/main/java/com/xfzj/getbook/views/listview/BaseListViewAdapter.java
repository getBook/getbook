package com.xfzj.getbook.views.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zj on 2016/3/22.
 */
public abstract class BaseListViewAdapter<T> extends BaseAdapter {
    private List<T> lists;
    protected Context context;

    public BaseListViewAdapter(List<T> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    public BaseListViewAdapter(Context context) {
        this.context = context;
        lists = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public T getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addAll(List<T> l) {
        lists.addAll(l);
        notifyDataSetChanged();

    }

    public void delete(int position) {
        lists.remove(position);
        notifyDataSetChanged();
    }
    public void clear() {
        lists.clear();
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseViewHolder baseViewHolder;
        if (null == convertView) {
            baseViewHolder = getViewHolder();
            convertView = baseViewHolder.getRootView();
            convertView.setTag(baseViewHolder);

        } else {
            baseViewHolder = (BaseViewHolder) convertView.getTag();
        }
        baseViewHolder.setData(lists.get(position));
        return convertView;
    }

    protected abstract BaseViewHolder getViewHolder();


    protected abstract class BaseViewHolder {
        private View rootView;

        public BaseViewHolder() {
            rootView = initView();
        }

        protected abstract View initView();

        public abstract void setData(T t);

        
        public View getRootView() {
            return rootView;
        }
    }

}
