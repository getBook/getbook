package com.xfzj.getbook.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.activity.AppActivity;
import com.xfzj.getbook.async.BaseAsyncTask;
import com.xfzj.getbook.async.GetPaperSearchAsync;
import com.xfzj.getbook.common.Paper;
import com.xfzj.getbook.views.recycleview.FooterLoadMoreRVAdapter;
import com.xfzj.getbook.views.recycleview.LoadMoreListen;
import com.xfzj.getbook.views.recycleview.LoadMoreView;
import com.xfzj.getbook.views.view.PaperItemVIew;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zj on 2016/4/2.
 */
public class PaperSearchFrag extends BaseFragment implements LoadMoreView.RefreshListener, LoadMoreListen, View.OnClickListener {
    public static final String PARAM = "PaperSearchFrag";

    private String param;

    private LoadMoreView loadMoreView;
    private LinearLayout llnodata, llError;
    private Button btn;
    private PaperSearchAdapter paperSearchAdapter;
    private List<Paper> papers = new ArrayList<>();
    private String key;

    public static PaperSearchFrag newInstance(String param) {
        PaperSearchFrag paperSearchFrag = new PaperSearchFrag();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM, param);
        paperSearchFrag.setArguments(bundle);
        return paperSearchFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            param = getArguments().getString(PARAM);
        }

    }

    @Override
    public LoadMoreView getLoadMoreView() {
        return loadMoreView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sale, container, false);
        loadMoreView = (LoadMoreView) view.findViewById(R.id.loadMoreView);
        llnodata = (LinearLayout) view.findViewById(R.id.llnodata);
        llError = (LinearLayout) view.findViewById(R.id.llError);
        btn = (Button) view.findViewById(R.id.btn);
        paperSearchAdapter = new PaperSearchAdapter(papers, getActivity());
        loadMoreView.setVisibility(View.GONE);
        loadMoreView.setAdapter(paperSearchAdapter);
        loadMoreView.setOnrefreshListener(this);
        loadMoreView.setOnLoadMoreListen(this);
        btn.setOnClickListener(this);
        return view;
    }


    @Override
    public void onRefresh() {
        paperSearchAdapter.clear();
        loadMoreView.setRefreshing();
        llnodata.setVisibility(View.GONE);
        llError.setVisibility(View.GONE);
        paperSearchAdapter.setFootViewVisibility(View.GONE);
        GetPaperSearchAsync getPaperSearchAsync = new GetPaperSearchAsync(getActivity());
        getPaperSearchAsync.executeOnExecutor(AppActivity.getThreadPoolExecutor(), key);
        getPaperSearchAsync.setOnTaskListener(new BaseAsyncTask.onTaskListener<List<Paper>>() {
            @Override
            public void onSuccess(List<Paper> papers) {
                if (null == loadMoreView) {
                    return;
                }
                if (papers.size() == 0) {
                    llnodata.setVisibility(View.VISIBLE);
                    llError.setVisibility(View.GONE);
                    loadMoreView.setVisibility(View.GONE);
                    return;
                }
                loadMoreView.setRefreshFinish();
                llnodata.setVisibility(View.GONE);
                loadMoreView.setVisibility(View.VISIBLE);
                paperSearchAdapter.addAll(papers);
                paperSearchAdapter.setFootViewVisibility(View.VISIBLE);

            }

            @Override
            public void onFail() {
                if (null == loadMoreView) {
                    return;
                }
                loadMoreView.setRefreshFinish();
                llError.setVisibility(View.VISIBLE);
                llnodata.setVisibility(View.GONE);
                loadMoreView.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onLoadMore() {


    }


    public void searchKey(String key) {
        this.key = key;
        onRefresh();
    }

    @Override
    public void onClick(View v) {
        onRefresh();
    }

    private class PaperSearchAdapter extends FooterLoadMoreRVAdapter<Paper> {
        private View footView;
        public PaperSearchAdapter(List<Paper> datas, Context context) {
            super(datas, context);
        }

        @Override
        protected View getFooterView() {
            TextView textView = new TextView(getActivity());
            textView.setText(R.string.duohuo_support);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(getActivity().getResources().getColor(R.color.secondary_text));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            textView.setLayoutParams(params);
            this.footView = textView;
            return textView;
        }

        public void setFootViewVisibility(int i) {
            if (null != footView) {
                footView.setVisibility(i);
            }
        }
        @Override
        protected View getNormalView() {
            return new PaperItemVIew(getActivity());
        }

        @Override
        protected RecyclerView.ViewHolder getNormalViewHolder(View view, int viewType) {
            return new NormalViewHolder<Paper>(view, viewType) {
                @Override
                protected void setNormalContent(View itemView, final Paper item, int viewType) {
                    if (itemView instanceof PaperItemVIew) {
                        ((PaperItemVIew) itemView).update(item);
                        ((PaperItemVIew) itemView).setOnPaperClickListener(new PaperItemVIew.OnPaperClickListener() {
                            @Override
                            public void onPaperClick(String link) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(link));
                                startActivity(intent);
                            }
                        });
                    }

                }
            };
        }
    }
}
