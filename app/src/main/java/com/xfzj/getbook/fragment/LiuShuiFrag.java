package com.xfzj.getbook.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.xfzj.getbook.BaseApplication;
import com.xfzj.getbook.R;
import com.xfzj.getbook.async.GetBillAllAsync;
import com.xfzj.getbook.async.UcardAsyncTask;
import com.xfzj.getbook.common.Bill;
import com.xfzj.getbook.common.BillShare;
import com.xfzj.getbook.common.CommonShareSetting;
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.utils.AppAnalytics;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.utils.MyUtils;
import com.xfzj.getbook.utils.ShareUtils;
import com.xfzj.getbook.views.view.DatePickerView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by zj on 2016/3/24.
 */
public class LiuShuiFrag extends BaseFragment implements ViewPager.OnPageChangeListener, View.OnClickListener {
    public static final String PARAM = "LiuShuiFrag.class";

    private String param;

    private ViewPager pager;
    private PagerSlidingTabStrip slidingTabStrip;
    private FragmentManager fm;
    private PayInfoFrag shopGroupFrag;
    private LiuShuiQueryFragment historyFrag;
    private PayInfoFrag dealTypeFrag;
    private FloatingActionButton fab;
    private String startTime, endTime;

    public LiuShuiFrag() {

    }


    public static LiuShuiFrag newINstance(String param) {
        LiuShuiFrag buZhuFrag = new LiuShuiFrag();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM, param);
        buZhuFrag.setArguments(bundle);
        return buZhuFrag;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            param = getArguments().getString(PARAM);
        }
        fm = getChildFragmentManager();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_liushui, container, false);
        pager = (ViewPager) view.findViewById(R.id.pager);
        slidingTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        init();
        return view;
    }

    private void init() {
        initLiuShuiQueryFrag();
        initShopGroupFrag();
        initDealTypeFrag();
        List<Fragment> lists = new ArrayList<>();
        lists.add(historyFrag);
        lists.add(shopGroupFrag);
        lists.add(dealTypeFrag);
        pager.setAdapter(new TestAdapter(fm, lists));
        pager.setOffscreenPageLimit(3);
        slidingTabStrip.setViewPager(pager);
        slidingTabStrip.setOnPageChangeListener(this);
        initTabsValue();
        setSelectedTextColor(0);
    }

    private void initDealTypeFrag() {
        dealTypeFrag = (PayInfoFrag) fm.findFragmentByTag(PayInfoFrag.TYPE2);
        if (null == dealTypeFrag) {
            dealTypeFrag = PayInfoFrag.newInstance(PayInfoFrag.TYPE2);
            dealTypeFrag.setFloatingActionButton(fab);
        }
    }

    private void initLiuShuiQueryFrag() {
        historyFrag = (LiuShuiQueryFragment) fm.findFragmentByTag(LiuShuiQueryFragment.PARAM);
        if (null == historyFrag) {
            historyFrag = LiuShuiQueryFragment.newInstance(LiuShuiQueryFragment.PARAM);
            historyFrag.setFloatingActionButton(fab);

        }
    }

    private void initShopGroupFrag() {
        shopGroupFrag = (PayInfoFrag) fm.findFragmentByTag(PayInfoFrag.TYPE1);
        if (null == shopGroupFrag) {
            shopGroupFrag = PayInfoFrag.newInstance(PayInfoFrag.TYPE1);
            shopGroupFrag.setFloatingActionButton(fab);

        }
    }

    /**
     * mPagerSlidingTabStrip默认值配置
     */
    private void initTabsValue() {
        // 底部游标颜色   
        slidingTabStrip.setIndicatorColor(Color.WHITE);
        // tab的分割线颜色   
        slidingTabStrip.setDividerColor(Color.TRANSPARENT);
        // tab背景   
        slidingTabStrip.setBackgroundColor(getResources().getColor(R.color.primary));
        // tab底线高度   
        slidingTabStrip.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                1, getResources().getDisplayMetrics()));
        // 游标高度   
        slidingTabStrip.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                2, getResources().getDisplayMetrics()));
        // 选中的文字颜色   
