package com.xfzj.getbook.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.xfzj.getbook.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zj on 2016/3/24.
 */
public class LiuShuiFrag extends Fragment implements ViewPager.OnPageChangeListener {
    public static final String PARAM = "LiuShuiFrag.class";

    private String param;

    private ViewPager pager;
    private PagerSlidingTabStrip slidingTabStrip;
    private FragmentManager fm;
    private LiuShuiQueryFragment todayFrag;
    private LiuShuiHistoryQueryFragment historyFrag;

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
        init();
        return view;
    }

    private void init() {
        initHistoryFrag();
        initTodayFrag();
        List<Fragment> lists = new ArrayList<>();
        lists.add(historyFrag);
        lists.add(todayFrag);
        pager.setAdapter(new TestAdapter(fm, lists));
        slidingTabStrip.setViewPager(pager);
        slidingTabStrip.setOnPageChangeListener(this);
        initTabsValue();
        setSelectedTextColor(0);
    }

    private void initHistoryFrag() {
        historyFrag = (LiuShuiHistoryQueryFragment) fm.findFragmentByTag(LiuShuiHistoryQueryFragment.HISTORY);
        if (null == historyFrag) {
            historyFrag = LiuShuiHistoryQueryFragment.newInstance(LiuShuiHistoryQueryFragment.HISTORY);
        }
    }

    private void initTodayFrag() {
        todayFrag = (LiuShuiQueryFragment) fm.findFragmentByTag(LiuShuiQueryFragment.TODAY);
        if (null == todayFrag) {
            todayFrag = LiuShuiQueryFragment.newInstance(LiuShuiQueryFragment.TODAY);
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
            for (int i = 0; i < 2; i++) {
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

    private class TestAdapter extends FragmentPagerAdapter {
        private List<Fragment> lists;
        private int[] tag = new int[]{R.string.historyQuery, R.string.todayquery};

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
