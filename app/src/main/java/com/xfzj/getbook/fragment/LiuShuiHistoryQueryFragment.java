package com.xfzj.getbook.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.xfzj.getbook.R;
import com.xfzj.getbook.async.GetHistoryTrjnAsync;
import com.xfzj.getbook.async.UcardAsyncTask;
import com.xfzj.getbook.common.HistoryTrjn;
import com.xfzj.getbook.recycleview.FooterLoadMoreRVAdapter;
import com.xfzj.getbook.recycleview.LoadMoreLayout;
import com.xfzj.getbook.recycleview.LoadMoreListen;
import com.xfzj.getbook.recycleview.LoadMoreView;
import com.xfzj.getbook.utils.MyLog;
import com.xfzj.getbook.utils.MyUtils;
import com.xfzj.getbook.views.view.DatePickerView;
import com.xfzj.getbook.views.view.TrjnItemVIew;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zj on 2016/3/25.
 */
public class LiuShuiHistoryQueryFragment extends Fragment implements View.OnClickListener, LoadMoreView.RefreshListener, LoadMoreListen, LoadMoreLayout.OnScrollCallBack {


    public static final String PARAM = "LiuShuiQueryFragment.class";
    public static final String HISTORY = "history";
    public static final String TODAY = "today";
    private String param;
    private LoadMoreView loadMoreView;
    private String startTime, endTime;
    private int page = 1;
    private List<HistoryTrjn> list = new ArrayList<>();
    private HistoryTrjnAdapter historyTrjnAdapter;
    private FloatingActionButton fab;

    public LiuShuiHistoryQueryFragment() {

    }


    public static LiuShuiHistoryQueryFragment newInstance(String param) {
        LiuShuiHistoryQueryFragment liuShuiHistoryQueryFragment = new LiuShuiHistoryQueryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM, param);
        liuShuiHistoryQueryFragment.setArguments(bundle);
        return liuShuiHistoryQueryFragment;
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

        View view = inflater.inflate(R.layout.fragment_liushuihistory, null);

        loadMoreView = (LoadMoreView) view.findViewById(R.id.loadMoreView);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        loadMoreView.setOnScrollCallBack(this);
        
        loadMoreView.setVisibility(View.GONE);
        loadMoreView.setOnrefreshListener(this);
        loadMoreView.setOnLoadMoreListen(this);
        fab.setOnClickListener(this);
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
            builder.setView(datePickerView,margin,margin,margin,margin);
            final AlertDialog dialog = builder.create();
            dialog.show();
            datePickerView.setOnTimeGetListener(new DatePickerView.OnTimeGetListener() {
                @Override
                public void getTime(String startTime, String endTime) {
                    if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
                        return;
                    }
                    LiuShuiHistoryQueryFragment.this.startTime = startTime;
                    LiuShuiHistoryQueryFragment.this.endTime = endTime;
                    dialog.dismiss();
                    loadMoreView.setVisibility(View.VISIBLE);
                    onRefresh();

                }
            });


        }


    }


    @Override
    public void onRefresh() {
        page = 1;
        loadMoreView.setRefreshing();

        GetHistoryTrjnAsync getHistoryTrjnAsync = new GetHistoryTrjnAsync(getActivity());
        getHistoryTrjnAsync.execute(String.valueOf(page), startTime, endTime);
        getHistoryTrjnAsync.setOnUcardTaskListener(new UcardAsyncTask.OnUcardTaskListener<List<HistoryTrjn>>() {
            @Override
            public void onSuccess(List<HistoryTrjn> historyTrjns) {
                loadMoreView.setRefreshFinish();
                historyTrjnAdapter.clear();
                historyTrjnAdapter.addAll(historyTrjns);

            }

            @Override
            public void onFail(String s) {
                loadMoreView.setRefreshFinish();
                MyLog.print("onrefreshonFail", "");
            }
        });

    }

    @Override
    public void onLoadMore() {
        page++;
        GetHistoryTrjnAsync getHistoryTrjnAsync = new GetHistoryTrjnAsync(getActivity());
        getHistoryTrjnAsync.execute(String.valueOf(page), startTime, endTime);
        getHistoryTrjnAsync.setOnUcardTaskListener(new UcardAsyncTask.OnUcardTaskListener<List<HistoryTrjn>>() {
            @Override
            public void onSuccess(List<com.xfzj.getbook.common.HistoryTrjn> historyTrjns) {
                loadMoreView.setLoadMoreFinish();
                MyLog.print("onLoadMore", historyTrjns.toString());
                historyTrjnAdapter.addAll(historyTrjns);
            }

            @Override
            public void onFail(String s) {
                loadMoreView.setLoadMoreFinish();
                MyLog.print("onLoadMoreonFail", "");
            }
        });
    }

    @Override
    public void onScroll(boolean b) {
        if (b) {
            fab.setVisibility(View.GONE);
        }else{
            fab.setVisibility(View.VISIBLE);
        }
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