//        slidingTabStrip.setSelectedTextColor(Color.WHITE);
        // 正常文字颜色   
        slidingTabStrip.setTextColor(getResources().getColor(R.color.blue_dark));

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setSelectedTextColor(position);
        if (null != fab) {
            fab.setVisibility(View.VISIBLE);
        }
    }

    private void setSelectedTextColor(int position) {
        View view = slidingTabStrip.getChildAt(0);
        if (view instanceof LinearLayout) {
            for (int i = 0; i < 3; i++) {
                View viewText = ((LinearLayout) view).getChildAt(i);
                TextView tabTextView = (TextView) viewText;
                if (viewText instanceof TextView) {
                    if (position == i) {
                        tabTextView.setTextColor(Color.WHITE);
                    } else {
                        tabTextView.setTextColor(getResources().getColor(R.color.blue_dark));
                    }
                }
            }

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        if (R.id.fab == v.getId()) {
            AppAnalytics.onEvent(getActivity(), AppAnalytics.C_Q);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            DatePickerView datePickerView = new DatePickerView(getActivity());
            getCurrentTime();
            datePickerView.initTime(startTime, endTime);
            int itemIndex = pager.getCurrentItem();
            if (itemIndex == 0) {
                datePickerView.setDwcyVisiblity(View.GONE);
                datePickerView.setIvTodayVisiblity(View.VISIBLE);
            } else {
                datePickerView.setDwcyVisiblity(View.VISIBLE);
                datePickerView.setIvTodayVisiblity(View.GONE);
            }
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
                    LiuShuiFrag.this.startTime = startTime;
                    LiuShuiFrag.this.endTime = endTime;
                    dialog.dismiss();
                    int index = pager.getCurrentItem();
                    if (index == 0) {
                        historyFrag.query(startTime, endTime);
                    } else if (index == 1) {
                        shopGroupFrag.query(startTime, endTime);
                    } else if (index == 2) {
                        dealTypeFrag.query(startTime, endTime);
                    }
                }
            });
            datePickerView.setOnDwcyClickListener(new DatePickerView.onDwcyClickListener() {
                @Override
                public void onDwcyClick() {
                    dialog.dismiss();
//                    int itemIndex = pager.getCurrentItem();
//                    if (itemIndex == 1) {
//                        shopGroupFrag.onDwcyClick();
//                    }else if (itemIndex == 2) {
//                        dealTypeFrag.onDwcyClick();
//                    }
                    onDwcy();


                }
            });
        }
    }

    public void onDwcy() {
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
                        queryAllBill(Integer.valueOf(startYear) + "-1-1", calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH), year);


                    }
                }).setNegativeButton(getActivity().getString(R.string.no), null).create().show();
            }

        }
    }

    private void queryAllBill(String startTime, String endTime, final int year) {
        GetBillAllAsync getBillAllAsync = new GetBillAllAsync(getActivity());
        getBillAllAsync.execute(startTime, endTime);
        getBillAllAsync.setOnUcardTaskListener(new UcardAsyncTask.OnUcardTaskListener<SparseArray<List<Bill>>>() {
            @Override
            public void onSuccess(final SparseArray<List<Bill>> billArray) {
                if (null == billArray || billArray.size() == 0) {
                    MyToast.show(getActivity(), getActivity().getString(R.string.queryfail));
                    return;
                }
                final List<Bill> shopGroups = billArray.get(1);
                if (null == shopGroups || shopGroups.size() == 0) {
                    MyToast.show(getActivity(), getActivity().getString(R.string.queryfail));
                    return;
                }
                final List<Bill> dealTypes = billArray.get(2);
                if (null == dealTypes || dealTypes.size() == 0) {
                    MyToast.show(getActivity(), getActivity().getString(R.string.queryfail));
                    return;
                }
                if (null != shopGroupFrag) {
                    shopGroupFrag.setBill(shopGroups);
                }
                if (null != dealTypeFrag) {
                    dealTypeFrag.setBill(dealTypes);
                }
                BmobQuery<CommonShareSetting> query = new BmobQuery<CommonShareSetting>();
                query.findObjects(getActivity(), new FindListener<CommonShareSetting>() {

                    @Override
                    public void onSuccess(List<CommonShareSetting> list) {
                        if (null != list && list.size() == 1) {
                            if (list.get(0).isOpen()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle(R.string.share).setMessage(getActivity().getString(R.string.shareyourbill)).setPositiveButton(R.string.share, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Gson gson = new Gson();
                                        final String shopJson = gson.toJson(shopGroups);
                                        final String dealTypeJson = gson.toJson(dealTypes.get(0));
                                        final BillShare billShare = new BillShare(shopJson, dealTypeJson);
                                        billShare.save(getActivity(), new SaveListener() {
                                            @Override
                                            public void onSuccess() {
                                                share(dealTypes, year, billShare);
                                            }

                                            @Override
                                            public void onFailure(int i, String s) {

                                            }
                                        });

                                    }
                                }).setNegativeButton(R.string.no, null).create().show();
                            }
                        }
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
            }

            @Override
            public void onFail(String s) {

            }
        });
    }

    private void share(List<Bill> dealTypes, int year, BillShare billShare) {
        String[] str = dealTypes.get(0).getV().split(",");
        str[0] = str[0].substring(1, str[0].length());
        String text = getActivity().getString(R.string.sharebilltext, year, str[1], str[0]);
        String title = getActivity().getString(R.string.sharebilltitle, year);
        ShareUtils.share(getActivity(), text, title, wrapUrl(billShare.getObjectId()), R.mipmap.nuist);
    }

    private String wrapUrl(String id) {
        return BaseHttp.SHAREBILL + "objectId=" + id;
    }

    private String encode(String string) {
        try {
            return URLEncoder.encode(string, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void getCurrentTime() {
        int index = pager.getCurrentItem();
        if (index == 0) {
            String[] s = historyFrag.getCurrentTime();
            startTime = s[0];
            endTime = s[1];
        } else if (index == 1) {
            String[] s = shopGroupFrag.getCurrentTime();
            startTime = s[0];
            endTime = s[1];
        } else if (index == 2) {
            String[] s = dealTypeFrag.getCurrentTime();
            startTime = s[0];
            endTime = s[1];
        }
    }

    private class TestAdapter extends FragmentPagerAdapter {
        private List<Fragment> lists;
        private int[] tag = new int[]{R.string.liushuichaxun, R.string.shopcount, R.string.dealtype};

        public TestAdapter(FragmentManager manager, List<Fragment> lists) {
            super(manager);
            this.lists = lists;
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Fragment getItem(int position) {
            return lists.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(tag[position]);
        }

    }
}
