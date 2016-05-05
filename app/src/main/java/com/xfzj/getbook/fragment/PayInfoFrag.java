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
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.utils.ShareUtils;
import com.xfzj.getbook.views.recycleview.FooterLoadMoreRVAdapter;
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

        View view = inflater.inflate(R.layout.fragment_payinfo, container, false);
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
        queryBill(startTime, endTime, false, 0);

    }

    /**
     * @param startTime 查询起始时间
     * @param endTime   查询终止时间
     * @param b         是否是带我穿越查询
     * @param year      大学几年
     */
    private void queryBill(String startTime, String endTime, final boolean b, final int year) {
        loadMoreView.setVisibility(View.VISIBLE);
        GetBillAsync getBillAsync = new GetBillAsync(getActivity());
        getBillAsync.execute(param, startTime, endTime);
        getBillAsync.setOnUcardTaskListener(new UcardAsyncTask.OnUcardTaskListener<List<Bill>>() {
            @Override
            public void onSuccess(final List<Bill> bills) {
                shopGroupAdapter.clear();
                shopGroupAdapter.addAll(bills);
                if (param.equals(TYPE2) && bills.size() > 0 && b) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.share).setMessage("将您的消费情况分享出去，炫耀一下吧！").setPositiveButton(R.string.share, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String[] str = bills.get(0).getV().split(",");
                            str[0] = str[0].substring(1, str[0].length());
                            String text = year + "年的大学时光，我一共使用一卡通进行了" + str[1] + "笔消费，共计" + str[0] + "元，快来看看你的大学账单吧！快下载盖饭——一款南信大学生专属APP！";
                            String title = "一卡通" + year + "年消费情况";
                            ShareUtils.share(getActivity(), text, title, R.mipmap.nuist);

                        }
                    }).setNegativeButton(R.string.no, null).create().show();
                }

            }

            @Override
            public void onFail(String s) {
//                loadMoreView.setRefreshFinish();
                if (null != loadMoreView) {
                    loadMoreView.setVisibility(View.GONE);
                }
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

                final int year = calendar.get(Calendar.YEAR) - Integer.valueOf(sno);
                final String startYear = sno;
                builder.setMessage(getActivity().getString(R.string.see_pay_info, year + "")).setPositiveButton(getActivity().getString(R.string.start_pay_info), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        queryBill(Integer.valueOf(startYear) + "-1-1", calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH), true, year);

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

    public void setBill(List<Bill> bills) {
        if (null == bills || bills.size() == 0) {
            MyToast.show(getActivity(), getActivity().getString(R.string.queryfail));
            return;
        }
        loadMoreView.setVisibility(View.VISIBLE);
        shopGroupAdapter.clear();
        shopGroupAdapter.addAll(bills);
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
