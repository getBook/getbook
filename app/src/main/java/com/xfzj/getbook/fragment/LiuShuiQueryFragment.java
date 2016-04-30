package com.xfzj.getbook.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.xfzj.getbook.R;
import com.xfzj.getbook.activity.AppActivity;
import com.xfzj.getbook.async.GetTrjnAsync;
import com.xfzj.getbook.async.GetTrjnCountAsync;
import com.xfzj.getbook.async.UcardAsyncTask;
import com.xfzj.getbook.common.HistoryTrjn;
import com.xfzj.getbook.views.recycleview.FooterLoadMoreRVAdapter;
import com.xfzj.getbook.views.recycleview.LoadMoreLayout;
import com.xfzj.getbook.views.recycleview.LoadMoreListen;
import com.xfzj.getbook.views.recycleview.LoadMoreView;
import com.xfzj.getbook.utils.MyUtils;
import com.xfzj.getbook.views.view.DatePickerView;
import com.xfzj.getbook.views.view.TrjnItemVIew;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zj on 2016/3/25.
 */
public class LiuShuiQueryFragment extends BaseFragment implements View.OnClickListener, LoadMoreView.RefreshListener, LoadMoreListen, LoadMoreLayout.OnScrollCallBack {


    public static final String PARAM = "PayInfoFrag.class";
    private String param;
    private LoadMoreView loadMoreView;
    private String startTime, endTime;
    private int page = 1;
    private List<HistoryTrjn> list = new ArrayList<>();
    private HistoryTrjnAdapter historyTrjnAdapter;
    private FloatingActionButton fab;

    public LiuShuiQueryFragment() {

    }


    public static LiuShuiQueryFragment newInstance(String param) {
        LiuShuiQueryFragment liuShuiQueryFragment = new LiuShuiQueryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM, param);
        liuShuiQueryFragment.setArguments(bundle);
        return liuShuiQueryFragment;
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
        loadMoreView.setOnScrollCallBack(this);

        loadMoreView.setVisibility(View.GONE);
        loadMoreView.setOnrefreshListener(this);
        loadMoreView.setOnLoadMoreListen(this);
        historyTrjnAdapter = new HistoryTrjnAdapter(list, getActivity());
        loadMoreView.setAdapter(historyTrjnAdapter);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (R.id.fab == v.getId()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            DatePickerView datePickerView = new DatePickerView(getActivity());
            datePickerView.initTime(startTime, endTime);
            int margin = (int) MyUtils.dp2px(getActivity(), 15f);
            builder.setView(datePickerView, margin, margin, margin, margin);
            final AlertDialog dialog = builder.create();
            dialog.show();
            datePickerView.setOnTimeGetListener(new DatePickerView.OnTimeGetListener() {
                @Override
                public void getTime(String startTime, String endTime) {
                    if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
                        return;
                    }

                }
            });


        }


    }


    @Override
    public void onRefresh() {
        page = 1;
        loadMoreView.setRefreshing();
        GetTrjnAsync getTrjnAsync = new GetTrjnAsync(getActivity());
        getTrjnAsync.executeOnExecutor(AppActivity.getThreadPoolExecutor(), String.valueOf(page), startTime, endTime);
        getTrjnAsync.setOnUcardTaskListener(new UcardAsyncTask.OnUcardTaskListener<List<HistoryTrjn>>() {
            @Override
            public void onSuccess(List<HistoryTrjn> historyTrjns) {
                loadMoreView.setRefreshFinish();
                historyTrjnAdapter.clear();
                historyTrjnAdapter.addAll(historyTrjns);

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
        GetTrjnAsync getTrjnAsync = new GetTrjnAsync(getActivity());
        getTrjnAsync.executeOnExecutor(AppActivity.getThreadPoolExecutor(),String.valueOf(page), startTime, endTime);
        getTrjnAsync.setOnUcardTaskListener(new UcardAsyncTask.OnUcardTaskListener<List<HistoryTrjn>>() {
            @Override
            public void onSuccess(List<com.xfzj.getbook.common.HistoryTrjn> historyTrjns) {
                loadMoreView.setLoadMoreFinish();
                historyTrjnAdapter.addAll(historyTrjns);
            }

            @Override
            public void onFail(String s) {
                loadMoreView.setLoadMoreFinish();
            }
        });
    }

    public void setFloatingActionButton(FloatingActionButton fab) {
        this.fab = fab;
    }

    @Override
    public void onScroll(boolean b) {
        if (null != fab) {
            if (b) {
                fab.setVisibility(View.GONE);
            } else {
                fab.setVisibility(View.VISIBLE);
            }
        }
    }

    public void query(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        loadMoreView.setVisibility(View.VISIBLE);
        onRefresh();
        GetTrjnCountAsync getTrjnCountAsync = new GetTrjnCountAsync(getActivity());
        getTrjnCountAsync.executeOnExecutor(AppActivity.getThreadPoolExecutor(),startTime, endTime);
        getTrjnCountAsync.setOnUcardTaskListener(new UcardAsyncTask.OnUcardTaskListener<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                Snackbar snackbar = Snackbar.make(loadMoreView, "当前共查询到" + integer + "笔消费", Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(Color.WHITE);
                snackbar.show();
            }

            @Override
            public void onFail(String s) {

            }
        });
    }

    public String[] getCurrentTime() {
        return new String[]{startTime, endTime};
    }


    private class HistoryTrjnAdapter extends FooterLoadMoreRVAdapter<HistoryTrjn> {

        public HistoryTrjnAdapter(List<HistoryTrjn> datas, Context context) {
            super(datas, context);
        }

        @Override
        protected View getNormalView() {
            return new TrjnItemVIew(getActivity());


        }

        @Override
        protected RecyclerView.ViewHolder getNormalViewHolder(View view, int viewType) {
            return new NormalViewHolder<HistoryTrjn>(view, viewType) {

                @Override
                protected void setNormalContent(View itemView, HistoryTrjn item, int viewType) {
                    if (itemView instanceof TrjnItemVIew) {
                        ((TrjnItemVIew) itemView).update(item);
                    }
                }
            };
        }
    }
}
