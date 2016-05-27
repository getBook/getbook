package com.xfzj.getbook.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xfzj.getbook.R;
import com.xfzj.getbook.activity.AppActivity;
import com.xfzj.getbook.async.BaseAsyncTask;
import com.xfzj.getbook.async.GetLibrarySearchAsync;
import com.xfzj.getbook.common.LibraryBook;
import com.xfzj.getbook.common.LibraryBookPosition;
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.views.recycleview.FooterLoadMoreRVAdapter;
import com.xfzj.getbook.views.recycleview.LoadMoreListen;
import com.xfzj.getbook.views.recycleview.LoadMoreView;
import com.xfzj.getbook.views.view.LibraryBookItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zj on 2016/4/2.
 */
public class LibrarySearchFrag extends BaseFragment implements LoadMoreView.RefreshListener, LoadMoreListen {
    public static final String PARAM = "LibrarySearchFrag";

    private String param;

    private LoadMoreView loadMoreView;
    private LinearLayout llnodata;
    private LibrarySearchAdapter librarySearchAdapter;
    private List<LibraryBook> libraryBooks = new ArrayList<>();
    private String key;
    private int page = 0;

    public static LibrarySearchFrag newInstance(String param) {
        LibrarySearchFrag librarySearchFrag = new LibrarySearchFrag();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM, param);
        librarySearchFrag.setArguments(bundle);
        return librarySearchFrag;
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
        librarySearchAdapter = new LibrarySearchAdapter(libraryBooks, getActivity());
        loadMoreView.setAdapter(librarySearchAdapter);
        loadMoreView.setOnrefreshListener(this);
        loadMoreView.setOnLoadMoreListen(this);
        return view;
    }


    @Override
    public void onRefresh() {
        librarySearchAdapter.clear();
        loadMoreView.setRefreshing();
        page = 0;
        llnodata.setVisibility(View.GONE);
        loadMoreView.setVisibility(View.VISIBLE);
        GetLibrarySearchAsync getLibrarySearchAsync = new GetLibrarySearchAsync(getActivity());
        getLibrarySearchAsync.executeOnExecutor(AppActivity.getThreadPoolExecutor(), wrapUrl());
        getLibrarySearchAsync.setOnTaskListener(new BaseAsyncTask.onTaskListener<List<LibraryBook>>() {
            @Override
            public void onSuccess(List<LibraryBook> libraryBooks) {
                loadMoreView.setRefreshFinish();
                llnodata.setVisibility(View.GONE);
                loadMoreView.setVisibility(View.VISIBLE);
                librarySearchAdapter.addAll(libraryBooks);

            }

            @Override
            public void onFail() {
                loadMoreView.setRefreshFinish();
                llnodata.setVisibility(View.VISIBLE);
                loadMoreView.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onLoadMore() {
        GetLibrarySearchAsync getLibrarySearchAsync = new GetLibrarySearchAsync(getActivity());
        getLibrarySearchAsync.executeOnExecutor(AppActivity.getThreadPoolExecutor(),wrapUrl());
        getLibrarySearchAsync.setOnTaskListener(new BaseAsyncTask.onTaskListener<List<LibraryBook>>() {
            @Override
            public void onSuccess(List<LibraryBook> libraryBooks) {
                loadMoreView.setLoadMoreFinish();
                llnodata.setVisibility(View.GONE);
                loadMoreView.setVisibility(View.VISIBLE);
                librarySearchAdapter.addAll(libraryBooks);

            }

            @Override
            public void onFail() {
                loadMoreView.setLoadMoreFinish();
            }
        });

    }

    public String wrapUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append(BaseHttp.GETLIBRARYSEARCH);
        sb.append("&title=" + key);
        sb.append("&page=" + (++page));
        return sb.toString();

    }

    public void searchKey(String key) {
        this.key = key;
        onRefresh();
    }

    private class LibrarySearchAdapter extends FooterLoadMoreRVAdapter<LibraryBook> {

        public LibrarySearchAdapter(List<LibraryBook> datas, Context context) {
            super(datas, context);
        }

        @Override
        protected View getNormalView() {
            return new LibraryBookItemView(getActivity());
        }

        @Override
        protected RecyclerView.ViewHolder getNormalViewHolder(View view, int viewType) {
            return new NormalViewHolder<LibraryBook>(view, viewType) {
                @Override
                protected void setNormalContent(View itemView, final LibraryBook item, int viewType) {
                    if (itemView instanceof LibraryBookItemView) {
                        ((LibraryBookItemView) itemView).update(getActivity(), item, new LibraryBookItemView.OnLibraryBookPositionLoadListener() {
                            @Override
                            public void onLoad(List<LibraryBookPosition> libraryBookPositions) {
                                item.setLibraryBookPositions(libraryBookPositions);
                            }
                        });
                    }

                }
            };
        }
    }
}
