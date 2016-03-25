package com.xfzj.getbook.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.xfzj.getbook.R;

import java.util.List;

/**
 * Created by zj on 2016/3/25.
 */
public abstract class FooterLoadMoreRVAdapter<T> extends LoadMoreRVAdapter<T> {

    public FooterLoadMoreRVAdapter(List<T> datas, Context context) {
        super(datas, context);
    }

    @Override
    protected View getFooterView() {
        View view = LayoutInflater.from(context).inflate(R.layout.footer, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        view.setVisibility(View.GONE);
        return view;
    }

    @Override
    protected View getView() {
        return getNormalView();
    }

    protected abstract View getNormalView();

    @Override
    protected RecyclerView.ViewHolder getViewHolder(View view, int viewType) {
        return getNormalViewHolder(view, viewType);
    }

    protected abstract RecyclerView.ViewHolder getNormalViewHolder(View view, int viewType);

    protected abstract class NormalViewHolder<T> extends BaseFooterViewHolder<T> {
        public NormalViewHolder(View itemView, int viewType) {
            super(itemView, viewType);
        }
        @Override
        protected void setContent(View itemView, T item, int viewType) {
            setNormalContent(itemView, item, viewType);
        }

        protected abstract void setNormalContent(View itemView, T item, int viewType);

        @Override
        public void handlefooter() {
        }
    }
}
