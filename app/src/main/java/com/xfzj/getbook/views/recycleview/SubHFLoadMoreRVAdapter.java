package com.xfzj.getbook.views.recycleview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.xfzj.getbook.R;

import java.util.List;

/**
 * Created by zj on 2016/5/8.
 */
public abstract class SubHFLoadMoreRVAdapter<T> extends HFLoadMoreRVAdapter<T> {
    public SubHFLoadMoreRVAdapter(List<T> datas, Context context) {
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
}
