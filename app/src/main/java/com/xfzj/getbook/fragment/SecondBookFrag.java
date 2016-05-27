package com.xfzj.getbook.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.xfzj.getbook.Constants;
import com.xfzj.getbook.R;
import com.xfzj.getbook.action.QueryAction;
import com.xfzj.getbook.activity.DetailActivity;
import com.xfzj.getbook.activity.SecondBookDetailAty;
import com.xfzj.getbook.common.SecondBook;
import com.xfzj.getbook.views.recycleview.FooterLoadMoreRVAdapter;
import com.xfzj.getbook.views.recycleview.LoadMoreLayout;
import com.xfzj.getbook.views.recycleview.LoadMoreListen;
import com.xfzj.getbook.views.recycleview.LoadMoreView;
import com.xfzj.getbook.utils.AppAnalytics;
import com.xfzj.getbook.views.view.SecondBookInfoView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecondBookFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondBookFrag extends BaseFragment implements QueryAction.OnQueryListener<List<SecondBook> >, LoadMoreListen, View.OnClickListener, LoadMoreView.RefreshListener, LoadMoreLayout.OnScrollCallBack {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static int MAX_NUM = Constants.day;
    private static final String FROMMYSALE = "fromSearchtoBook";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final String FROMMAIN = "fromMaintoBook";
    public static final String FROMSEARCH = "fromSearchtoBook";
    private int skip = 0;
    private int limit = 10;


    private LoadMoreView loadMoreView;
    private LinearLayout llError;
    private LinearLayout llnodata;
    private Button btn;


    private SaleAdapter saleAdapter;
    private QueryAction queryAction;
    private List<SecondBook> secondBooks = new ArrayList<>();

    private String key;
    private FloatingActionsMenu fab;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment SecondBookFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondBookFrag newInstance(String param1) {
        SecondBookFrag fragment = new SecondBookFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public SecondBookFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sale, container, false);
        loadMoreView = (LoadMoreView) view.findViewById(R.id.loadMoreView);
        llError = (LinearLayout) view.findViewById(R.id.llError);
        llnodata = (LinearLayout) view.findViewById(R.id.llnodata);
        btn = (Button) view.findViewById(R.id.btn);
        btn.setOnClickListener(this);
        saleAdapter = new SaleAdapter(secondBooks, getActivity());
        loadMoreView.setAdapter(saleAdapter);
        loadMoreView.setOnrefreshListener(this);
        loadMoreView.setOnLoadMoreListen(this);
        loadMoreView.setOnScrollCallBack(this);
        queryAction = new QueryAction(getActivity().getApplicationContext());
        if (mParam1.equals(FROMMAIN)) {
            loadMoreView.setRefreshing();
            queryAction.querySecondBookInfo(MAX_NUM, limit, skip, key);
        }else {
            AppAnalytics.onEvent(getActivity(), AppAnalytics.SB_SEARCH);
        }
        queryAction.setOnQueryListener(this);
        return view;
    }

    @Override
    public void onSuccess(List<SecondBook> lists) {

        //上拉刷新需要清楚之前的数据
        if (skip == 0) {
            loadMoreView.setRefreshFinish();
            saleAdapter.clear();
        } else {
            loadMoreView.setLoadMoreFinish();
        }
        if (null == lists || lists.size() == 0) {
            if (skip == 0) {
                if (mParam1.equals(FROMMAIN) && null != llError) {
                    llError.setVisibility(View.VISIBLE);
                } else if (mParam1.equals(FROMSEARCH) && null != llnodata) {
                    llnodata.setVisibility(View.VISIBLE);
                }
                loadMoreView.setVisibility(View.GONE);
            }
        } else {
            loadMoreView.setVisibility(View.VISIBLE);
            if (mParam1.equals(FROMMAIN) && null != llError) {
                llError.setVisibility(View.GONE);
            } else if (mParam1.equals(FROMSEARCH) && null != llnodata) {
                llnodata.setVisibility(View.GONE);
            }
            saleAdapter.addAll(lists);
        }


    }


    @Override
    public void onFail() {
        if (null != loadMoreView) {
            loadMoreView.setVisibility(View.GONE);
        }
        if (mParam1.equals(FROMMAIN) && null != llError) {
            llError.setVisibility(View.VISIBLE);
        } else if (mParam1.equals(FROMSEARCH) && null != llnodata) {
            llnodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefresh() {
        if (FROMSEARCH.equals(mParam1) && TextUtils.isEmpty(key)) {
            loadMoreView.setRefreshFinish();
            return;
        }
        skip = 0;
        loadMoreView.setRefreshing();
        queryAction.querySecondBookInfo(MAX_NUM, limit, skip, key);
    }

    @Override
    public void onLoadMore() {
        queryAction.querySecondBookInfo(MAX_NUM, limit, ++skip, key);
    }

    @Override
    public void onClick(View v) {
        onRefresh();
    }

    public void searchKey(String key) {
        this.key = key;
        onRefresh();
    }

    @Override
    public void onScroll(boolean b) {
        if (null == fab) {
            return ;
        }
        if (b) {
            fab.collapseImmediately();
            fab.setVisibility(View.GONE);
        } else {
            fab.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public LoadMoreView getLoadMoreView() {
        return loadMoreView;
    }

    public void setFloatingBUtton(FloatingActionsMenu fab) {
        this.fab = fab;
    }

    public class SaleAdapter extends FooterLoadMoreRVAdapter<SecondBook> {

        private SecondBookInfoView secondBookInfoView;

        public SaleAdapter(List<SecondBook> datas, Context context) {
            super(datas, context);
        }

        @Override
        protected View getNormalView() {
            View view = LayoutInflater.from(context).inflate(R.layout.wrap_secondbookinfo, null);

            secondBookInfoView = (SecondBookInfoView) view.findViewById(R.id.secondbookinfoview);

            view.setTag(secondBookInfoView);

            return view;
        }
        @Override
        protected RecyclerView.ViewHolder getNormalViewHolder(View view, int viewType) {
            return new NormalViewHolder<SecondBook>(view, viewType) {
                @Override
                protected void setNormalContent(View itemView, SecondBook item, int viewType) {
                    final SecondBookInfoView bookInfoView = ((SecondBookInfoView) itemView.getTag());
                    bookInfoView.update(item);
                    bookInfoView.setOnSecondBookInfoClick(new SecondBookInfoView.onClickListener<SecondBook>() {
                        @Override
                        public void onClick(SecondBook secondBook) {
//                            MyLog.print("点击的书本", secondBook.toString());
                            AppAnalytics.onEvent(getActivity(), AppAnalytics.CLICK_SECONDBOOK);
                            Intent intent = new Intent(getActivity(), SecondBookDetailAty.class);
                            intent.putExtra(SecondBookDetailAty.DATA, secondBook);
                            intent.putExtra(DetailActivity.IMAGE, bookInfoView.getSecondBookImage());
                            startActivity(intent);
                        }
                    });
                }
            };
        }
    }
}