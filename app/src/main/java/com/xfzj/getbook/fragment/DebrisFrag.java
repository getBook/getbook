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
import com.xfzj.getbook.R;
import com.xfzj.getbook.action.QueryAction;
import com.xfzj.getbook.activity.DebrisDetailAty;
import com.xfzj.getbook.common.Debris;
import com.xfzj.getbook.recycleview.FooterLoadMoreRVAdapter;
import com.xfzj.getbook.recycleview.LoadMoreLayout;
import com.xfzj.getbook.recycleview.LoadMoreListen;
import com.xfzj.getbook.recycleview.LoadMoreView;
import com.xfzj.getbook.utils.AppAnalytics;
import com.xfzj.getbook.views.view.DebrisInfoView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DebrisFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DebrisFrag extends BaseFragment implements QueryAction.OnQueryListener<List<Debris>>, View.OnClickListener, LoadMoreListen, LoadMoreView.RefreshListener, LoadMoreLayout.OnScrollCallBack {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final String FROMMAIN = "fromMaintoDebris";
    public static final String FROMSEARCH = "fromSearchtoDebris";

    private int skip = 0;
    private int limit = 10;
    private static final int MAX_NUM = 15;
    private LoadMoreView loadMoreView;
    private LinearLayout llError;
    private Button btn;


    private DebrisAdapter debrisAdapter;
    private QueryAction queryAction;
    private List<Debris> debrises = new ArrayList<>();
    private String key;
    private LinearLayout llnodata;
    private FloatingActionsMenu fab;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment DebrisFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static DebrisFrag newInstance(String param1) {
        DebrisFrag fragment = new DebrisFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public DebrisFrag() {
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
        View view = inflater.inflate(R.layout.fragment_want, container, false);
        loadMoreView = (LoadMoreView) view.findViewById(R.id.loadMoreView);
        llError = (LinearLayout) view.findViewById(R.id.llError);
        llnodata = (LinearLayout) view.findViewById(R.id.llnodata);
        btn = (Button) view.findViewById(R.id.btn);
        btn.setOnClickListener(this);
        debrisAdapter = new DebrisAdapter(debrises, getActivity());
        loadMoreView.setAdapter(debrisAdapter);
        loadMoreView.setOnrefreshListener(this);
        loadMoreView.setOnLoadMoreListen(this);
        loadMoreView.setOnScrollCallBack(this);
        queryAction = new QueryAction(getActivity().getApplicationContext());
        if (mParam1.equals(FROMMAIN)) {
            loadMoreView.setRefreshing();
            queryAction.queryDebrisInfo(MAX_NUM, limit, skip, key);
        }else {
            AppAnalytics.onEvent(getActivity(), AppAnalytics.DB_SEARCH);
        }
        queryAction.setOnQueryListener(this);
        return view;
    }

    @Override
    public void onSuccess(List<Debris> lists) {
        //上拉刷新需要清楚之前的数据
        if (skip == 0) {
            loadMoreView.setRefreshFinish();
            debrisAdapter.clear();
        } else {
            loadMoreView.setLoadMoreFinish();
        }
        if (null == lists || lists.size() == 0) {
            if (skip == 0) {
                loadMoreView.setVisibility(View.GONE);
                if (mParam1.equals(FROMMAIN)) {
                    llError.setVisibility(View.VISIBLE);
                } else if (mParam1.equals(FROMSEARCH)) {
                    llnodata.setVisibility(View.VISIBLE);
                }
            }
        } else {
            loadMoreView.setVisibility(View.VISIBLE);
            if (mParam1.equals(FROMMAIN) && null != llError) {
                llError.setVisibility(View.GONE);
            } else if (mParam1.equals(FROMSEARCH) && null != llnodata) {
                llnodata.setVisibility(View.GONE);
            }
            debrisAdapter.addAll(lists);
//            for (Debris secondBook : lists) {
//                MyLog.print("杂货信息", secondBook.toString());
//            }
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
        queryAction.queryDebrisInfo(MAX_NUM, limit, skip, key);
    }

    @Override
    public void onLoadMore() {
        queryAction.queryDebrisInfo(MAX_NUM, limit, ++skip, key);

    }

    @Override
    public void onClick(View v) {
        onRefresh();
    }

    public void searchKey(String key) {
        this.key = key;
        onRefresh();


    }

    public void setFloatingBUtton(FloatingActionsMenu fab) {
        this.fab = fab;
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

    private class DebrisAdapter extends FooterLoadMoreRVAdapter<Debris> {
        private DebrisInfoView debrisInfoView;

        public DebrisAdapter(List<Debris> datas, Context context) {
            super(datas, context);
        }

        @Override
        protected View getNormalView() {
            View view = LayoutInflater.from(context).inflate(R.layout.wrap_debrisinfo, null);

            debrisInfoView = (DebrisInfoView) view.findViewById(R.id.debrisInfoView);

            view.setTag(debrisInfoView);

            return view;
        }


        @Override
        protected RecyclerView.ViewHolder getNormalViewHolder(View view, int viewType) {
            return new NormalViewHolder<Debris>(view, viewType) {
                @Override
                protected void setNormalContent(View itemView, Debris item, int viewType) {
                    final DebrisInfoView debrisInfoView = ((DebrisInfoView) itemView.getTag());
                    debrisInfoView.update(item);

//                    debrisInfoView.setOnUserInfoClick(new DebrisInfoView.onClickListener<User>() {
//                        @Override
//                        public void onClick(User user) {
////                            MyLog.print("点击的人物", user.toString());
//                        }
//                    });

                    debrisInfoView.setOnDebrisInfoClick(new DebrisInfoView.onClickListener<Debris>() {
                        @Override
                        public void onClick(Debris debris) {
//                            MyLog.print("点击的书本", debris.toString());
                            AppAnalytics.onEvent(getActivity(), AppAnalytics.CLICK_DEBRIS);
                            Intent intent = new Intent(getActivity(), DebrisDetailAty.class);
                            intent.putExtra(DebrisDetailAty.DATA, debris);
                            startActivity(intent);
                        }
                    });
                }
            };
        }
    }
}
