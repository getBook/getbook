package com.xfzj.getbook.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xfzj.getbook.R;
import com.xfzj.getbook.async.GetNewsListLoader;
import com.xfzj.getbook.common.News;
import com.xfzj.getbook.recycleview.BaseLoadRecycleView;
import com.xfzj.getbook.recycleview.BaseRecycleViewAdapter;
import com.xfzj.getbook.recycleview.LoadMoreListen;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.views.view.NewsShowView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zj on 2016/3/16.
 */
public class NewsShowFrag extends Fragment implements BaseLoadRecycleView.RefreshListener, LoadMoreListen, View.OnClickListener, LoaderManager.LoaderCallbacks<List<News>> {
    public static final String PARAM = "newsshowfrag";
    private static final String NEWSLIST = "newslists";
    private String param;
    private BaseLoadRecycleView rc;
    private LinearLayout llError;
    private Button btn;
    private List<News> lists = new ArrayList<>();
    private boolean isRefresh = true;
    private NewsShowAdapter newsShowAdapter;
private GetNewsListLoader getNewsListLoader;
    private OnNewsClick onNewsClick;
    public NewsShowFrag() {

    }


    public static NewsShowFrag newInstance(String param) {

        NewsShowFrag newsFrag = new NewsShowFrag();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM, param);
        newsFrag.setArguments(bundle);
        return newsFrag;
    }

    public void setOnNewsClick(NewsShowFrag.OnNewsClick onNewsClick) {
        this.onNewsClick = onNewsClick;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            param = getArguments().getString(PARAM);
        }
        if (null != savedInstanceState) {
           lists= (List<News>) savedInstanceState.getSerializable(NEWSLIST);
           
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsshow, null);
        rc = (BaseLoadRecycleView) view.findViewById(R.id.recycleView);
        llError = (LinearLayout) view.findViewById(R.id.llError);
        btn = (Button) view.findViewById(R.id.btn);
        newsShowAdapter = new NewsShowAdapter(lists, getActivity());
        rc.setAdapter(newsShowAdapter);
        rc.setOnrefreshListener(this);
        rc.setOnLoadMoreListen(this);
        btn.setOnClickListener(this);
        onRefresh();
        return view;
    }


    @Override
    public void onRefresh() {
        rc.setRefreshing();
        isRefresh = true;
        getNewsListLoader = (GetNewsListLoader) getLoaderManager().restartLoader(0,null,this);
    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        getNewsListLoader.forceLoad();

    }
    @Override
    public void onClick(View v) {
        if (R.id.btn == v.getId()) {
            onRefresh();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(NEWSLIST, (Serializable) newsShowAdapter.getAll());
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new GetNewsListLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        if (null == data || data.size() == 0) {
            if (isRefresh) {
                rc.setVisibility(View.GONE);
                llError.setVisibility(View.VISIBLE);
            } else {
                MyToast.show(getActivity(), getActivity().getString(R.string.end));
            }
        } else {
            rc.setVisibility(View.VISIBLE);
            llError.setVisibility(View.GONE);
            //上拉刷新需要清楚之前的数据
            if (isRefresh) {
                rc.setRefreshFinish();
                newsShowAdapter.clear();
            } else {
                rc.setLoadMoreFinish();
            }
            newsShowAdapter.addAll(data);

        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
    }

    private class NewsShowAdapter extends BaseRecycleViewAdapter<News> {

        public NewsShowAdapter(List<News> datas, Context context) {
            super(datas, context);
        }

        @Override
        protected View getView() {
            return new NewsShowView(getActivity());
        }

        @Override
        protected RecyclerView.ViewHolder getViewHolder(View view, int viewType) {


            return new BaseViewHolder<News>(view) {

                @Override
                protected void setContent(View itemView, News news, int viewType) {
                    if (itemView instanceof NewsShowView) {
                        NewsShowView newsShowView = (NewsShowView) itemView;
                        newsShowView.update(news);
                        newsShowView.setOnNewsShowClickListener(new NewsShowView.OnNewsShowClickListener() {
                            @Override
                            public void onClick(News news) {
                                if (null != onNewsClick) {
                                    onNewsClick.onClick(news);
                                }
                            }
                        });
                    }
                }
            }
                    ;
        }
    }

    public interface OnNewsClick {
        void onClick(News news);
    }
}
