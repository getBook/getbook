package com.xfzj.getbook.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.xfzj.getbook.R;
import com.xfzj.getbook.fragment.DebrisFrag;
import com.xfzj.getbook.fragment.SecondBookFrag;
import com.xfzj.getbook.views.view.BaseToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by zj on 2016/3/7.
 */
public class SearchAty extends AppActivity implements SearchView.OnQueryTextListener, ViewPager.OnPageChangeListener {
    @Bind(R.id.baseToolbar)
    BaseToolBar baseToolBar;
    @Bind(R.id.fram)
    FrameLayout fram;
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.tabs)
    PagerSlidingTabStrip slidingTabStrip;
    private SearchView searchView;
    private SecondBookFrag sbFrag;
    private DebrisFrag debrisFrag;
    private FragmentManager fm;
    private String query;


    @Override
    protected void onSetContentView() {
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        baseToolBar.initToolbar(this, "");
        fm = getSupportFragmentManager();
        init();
    }

    private void init() {
        initSaleFrag();
        initWantFrag();
        List<Fragment> lists = new ArrayList<>();
        lists.add(sbFrag);
        lists.add(debrisFrag);
        pager.setAdapter(new TestAdapter(getSupportFragmentManager(), lists));
        slidingTabStrip.setViewPager(pager);
        slidingTabStrip.setOnPageChangeListener(this);
        initTabsValue();
        setSelectedTextColor(0);
    }

    private void initSaleFrag() {
        sbFrag = (SecondBookFrag) fm.findFragmentByTag(SecondBookFrag.FROMSEARCH);
        if (null == sbFrag) {
            sbFrag = SecondBookFrag.newInstance(SecondBookFrag.FROMSEARCH);
        }
    }

    private void initWantFrag() {
        debrisFrag = (DebrisFrag) fm.findFragmentByTag(DebrisFrag.FROMSEARCH);
        if (null == debrisFrag) {
            debrisFrag = DebrisFrag.newInstance(DebrisFrag.FROMSEARCH);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.search_badge);//在菜单中找到对应控件的item
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setQueryHint(getString(R.string.please_input_keywords));
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.expandActionView(menuItem);
        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {//设置打开关闭动作监听
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                finish();
                return true;
            }
        });

        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        this.query = query;
        int index = pager.getCurrentItem();
        if (index == 0) {
            sbFrag.searchKey(query);

        } else {
            debrisFrag.searchKey(query);
        }


        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {


        return false;
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
        query = searchView.getQuery().toString();
        if (TextUtils.isEmpty(query)) {
            return;
        }
        if (position == 0) {
            sbFrag.searchKey(query);
        } else {
            debrisFrag.searchKey(query);
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
