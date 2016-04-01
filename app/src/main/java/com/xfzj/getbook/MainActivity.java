package com.xfzj.getbook;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.xfzj.getbook.action.LoginAction;
import com.xfzj.getbook.activity.BaseActivity;
import com.xfzj.getbook.activity.CaptureAty;
import com.xfzj.getbook.activity.FlashActivity;
import com.xfzj.getbook.activity.LoginAty;
import com.xfzj.getbook.activity.PublishDebrisActivity;
import com.xfzj.getbook.activity.SearchAty;
import com.xfzj.getbook.async.LoginAsync;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.fragment.DebrisFrag;
import com.xfzj.getbook.fragment.MyFrag;
import com.xfzj.getbook.fragment.SecondBookFrag;
import com.xfzj.getbook.utils.AppAnalytics;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.utils.SharedPreferencesUtils;
import com.xfzj.getbook.views.view.BaseToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class MainActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, ViewPager.OnPageChangeListener, View.OnClickListener {
    public static final String FROM = "MainActivity.class";

    @Bind(R.id.fram)
    FrameLayout fram;
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.tabs)
    PagerSlidingTabStrip slidingTabStrip;
    @Bind(R.id.baseToolbar)
    BaseToolBar baseToolbar;
    @Bind(R.id.rl)
    RelativeLayout rl;
    @Bind(R.id.fab)
    FloatingActionsMenu fab;
    @Bind(R.id.fab1)
    FloatingActionButton fab1;
    @Bind(R.id.fab2)
    FloatingActionButton fab2;
    private Toolbar toolbar;

    private SecondBookFrag saleFrag;
    private DebrisFrag wantFrag;
    private MyFrag myFrag;
    private FragmentManager fm;
    private BaseApplication baseApplication;
    private User user, serverUser;
    /**
     * 从哪个activity进入
     */
    private String from;

    @Override
    protected void onSetContentView() {
        setContentView(R.layout.activity_main);


    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        baseToolbar.initToolbar(this, getResources().getString(R.string.app_name));
        toolbar = baseToolbar.getToolbar();
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        rl.setVisibility(View.VISIBLE);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setTitleTextColor(Color.WHITE);
        fm = getSupportFragmentManager();
        baseApplication = (BaseApplication) getApplication();
        user = baseApplication.getUser();
        isNeedHuaName();
        init();
        isNeedLogin();
    }

    private void isNeedHuaName() {

        if (null != user) {
            BmobQuery<User> query = new BmobQuery<>();
            query.addWhereEqualTo("sno", user.getSno());
            query.findObjects(getApplicationContext(), new FindListener<User>() {
                @Override
                public void onSuccess(List<User> list) {
                    if (null != list && list.size() != 0) {
                        serverUser = list.get(0);
                        if (TextUtils.isEmpty(serverUser.getHuaName())) {
                            showHuaNameDialog();
                        } else {
                            SharedPreferencesUtils.saveHuaName(getApplicationContext(), serverUser.getHuaName());
                            user.setHuaName(serverUser.getHuaName());

                        }
                    } else {
                        showHuaNameDialog();
                    }
                }

                @Override
                public void onError(int i, String s) {

                }
            });
        }
    }

    private void showHuaNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        final View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.huaname, null);
        final AlertDialog ad = builder.setTitle(getString(R.string.sethuaname)).setView(view).setCancelable(false).create();
        ad.show();
        final EditText edtHuaName = (EditText) view.findViewById(R.id.edtHuaName);
        Button btnEnsure = (Button) view.findViewById(R.id.btnEnsure);
        btnEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String huaName = edtHuaName.getText().toString();
                if (TextUtils.isEmpty(huaName)) {
                    MyToast.show(getApplicationContext(), getString(R.string.please_to_input, getString(R.string.nicheng)));
                } else {
                    ad.dismiss();
                    uploadHuaName(huaName);
                }
            }
        });


    }

    private void uploadHuaName(final String huaName) {
        user.setHuaName(huaName);
        user.update(getApplicationContext(), serverUser.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                SharedPreferencesUtils.saveHuaName(getApplicationContext(), huaName);
                user.setHuaName(huaName);
            }

            @Override
            public void onFailure(int i, String s) {
            }
        });
    }

    /**
     * 根据从哪个界面进入，判断是否需要登录
     */
    private void isNeedLogin() {
        Intent i = getIntent();
        from = i.getStringExtra(FROM);
        if (!TextUtils.isEmpty(from) && from.equals(FlashActivity.FROM)) {
            //需要登陆一次获得最新的cookie

            final User user = BmobUser.getCurrentUser(getApplicationContext(), User.class);
            if (null == user) {
                jump2Login("");
                return;
            }

            String password = SharedPreferencesUtils.getUserPassword(getApplicationContext(), user.getSno());

            if (TextUtils.isEmpty(password)) {
                jump2Login(user.getSno());
                return;
            }
            LoginAsync loginAsync = new LoginAsync(getApplicationContext(), user.getSno(), password);
            loginAsync.execute();
            loginAsync.setCallback(new LoginAction.CallBack() {
                @Override
                public void onSuccess() {
//                    MyToast.show(getApplicationContext(), getString(R.string.login_success));
                    AppAnalytics.onEvent(getApplicationContext(), AppAnalytics.LOGIN_SUCCESS);
                }

                @Override
                public void onFail() {
                    AppAnalytics.onEvent(getApplicationContext(), AppAnalytics.LOGIN_FAIL);
                    MyToast.show(getApplicationContext(), getString(R.string.id_verify_fail));
                    jump2Login(user.getSno());
                }

                @Override
                public void onModify() {
                    AppAnalytics.onEvent(getApplicationContext(), AppAnalytics.LOGIN_MODIFY);
                    jump2Login(user.getSno());
                }
            });
        }


    }

    private void jump2Login(String str) {
        exitAccount(LoginAty.ACCOUNT, str);
        finish();
    }

    /**
     * 初始化菜单
     */
    private void init() {
        initSaleFrag();
        initWantFrag();
        initMyFrag();
        List<Fragment> lists = new ArrayList<>();
        lists.add(saleFrag);
        lists.add(wantFrag);
        lists.add(myFrag);
        pager.setAdapter(new TestAdapter(getSupportFragmentManager(), lists));
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

    private void initMyFrag() {
        myFrag = (MyFrag) fm.findFragmentByTag(MyFrag.MY);
        if (null == myFrag || saleFrag.isDetached()) {
            myFrag = MyFrag.newInstance(MyFrag.MY);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_scanner:
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                builder.setTitle("选择你想要发布的类目");
//                builder.setItems(new String[]{getString(R.string.secondbook), getString(R.string.drugstore)}, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case 0:
//
//                                Intent intent = new Intent(MainActivity.this, CaptureAty.class);
//                                startActivity(intent);
//                                break;
//                            case 1:
//                                Intent intent2 = new Intent(MainActivity.this, PublishDebrisActivity.class);
//                                startActivity(intent2);
//                                break;
//                        }
//                    }
//                });
//                builder.create().show();
//
//                break;
            case R.id.action_search:
                AppAnalytics.onEvent(getApplicationContext(), AppAnalytics.CLICK_SEARCH);
                Intent intent = new Intent(MainActivity.this, SearchAty.class);
                startActivity(intent);
                break;
            case R.id.setting:
                exitAccount();
                break;
        }
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setSelectedTextColor(position);
        if (2 == position) {
            if (null != fab) {
                fab.collapseImmediately();
                fab.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(from)) {
                if (null != myFrag) {
                    myFrag.setHeader();
                    myFrag.updateUserInfo();
                }
            }
        } else {
            if (null != fab) {
                fab.setVisibility(View.VISIBLE);
            }
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
        switch (v.getId()) {
            case R.id.fab1:
                AppAnalytics.onEvent(getApplicationContext(), AppAnalytics.C_PUBLISH_SB);
                fab.collapseImmediately();
                Intent intent = new Intent(MainActivity.this, CaptureAty.class);
                startActivity(intent);
                break;
            case R.id.fab2:
                AppAnalytics.onEvent(getApplicationContext(), AppAnalytics.C_PUBLISH_DB);
                fab.collapseImmediately();
                Intent intent2 = new Intent(MainActivity.this, PublishDebrisActivity.class);
                startActivity(intent2);
                break;
        }
    }


//
//    private class OkAdapter extends BaseRecycleViewAdapter<String> {
//
//        public OkAdapter(List<String> datas, Context context, int resource) {
//            super(datas, context, resource);
//        }
//
//
//
//        @Override
//        protected RecyclerView.ViewHolder getViewHolder(View view, int viewType) {
//            return new BaseViewHolder(view) {
//                @Override
//                protected void setContent(View itemView, String item, int viewType) {
//                    TextView tv = (TextView) itemView.findViewById(R.id.tv);
//                    tv.setText(item);
//                }
//            };
//        }
//    }

//    private class OkAdapter extends LoadMoreRVAdapter<String> {
//        public OkAdapter(List<String> datas, Context context, int resourceNormal, int resourceHeader, int resourceFooter) {
//            super(datas, context, resourceNormal, resourceHeader, resourceFooter);
//        }
//
//        @Override
//        protected RecyclerView.ViewHolder getViewHolder(View view, int viewType) {
//            return new BaseViewHolder(view, viewType) {
//                @Override
//                protected void setContent(View itemView, String item, int viewType) {
//                    if (viewType == LoadMoreRVAdapter.HEADER) {
////                        Button btnh = (Button) itemView.findViewById(R.id.btnh);
////                        btnh.setOnClickListener(new View.OnClickListener() {
////                            @Override
////                            public void onClick(View v) {
////                                Toast.makeText(getApplicationContext(), "click HEder", Toast.LENGTH_SHORT).show();
////
////                            }
////                        });
//
//                    } else if (viewType == LoadMoreRVAdapter.NORMAL) {
//                        TextView tv = (TextView) itemView.findViewById(R.id.tv);
//                        tv.setText(item);
//                    } else {
//                        Button btnf = (Button) itemView.findViewById(R.id.btnf);
//                        btnf.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Toast.makeText(getApplicationContext(), "click Footer", Toast.LENGTH_SHORT).show();
//
//                            }
//                        });
//                    }
//                }
//            };
//        }
//
//    }


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
