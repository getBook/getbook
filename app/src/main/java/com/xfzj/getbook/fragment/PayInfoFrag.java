package com.xfzj.getbook.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.xfzj.getbook.BaseApplication;
import com.xfzj.getbook.R;
import com.xfzj.getbook.async.GetBillAsync;
import com.xfzj.getbook.async.UcardAsyncTask;
import com.xfzj.getbook.common.Bill;
import com.xfzj.getbook.recycleview.FooterLoadMoreRVAdapter;
import com.xfzj.getbook.utils.AppAnalytics;
import com.xfzj.getbook.views.view.PayInfoVIew;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zj on 2016/3/25.
 */
public class PayInfoFrag extends BaseFragment implements View.OnClickListener, View.OnTouchListener {


    public static final String PARAM = "PayInfoFrag.class";
    public static final String TYPE1 = "1";
    public static final String TYPE2 = "2";
    private String param;
    private FloatingActionButton fab;
    private String startTime, endTime;
    private RecyclerView loadMoreView;
    private ShopGroupAdapter shopGroupAdapter;
    private List<Bill> list = new ArrayList<>();
    private int mYDown;
    private int mLastY;

    public PayInfoFrag() {

    }


    public static PayInfoFrag newInstance(String param) {
        PayInfoFrag payInfoFrag = new PayInfoFrag();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM, param);
        payInfoFrag.setArguments(bundle);
        return payInfoFrag;
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

        View view = inflater.inflate(R.layout.fragment_payinfo, container,false);
        loadMoreView = (RecyclerView) view.findViewById(R.id.loadMoreView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        loadMoreView.setLayoutManager(layoutManager);
        loadMoreView.setVisibility(View.GONE);
        loadMoreView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (null != fab) {
                    if ((mYDown - mLastY) >= ViewConfiguration.get(getActivity()).getScaledTouchSlop()) {
                        fab.setVisibility(View.GONE);
                    } else {
                        fab.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        loadMoreView.setOnTouchListener(this);
        shopGroupAdapter = new ShopGroupAdapter(list, getActivity());
        loadMoreView.setAdapter(shopGroupAdapter);

        return view;
    }

    public void setFloatingActionButton(FloatingActionButton fab) {
        this.fab = fab;
    }

    public void query(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        queryBill(startTime, endTime);

    }

    private void queryBill(String startTime, String endTime) {
        loadMoreView.setVisibility(View.VISIBLE);
        GetBillAsync getBillAsync = new GetBillAsync(getActivity());
        getBillAsync.execute(param, startTime, endTime);
        getBillAsync.setOnUcardTaskListener(new UcardAsyncTask.OnUcardTaskListener<List<Bill>>() {
            @Override
            public void onSuccess(List<Bill> bills) {
                shopGroupAdapter.clear();
                shopGroupAdapter.addAll(bills);

            }

            @Override
            public void onFail(String s) {
//                loadMoreView.setRefreshFinish();
                loadMoreView.setVisibility(View.GONE);
            }
        });
    }

    public String[] getCurrentTime() {
        return new String[]{startTime, endTime};
    }
    @Override
    public void onClick(View v) {
        if (R.id.iv == v.getId()) {
           
        

        }
    }

    public void onDwcyClick() {
        BaseApplication baseApplication = (BaseApplication) getActivity().getApplicationContext();
        if (null != baseApplication && null != baseApplication.getUser()) {
            String sno = baseApplication.getUser().getSno();
            if (!TextUtils.isEmpty(sno)) {
                sno = sno.substring(0, 4);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                Date date = new Date(System.currentTimeMillis());
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                int year = calendar.get(Calendar.YEAR) - Integer.valueOf(sno);
                final String startYear = sno;
                builder.setMessage(getActivity().getString(R.string.see_pay_info, year + "")).setPositiveButton(getActivity().getString(R.string.start_pay_info), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        queryBill(Integer.valueOf(startYear) + "-1-1", calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH));

                    }
                }).setNegativeButton(getActivity().getString(R.string.no), null).create().show();
            }

        }
    }
    
    
    @Override
    public boolean onTouch(View v, MotionEvent ev) {

        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mYDown = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                mLastY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
               
                break;
        }


        return false;
    }

    private class ShopGroupAdapter extends FooterLoadMoreRVAdapter<Bill> {

        public ShopGroupAdapter(List<Bill> datas, Context context) {
            super(datas, context);
        }

        @Override
        protected View getNormalView() {
            return new PayInfoVIew(getActivity());


        }

        @Override
        protected RecyclerView.ViewHolder getNormalViewHolder(View view, int viewType) {
            return new NormalViewHolder<Bill>(view, viewType) {

                @Override
                protected void setNormalContent(View itemView, Bill item, int viewType) {
                    if (itemView instanceof PayInfoVIew) {
                        ((PayInfoVIew) itemView).update(item);
                    }
                }
            };
        }
    }
}
