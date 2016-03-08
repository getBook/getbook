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

import com.xfzj.getbook.R;
import com.xfzj.getbook.action.QueryAction;
import com.xfzj.getbook.activity.DebrisDetailAty;
import com.xfzj.getbook.common.Debris;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.recycleview.BaseLoadRecycleView;
import com.xfzj.getbook.recycleview.BaseRecycleViewAdapter;
import com.xfzj.getbook.recycleview.LoadMoreListen;
import com.xfzj.getbook.utils.MyLog;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.views.view.DebrisInfoView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DebrisFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DebrisFrag extends Fragment implements QueryAction.OnQueryListener<Debris>, View.OnClickListener, BaseLoadRecycleView.RefreshListener, LoadMoreListen {
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
    private static final int MAX_NUM = 8;
    private BaseLoadRecycleView rc;
    private LinearLayout llError;
    private Button btn;


    private DebrisAdapter debrisAdapter;
    private QueryAction queryAction;
    private List<Debris> debrises = new ArrayList<>();
    private String key;
    private LinearLayout llnodata;

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
        queryAction = new QueryAction(getActivity().getApplicationContext());
        if (mParam1.equals(FROMMAIN)) {
            queryAction.queryDebrisInfo(MAX_NUM, limit, skip,key);
        }
        queryAction.setOnQueryListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_want, container, false);
        rc = (BaseLoadRecycleView) view.findViewById(R.id.recycleView);
        llError = (LinearLayout) view.findViewById(R.id.llError);
        llnodata = (LinearLayout) view.findViewById(R.id.llnodata);
        btn = (Button) view.findViewById(R.id.btn);
        btn.setOnClickListener(this);
        debrisAdapter = new DebrisAdapter(debrises, getActivity());
        rc.setAdapter(debrisAdapter);
        rc.setOnrefreshListener(this);
        rc.setOnLoadMoreListen(this);
        return view;
    }

    @Override
    public void onSuccess(List<Debris> lists) {
        //上拉刷新需要清楚之前的数据
        if (skip == 0) {
            rc.setRefreshFinish();
            debrisAdapter.clear();
        } else {
            rc.setLoadMoreFinish();
            if (null != lists && lists.size() == 0) {
                MyToast.show(getActivity(), "到头了~");
            }
        }
        if (null == lists || lists.size() == 0 && skip == 0) {

            rc.setVisibility(View.GONE);
            if (mParam1.equals(FROMMAIN)) {
                llError.setVisibility(View.VISIBLE);
            } else if (mParam1.equals(FROMSEARCH)) {
                llnodata.setVisibility(View.VISIBLE);
            }
        } else {
            rc.setVisibility(View.VISIBLE);
            if (mParam1.equals(FROMMAIN)) {
                llError.setVisibility(View.GONE);
            } else if (mParam1.equals(FROMSEARCH)) {
                llnodata.setVisibility(View.GONE);
            }
            debrisAdapter.addAll(lists);
            for (Debris secondBook : lists) {
                MyLog.print("杂货信息", secondBook.toString());
            }
        }
    }

    @Override
    public void onFail() {
        rc.setVisibility(View.GONE);
        if (mParam1.equals(FROMMAIN)) {
            llError.setVisibility(View.VISIBLE);
        } else if (mParam1.equals(FROMSEARCH)) {
            llnodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefresh() {
        if (FROMSEARCH.equals(mParam1) && TextUtils.isEmpty(key)) {
            rc.setRefreshFinish();
            return;
        }
        skip = 0;
        rc.setRefreshing();
        queryAction.queryDebrisInfo(MAX_NUM, limit, skip,key);
    }

    @Override
    public void onLoadMore() {
        queryAction.queryDebrisInfo(MAX_NUM, limit, ++skip,key);

    }

    @Override
    public void onClick(View v) {
        onRefresh();
    }

    public void searchKey(String key) {
        this.key = key;
        onRefresh();
        

    }

    private class DebrisAdapter extends BaseRecycleViewAdapter<Debris> {
        private DebrisInfoView debrisInfoView;

        public DebrisAdapter(List<Debris> datas, Context context) {
            super(datas, context);
        }

        @Override
        protected View getView() {
            View view = LayoutInflater.from(context).inflate(R.layout.wrap_debrisinfo, null);

            debrisInfoView = (DebrisInfoView) view.findViewById(R.id.debrisInfoView);

            view.setTag(debrisInfoView);

            return view;
        }

        @Override
        protected RecyclerView.ViewHolder getViewHolder(View view, int viewType) {
            return new BaseViewHolder<Debris>(view) {
                @Override
                protected void setContent(View itemView, Debris item, int viewType) {
                    final DebrisInfoView debrisInfoView = ((DebrisInfoView) itemView.getTag());
                    debrisInfoView.update(item);

                    debrisInfoView.setOnUserInfoClick(new DebrisInfoView.onClickListener<User>() {
                        @Override
                        public void onClick(User user) {
                            MyLog.print("点击的人物", user.toString());
                        }
                    });

                    debrisInfoView.setOnDebrisInfoClick(new DebrisInfoView.onClickListener<Debris>() {
                        @Override
                        public void onClick(Debris debris) {
                            MyLog.print("点击的书本", debris.toString());
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
