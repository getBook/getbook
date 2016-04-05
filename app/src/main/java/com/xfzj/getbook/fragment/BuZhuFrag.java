package com.xfzj.getbook.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xfzj.getbook.R;
import com.xfzj.getbook.activity.AppActivity;
import com.xfzj.getbook.async.GetSubsidyTrjnAsync;
import com.xfzj.getbook.async.UcardAsyncTask;
import com.xfzj.getbook.common.SubsidyTrjn;
import com.xfzj.getbook.recycleview.FooterLoadMoreRVAdapter;
import com.xfzj.getbook.recycleview.LoadMoreListen;
import com.xfzj.getbook.recycleview.LoadMoreView;
import com.xfzj.getbook.views.view.SubsidytrjnsView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zj on 2016/3/24.
 */
public class BuZhuFrag extends BaseFragment implements LoadMoreView.RefreshListener, LoadMoreListen {
    public static final String PARAM = "BuZhuFrag.class";

    private String param;
    private LoadMoreView loadMoreView;
    private SubsidyTrjnAdapter subsidyTrjnAdapter;
    private List<SubsidyTrjn> list = new ArrayList<>();
    private int page = 1;


    public BuZhuFrag() {

    }


    public static BuZhuFrag newInstance(String param) {
        BuZhuFrag buZhuFrag = new BuZhuFrag();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM, param);
        buZhuFrag.setArguments(bundle);
        return buZhuFrag;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            param = getArguments().getString(PARAM);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_liushuihistory, container,false);

        loadMoreView = (LoadMoreView) view.findViewById(R.id.loadMoreView);
        loadMoreView.setVisibility(View.GONE);
        loadMoreView.setOnrefreshListener(this);
        loadMoreView.setOnLoadMoreListen(this);
        subsidyTrjnAdapter = new SubsidyTrjnAdapter(list, getActivity());
        loadMoreView.setAdapter(subsidyTrjnAdapter);
        onRefresh();
        return view;
    }

    @Override
    public void onRefresh() {
        page = 1;
        loadMoreView.setRefreshing();
    
        GetSubsidyTrjnAsync getSubsidyTrjnAsync = new GetSubsidyTrjnAsync(getActivity());
        getSubsidyTrjnAsync.executeOnExecutor(AppActivity.getThreadPoolExecutor(), String.valueOf(page));
        getSubsidyTrjnAsync.setOnUcardTaskListener(new UcardAsyncTask.OnUcardTaskListener<List<SubsidyTrjn>>() {
            @Override
            public void onSuccess(List<SubsidyTrjn> subsidyTrjns) {
                if(!loadMoreView.isShown()) {
                    loadMoreView.setVisibility(View.VISIBLE);
                }
                loadMoreView.setRefreshFinish();
                subsidyTrjnAdapter.clear();
                subsidyTrjnAdapter.addAll(subsidyTrjns);

            }

            @Override
            public void onFail(String s) {
                loadMoreView.setRefreshFinish();
            }
        });
    }

    @Override
    public void onLoadMore() {
        page++;
        GetSubsidyTrjnAsync getSubsidyTrjnAsync = new GetSubsidyTrjnAsync(getActivity());
        getSubsidyTrjnAsync.executeOnExecutor(AppActivity.getThreadPoolExecutor(),String.valueOf(page));
        getSubsidyTrjnAsync.setOnUcardTaskListener(new UcardAsyncTask.OnUcardTaskListener<List<SubsidyTrjn>>() {
            @Override
            public void onSuccess(List<SubsidyTrjn> subsidyTrjns) {
                loadMoreView.setLoadMoreFinish();
                subsidyTrjnAdapter.addAll(subsidyTrjns);
            }

            @Override
            public void onFail(String s) {
                loadMoreView.setLoadMoreFinish();
            }
        });
    }

    private class SubsidyTrjnAdapter extends FooterLoadMoreRVAdapter<SubsidyTrjn> {

        public SubsidyTrjnAdapter(List<SubsidyTrjn> datas, Context context) {
            super(datas, context);
        }

        @Override
        protected View getNormalView() {
            return new SubsidytrjnsView(getActivity());


        }

        @Override
        protected RecyclerView.ViewHolder getNormalViewHolder(View view, int viewType) {
            return new NormalViewHolder<SubsidyTrjn>(view, viewType) {

                @Override
                protected void setNormalContent(View itemView, SubsidyTrjn item, int viewType) {
                    if (itemView instanceof SubsidytrjnsView) {
                        ((SubsidytrjnsView) itemView).update(item);
                    }
                }
            };
        }
    }
}
