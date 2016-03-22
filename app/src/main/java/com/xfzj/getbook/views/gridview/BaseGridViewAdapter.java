package com.xfzj.getbook.views.gridview;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.xfzj.getbook.R;
import com.xfzj.getbook.loader.ImageLoader;

import java.util.List;

/**
 * Created by zj on 2016/2/8.
 */
public abstract class BaseGridViewAdapter<T> extends BaseAdapter implements AbsListView.OnScrollListener {
    protected Context mContext;
    protected List<T> paths;

    private static final int LAST = 0;
    private static final int NORMAL = 1;
    protected boolean IsGridViewIdle;
    protected ImageLoader imageLoader;

    // Constructor
    public BaseGridViewAdapter(Context c, List<T> paths) {
        mContext = c;
        this.paths = paths;
        imageLoader = ImageLoader.build(mContext, BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.image_default));
    }
    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public T getItem(int position) {
        if (position < 0 || position > paths.size())
            return null;
        return paths.get(position);

    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseViewHolder baseViewHolder = null;
        if (convertView == null) {
            baseViewHolder = getViewHolder();
            convertView = baseViewHolder.getRootView();
            convertView.setTag(baseViewHolder);
        } else {
            baseViewHolder = (BaseViewHolder) convertView.getTag();
        }
        baseViewHolder.setData(paths.get(position));
        
        return convertView;
    }

    protected abstract BaseViewHolder getViewHolder();


    public abstract class BaseViewHolder {
        public View getRootView() {
            return rootView;
        }

        private View rootView;
        public BaseViewHolder() {
            rootView = initView();
        }

        protected abstract View initView();

        public abstract void setData(T s);

        
    }

    public void delete(int position) {
        if (position < 0 || position >= paths.size() - 1) {
            return;
        }
        paths.remove(position);
        notifyDataSetChanged();
    }


    public void delete(T path) {
        if (null == paths) {
            return;
        }
        if (null == this.paths || this.paths.size() ==  0) {
            return;
        }
        this.paths.remove(path);
    }
    public void deleteAll() {
        if (null == paths || paths.size() ==  0) {
            return;
        }
        paths.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<T> paths) {
        this.paths.addAll(paths);
        notifyDataSetChanged();
    }

    public void add(T  paths) {
        this.paths.add(paths);
        notifyDataSetChanged();
    }

    public List<T> getPaths() {
        return paths;
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            IsGridViewIdle = true;
            notifyDataSetChanged();
        } else {
            IsGridViewIdle = false;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
