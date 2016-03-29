package com.xfzj.getbook.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.xfzj.getbook.R;
import com.xfzj.getbook.utils.MyUtils;
import com.xfzj.getbook.views.view.DatePickerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zj on 2016/3/24.
 */
public class LiuShuiFrag extends Fragment implements ViewPager.OnPageChangeListener, View.OnClickListener {
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

        View view = inflater.inflate(R.layout.fragment_liushui, null);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            DatePickerView datePickerView = new DatePickerView(getActivity());
            getCurrentTime();
            datePickerView.initTime(startTime, endTime);
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
