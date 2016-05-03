package com.xfzj.getbook.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xfzj.getbook.R;
import com.xfzj.getbook.activity.AppActivity;
import com.xfzj.getbook.async.BaseAsyncTask;
import com.xfzj.getbook.async.GetAsordListAsync;
import com.xfzj.getbook.common.AsordBook;
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.views.recycleview.FooterLoadMoreRVAdapter;
import com.xfzj.getbook.views.view.AsordListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zj on 2016/5/2.
 */
public class AsordFrag extends BaseFragment {
    public static final String ARG_PARAM1 = "AsordFrag";
    @Bind(R.id.loadMoreView)
    RecyclerView loadMoreView;
    @Bind(R.id.llnodata)
    LinearLayout llnodata;
    @Bind(R.id.btn)
    Button btn;
    @Bind(R.id.llError)
    LinearLayout llError;
    private String mParam1;
    private AsordListAdapter asordListAdapter;
    private List<AsordBook> list = new ArrayList<>();

    public static AsordFrag newInstance(String param1) {
        AsordFrag fragment = new AsordFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public AsordFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asord, container, false);
        ButterKnife.bind(this, view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        loadMoreView.setLayoutManager(layoutManager);
        asordListAdapter = new AsordListAdapter(list, getActivity());
        loadMoreView.setAdapter(asordListAdapter);
        getAsordList();
        return view;
    }

    private void getAsordList() {
        GetAsordListAsync getAsordListAsync = new GetAsordListAsync(getActivity());
        getAsordListAsync.executeOnExecutor(AppActivity.THREAD_POOL_EXECUTOR, BaseHttp.GETASORDLIST);
        getAsordListAsync.setOnTaskListener(new BaseAsyncTask.onTaskListener<List<AsordBook>>() {
            @Override
            public void onSuccess(List<AsordBook> asordBooks) {
                if (null == loadMoreView) {
                    return;
                }
                if (null == asordBooks || asordBooks.size() == 0) {
                    loadMoreView.setVisibility(View.GONE);
                    llnodata.setVisibility(View.VISIBLE);
                    llError.setVisibility(View.GONE);
                } else {
                    loadMoreView.setVisibility(View.VISIBLE);
                    llnodata.setVisibility(View.GONE);
                    llError.setVisibility(View.GONE);
                    asordListAdapter.clear();
                    asordListAdapter.addAll(asordBooks);
                }
            }

            @Override
            public void onFail() {
                if (null == loadMoreView) {
                    return;
                }
                loadMoreView.setVisibility(View.GONE);
                llError.setVisibility(View.VISIBLE);
                llnodata.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn)
    public void onClick() {
        getAsordList();
    }

    private class AsordListAdapter extends FooterLoadMoreRVAdapter<AsordBook> {
        public AsordListAdapter(List<AsordBook> datas, Context context) {
            super(datas, context);
        }

        @Override
        protected View getNormalView() {
            return new AsordListView(getActivity());
        }

        @Override
        protected RecyclerView.ViewHolder getNormalViewHolder(View view, int viewType) {
            return new NormalViewHolder<AsordBook>(view, viewType) {
                @Override
                protected void setNormalContent(View itemView, AsordBook item, int viewType) {
                    if (itemView instanceof AsordListView) {
                        ((AsordListView) itemView).update(item);
                    }
                }
            };
        }
    }

}
