package com.xfzj.getbook.fragment;

import android.content.Intent;
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
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.xfzj.getbook.R;
import com.xfzj.getbook.activity.CaptureAty;
import com.xfzj.getbook.activity.PublishDebrisActivity;
import com.xfzj.getbook.utils.AppAnalytics;

import java.util.ArrayList;
import java.util.List;

public class HomeFrag extends BaseFragment implements ViewPager.OnPageChangeListener, View.OnClickListener {
    public static final String ARG_PARAM1 = "HomeFrag.class";

    
    ViewPager pager;
    PagerSlidingTabStrip slidingTabStrip;
    FloatingActionsMenu fab;
    FloatingActionButton fab1;
    FloatingActionButton fab2;
    private SecondBookFrag saleFrag;
    private DebrisFrag wantFrag;
    private FragmentManager fm;
    /**
     * 从哪个activity进入
     */
    private String from;
    private String mParam1;

    public static HomeFrag newInstance(String param1) {
        HomeFrag fragment = new HomeFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        fm=getChildFragmentManager();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        pager = (ViewPager) view.findViewById(R.id.pager);
        slidingTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        fab = (FloatingActionsMenu) view.findViewById(R.id.fab);
        fab1 = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        init();
        return view;
    }
    /**
     * 初始化菜单
     */
    private void init() {
        initSaleFrag();
        initWantFrag();
        List<Fragment> lists = new ArrayList<>();
        lists.add(saleFrag);
        lists.add(wantFrag);
        pager.setAdapter(new TestAdapter(fm, lists));
        pager.setOffscreenPageLimit(3);
        slidingTabStrip.setViewPager(pager);
        slidingTabStrip.setOnPageChangeListener(this);
        initTabsValue();
        setSelectedTextColor(0);
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

    private void initSaleFrag() {
        saleFrag = (SecondBookFrag) fm.findFragmentByTag(SecondBookFrag.FROMMAIN);
        if (null == saleFrag || saleFrag.isDetached()) {
            saleFrag = SecondBookFrag.newInstance(SecondBookFrag.FROMMAIN);
            saleFrag.setFloatingBUtton(fab);
        }

    }

    private void initWantFrag() {
        wantFrag = (DebrisFrag) fm.findFragmentByTag(DebrisFrag.FROMMAIN);

        if (null == wantFrag || saleFrag.isDetached()) {
            wantFrag = DebrisFrag.newInstance(DebrisFrag.FROMMAIN);
            wantFrag.setFloatingBUtton(fab);
        }

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setSelectedTextColor(position);
        if (null != fab) {
            fab.collapseImmediately();
            fab.setVisibility(View.VISIBLE);
        }
    }

    private void setSelectedTextColor(int position) {
        ViewGroup view = (ViewGroup) slidingTabStrip.getChildAt(0);
        if (view instanceof LinearLayout) {
            for (int i = 0; i < view.getChildCount(); i++) {
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
        switch (v.getId()) {
            case R.id.fab1:
                AppAnalytics.onEvent(getActivity(), AppAnalytics.C_PUBLISH_SB);
                fab.collapseImmediately();
                Intent intent = new Intent(getActivity(), CaptureAty.class);
                startActivity(intent);
                break;
            case R.id.fab2:
                AppAnalytics.onEvent(getActivity(), AppAnalytics.C_PUBLISH_DB);
                fab.collapseImmediately();
                Intent intent2 = new Intent(getActivity(), PublishDebrisActivity.class);
                startActivity(intent2);
                break;
        }
    }

    private class TestAdapter extends FragmentPagerAdapter {
        private List<Fragment> lists;
        private int[] tag = new int[]{R.string.secondbook, R.string.drugstore, R.string.my};

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
