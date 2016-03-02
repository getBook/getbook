package com.xfzj.getbook.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xfzj.getbook.R;
import com.xfzj.getbook.action.QueryAction;
import com.xfzj.getbook.common.SecondBook;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.recycleview.BaseLoadRecycleView;
import com.xfzj.getbook.recycleview.BaseRecycleViewAdapter;
import com.xfzj.getbook.recycleview.LoadMoreListen;
import com.xfzj.getbook.utils.MyLog;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.views.view.SecondBookInfoView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SaleFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaleFrag extends Fragment implements QueryAction.OnQueryListener<SecondBook>, BaseLoadRecycleView.RefreshListener, LoadMoreListen {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final String SALE = "sale";

    private int skip = 0;
    private int limit = 10;

    private BaseLoadRecycleView rc;
    private SaleAdapter saleAdapter;
    private QueryAction queryAction;
    private List<SecondBook> secondBooks = new ArrayList<>();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment SaleFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static SaleFrag newInstance(String param1) {
        SaleFrag fragment = new SaleFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public SaleFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }


        queryAction = new QueryAction(getActivity().getApplicationContext());
        queryAction.querySaleInfo(8, limit, skip);
        queryAction.setOnQueryListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sale, container, false);
        rc = (BaseLoadRecycleView) view.findViewById(R.id.recycleView);
        saleAdapter = new SaleAdapter(secondBooks, getActivity());
        rc.setAdapter(saleAdapter);
        rc.setOnrefreshListener(this);
        rc.setOnLoadMoreListen(this);
        return view;
    }

    @Override
    public void onSuccess(List<SecondBook> lists) {

        //上拉刷新需要清楚之前的数据
        if (skip == 0) {
            rc.setRefreshFinish();
            saleAdapter.clear();
        } else {
            rc.setLoadMoreFinish();
            if (null != lists && lists.size() == 0) {
                MyToast.show(getActivity(), "到头了~");
            }
        }
        saleAdapter.addAll(lists);
        for (SecondBook secondBook : lists) {
            MyLog.print("书本信息", secondBook.toString());
        }
    }

    @Override
    public void onFail() {
        MyToast.show(getActivity(), "获取信息失败");
    }

    @Override
    public void onRefresh() {
        skip = 0;
        queryAction.querySaleInfo(8, limit, skip);
        queryAction.setOnQueryListener(this);
    }

    @Override
    public void onLoadMore() {
        queryAction.querySaleInfo(8, limit, ++skip);
        queryAction.setOnQueryListener(this);
    }

    


    public class SaleAdapter extends BaseRecycleViewAdapter<SecondBook> {

        private SecondBookInfoView secondBookInfoView;

        public SaleAdapter(List<SecondBook> datas, Context context) {
            super(datas, context);
        }

        @Override
        protected View getView() {
            View view = LayoutInflater.from(context).inflate(R.layout.wrap_secondbookinfo, null);

            secondBookInfoView = (SecondBookInfoView) view.findViewById(R.id.secondbookinfoview);

            view.setTag(secondBookInfoView);

            return view;
        }

        @Override
        protected RecyclerView.ViewHolder getViewHolder(View view, int viewType) {
            return new BaseViewHolder<SecondBook>(view) {
                @Override
                protected void setContent(View itemView, SecondBook item, int viewType) {
                    SecondBookInfoView bookInfoView = ((SecondBookInfoView) itemView.getTag());
                    bookInfoView.update(item);
                    
                    bookInfoView.setOnUserInfoClick(new SecondBookInfoView.onClickListener<User>() {
                        @Override
                        public void onClick(User user) {
                            MyLog.print("点击的人物", user.toString());
                        }
                    });

                    bookInfoView.setOnSecondBookInfoClick(new SecondBookInfoView.onClickListener<SecondBook>() {
                        @Override
                        public void onClick(SecondBook secondBook) {
                            MyLog.print("点击的书本", secondBook.toString());
                        }
                    });
                }
            };
        }
    }
}