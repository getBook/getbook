package com.xfzj.getbook;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.alibaba.sdk.android.feedback.util.IWxCallback;
import com.umeng.socialize.UMShareAPI;
import com.xfzj.getbook.action.CommentAction;
import com.xfzj.getbook.action.LoginAction;
import com.xfzj.getbook.action.UploadAction;
import com.xfzj.getbook.activity.BaseActivity;
import com.xfzj.getbook.activity.BaseMySaleFrag;
import com.xfzj.getbook.activity.FlashActivity;
import com.xfzj.getbook.activity.LoginAty;
import com.xfzj.getbook.activity.SearchAty;
import com.xfzj.getbook.common.Post;
import com.xfzj.getbook.common.UnreadPost;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.fragment.BaseLibraryFrag;
import com.xfzj.getbook.fragment.CardFrag;
import com.xfzj.getbook.fragment.HomeFrag;
import com.xfzj.getbook.fragment.NewsFrag;
import com.xfzj.getbook.fragment.NotificationFrag;
import com.xfzj.getbook.fragment.PostDetailFrag;
import com.xfzj.getbook.fragment.PostFrag;
import com.xfzj.getbook.fragment.ScoreFrag;
import com.xfzj.getbook.newnet.ApiException;
import com.xfzj.getbook.newnet.BaseSubscriber;
import com.xfzj.getbook.newnet.GetFunApi;
import com.xfzj.getbook.newnet.NetRxWrap;
import com.xfzj.getbook.utils.AppAnalytics;
import com.xfzj.getbook.utils.MyLog;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.utils.MyUtils;
import com.xfzj.getbook.utils.PhotoClipTool;
import com.xfzj.getbook.utils.ShareUtils;
import com.xfzj.getbook.utils.SharedPreferencesUtils;
import com.xfzj.getbook.views.view.BaseToolBar;
import com.xfzj.getbook.views.view.NavigationHeaderView;
import com.xfzj.getbook.views.view.NotificationImaheView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;

import butterknife.Bind;
import cn.bmob.v3.update.BmobUpdateAgent;

public class MainActivity extends BaseActivity implements NavigationHeaderView.OnHeaderClickListener, Toolbar.OnMenuItemClickListener, NavigationView.OnNavigationItemSelectedListener, NavigationHeaderView.OnHuaNameClick, NotificationFrag.OnUnreadPostClick {
    public static final String FROM = "MainActivity.class";
    private static final int IMAGE_FROM_ALBUM = 456;

    protected static final int IMAGE_FROM_CAPTURE = 123;
    private static final int PHOTO_SIZE = 300;
    private static final int PHOTO_CLIP = 789;
    private static File IMAGE_PATH;
    private static File IMAGE_CLIP_PATH;
    @Bind(R.id.baseToolbar)
    BaseToolBar baseToolbar;
    @Bind(R.id.ll)
    LinearLayout ll;
    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private FragmentManager fm;
    private BaseApplication baseApplication;
    private User user, serverUser;
    private List<Fragment> frags = new ArrayList<>();

    /**
     * 从哪个activity进入
     */
    private String from;
    private HomeFrag homeFrag;
    private BaseMySaleFrag baseMySaleFrag;
    private NewsFrag newsFrag;
    private BaseLibraryFrag baseLibraryFrag;
    private CardFrag cardFrag;
    private ScoreFrag scoreFrag;
    private NotificationFrag notificationFrag;
    public Menu menu;
    private PostFrag postFrag;
    private Timer timer;
    /**
     * 是否已经显示了NotificationImaheView
     */
    private boolean isNoShow = false;
    /**
     * 上次和这次获取消息通知的时间
     */
    private long lastTime, nowTime;
    /**
     * 摇铃的imageview
     */
    private NotificationImaheView notificationImaheView;
    private Set<UnreadPost> unreadPosts = new HashSet<>();
    private boolean isUnreadPostOpen;
    private boolean isClickNo;
    /**
     * 已经获取过通知的post
     */
    private Set<Post> posteds;
    private List<Post> deletePost;

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }

    @Override
    protected void onSetContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        baseToolbar.initToolbar(this, getResources().getString(R.string.app_name));
        toolbar = baseToolbar.getToolbar();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        //使用图片本身的颜色，而不是灰色
        navigationView.setItemIconTintList(null);
        navigationHeaderView = new NavigationHeaderView(MainActivity.this);
        navigationView.addHeaderView(navigationHeaderView);
        navigationHeaderView.setOnLogoutClick(new NavigationHeaderView.OnLogoutClick() {
            @Override
            public void logout() {
                exitAccount();
            }
        });
        navigationHeaderView.setOnHeaderClickListener(this);
        navigationHeaderView.setOnHuaNameClick(this);
        navigationView.setNavigationItemSelectedListener(this);

        toolbar.setOnMenuItemClickListener(this);
        toolbar.setTitleTextColor(Color.WHITE);
        fm = getSupportFragmentManager();
        baseApplication = (BaseApplication) getApplication();
        user = baseApplication.getUser();
        setDrawerToggle();
        isNeedHuaName();
        isNeedLogin();
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
        //bmob自动更新
        try {
            BmobUpdateAgent.update(this);
//            BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {
//                @Override
//                public void onUpdateReturned(int updateStatus, UpdateResponse updateResponse) {
//                    // TODO Auto-generated method stub
//                    if (updateStatus == UpdateStatus.Yes) {//版本有更新
//
//                    }else if(updateStatus == UpdateStatus.No){
//                        Toast.makeText(MainActivity.this, "版本无更新", Toast.LENGTH_SHORT).show();
//                    }else if(updateStatus==UpdateStatus.EmptyField){//此提示只是提醒开发者关注那些必填项，测试成功后，无需对用户提示
//                        Toast.makeText(MainActivity.this, "请检查你AppVersion表的必填项，1、target_size（文件大小）是否填写；2、path或者android_url两者必填其中一项。", Toast.LENGTH_SHORT).show();
//                    }else if(updateStatus==UpdateStatus.IGNORED){
//                        Toast.makeText(MainActivity.this, "该版本已被忽略更新", Toast.LENGTH_SHORT).show();
//                    }else if(updateStatus==UpdateStatus.ErrorSizeFormat){
//                        Toast.makeText(MainActivity.this, "请检查target_size填写的格式，请使用file.length()方法获取apk大小。", Toast.LENGTH_SHORT).show();
//                    }else if(updateStatus==UpdateStatus.TimeOut){
//                        Toast.makeText(MainActivity.this, "查询出错或查询超时", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
        } catch (Exception e) {

        }
        getUnreadFeedBack();
        showShare();
    }

    /**
     * 显示分享对话框
     */
    private void showShare() {
        if (SharedPreferencesUtils.isShowShare(getApplicationContext())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.please).setIcon(R.mipmap.ic_launcher).
                    setMessage(getString(R.string.share_please)).
                    setPositiveButton(R.string.share, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ShareUtils.shareDefautl(MainActivity.this);
                        }
                    }).create().show();


        }


    }

    private void getUnreadFeedBack() {
        if (null == ll) {
            return;
        }
        FeedbackAPI.getFeedbackUnreadCount(getApplicationContext(), null, new IWxCallback() {
            @Override
            public void onSuccess(Object... objects) {
                if (null != objects[0]) {
                    Integer i = (Integer) objects[0];
                    if (i > 0) {
                        Snackbar snackbar = Snackbar.make(ll, getString(R.string.feedback_unread, i), Snackbar.LENGTH_LONG).setAction("查看", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openFeedBack();
                            }
                        });
                        snackbar.getView().setBackgroundColor(Color.WHITE);
                        snackbar.show();
                    }
                }
            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i) {

            }
        });

    }

    private void setDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.secondbook, R.string.debris) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {

                MyUtils.setStatusBarColor(getWindow(), Color.TRANSPARENT);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

        };
        mDrawerToggle.syncState();
        // Set the drawer toggle as the DrawerListener
        drawerLayout.addDrawerListener(mDrawerToggle);
    }

    @Override
    public void onBackPressed() {
        if (null != newsFrag && newsFrag.isVisible()) {
            FragmentManager cfm = newsFrag.getChildFragmentManager();
            if (null != cfm && cfm.getBackStackEntryCount() > 1) {
                cfm.popBackStack();
            } else {
                super.onBackPressed();
            }
        } else if (null != baseMySaleFrag && baseMySaleFrag.isVisible() && !baseMySaleFrag.isOriginState()) {
            baseMySaleFrag.setVisibilty(View.GONE);

        } else if (null != baseLibraryFrag && baseLibraryFrag.isVisible()) {
            FragmentManager cfm = baseLibraryFrag.getChildFragmentManager();
            if (null != cfm && cfm.getBackStackEntryCount() > 1) {
                cfm.popBackStack();
                setbaseLibraryFragVisibility();
            } else {
                super.onBackPressed();
            }
        } else if (null != postFrag && postFrag.isVisible()) {
            if (postFrag.isEmojiShow()) {
                postFrag.hideEmoji();
            } else {
                if (null != postFrag.getPostDetailFrag()) {
                    PostDetailFrag pdf = postFrag.getPostDetailFrag();
                    if (null != pdf && pdf.isVisible() && isUnreadPostOpen) {

                        FragmentManager cfm = postFrag.getChildFragmentManager();
                        cfm.getBackStackEntryCount();
                        if (null != cfm) {
                            cfm.popBackStack();
                        }
                        initNotificationFrag(unreadPosts);
                        isUnreadPostOpen = false;
                    } else if (null != pdf && pdf.isVisible()) {
                        FragmentManager cfm = postFrag.getChildFragmentManager();
                        if (null != cfm && cfm.getBackStackEntryCount() >= 1) {
                            cfm.popBackStack();
                            postFrag.initPostShowFrag();
                        }
                    } else {
                        FragmentManager cfm = postFrag.getChildFragmentManager();
                        if (null != cfm && cfm.getBackStackEntryCount() > 1) {
                            cfm.popBackStack();
                        } else if (null != cfm && cfm.getBackStackEntryCount() == 0) {
                            postFrag.initPostShowFrag();
                        } else {
                            super.onBackPressed();
                        }
                    }

                } else {
                    FragmentManager cfm = postFrag.getChildFragmentManager();
                    if (null != cfm && cfm.getBackStackEntryCount() > 1) {
                        cfm.popBackStack();
                    } else {
                        super.onBackPressed();
                    }
                }
            }
        } else if (null != notificationFrag && notificationFrag.isVisible()) {
            if (null != fm) {
                fm.popBackStack();

            }
            if (null != selectedItem) {
                onNavigationItemSelected(selectedItem);
            }
        } else {
            super.onBackPressed();
        }
    }

    private void setbaseLibraryFragVisibility() {
        if (null != baseLibraryFrag && baseLibraryFrag.isVisible() && !baseLibraryFrag.isOriginState()) {
            baseLibraryFrag.setVisibilty(View.GONE);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        currentClickItem = menuItem.getTitle();
        selectedItem = menuItem;
        if (null != baseMySaleFrag && baseMySaleFrag.isVisible() && !baseMySaleFrag.isOriginState()) {
            baseMySaleFrag.setVisibilty(View.GONE);
        }
//        setbaseLibraryFragVisibility();
        switch (menuItem.getItemId()) {
            case R.id.menum_home:
                toolbar.setTitle(R.string.app_name);
                initHomeFrag();
                break;
            case R.id.menum_secondBook:
                AppAnalytics.onEvent(this, AppAnalytics.C_M_SB);
                toolbar.setTitle(R.string.secondbook);
                initBaseMySaleFrag(BaseMySaleFrag.SECONDBOOK);
                break;
            case R.id.menum_debris:
                AppAnalytics.onEvent(this, AppAnalytics.C_M_DB);
                toolbar.setTitle(R.string.debris);
                initBaseMySaleFrag(BaseMySaleFrag.DEBRIS);
                break;
            case R.id.menum_schoolAnnounce:
                AppAnalytics.onEvent(this, AppAnalytics.C_M_SA);
                toolbar.setTitle(R.string.school_announce);
                initNewsFrag();
                break;
            case R.id.menum_library:
                AppAnalytics.onEvent(this, AppAnalytics.C_M_L);
                toolbar.setTitle(R.string.library);
                initLibraryFrag();
                break;
            case R.id.menum_card:
                AppAnalytics.onEvent(this, AppAnalytics.C_M_C);
                toolbar.setTitle(R.string.yikatong);
                initCardFrag();
                break;
            case R.id.menum_score:
                AppAnalytics.onEvent(this, AppAnalytics.C_M_S);
                toolbar.setTitle(R.string.grades);
                initScoreFrag();
                break;
            case R.id.menum_post:
                AppAnalytics.onEvent(this, AppAnalytics.C_T);
                toolbar.setTitle(R.string.tree);
                initPostFrag(null, null);
                break;
        }
        drawerLayout.closeDrawers();
        getColor(menuItem);
        return true;
    }

    private void initPostFrag(String param, Post post) {
        postFrag = (PostFrag) fm.findFragmentByTag(PostFrag.ARG_PARAM1);
        if (null == postFrag || postFrag.isDetached()) {
            postFrag = PostFrag.newInstance(param);
        }
        postFrag.isInit(TextUtils.isEmpty(param));
        postFrag.setPost(post);
        if (!frags.contains(postFrag)) {
            frags.add(postFrag);
        }
        showFrag(postFrag, PostFrag.ARG_PARAM1);
    }

    private void initNotificationFrag(Set<UnreadPost> unreadPosts) {
        notificationFrag = (NotificationFrag) fm.findFragmentByTag(NotificationFrag.PARAM);
        if (null == notificationFrag || notificationFrag.isDetached()) {
            notificationFrag = NotificationFrag.newInstance(NotificationFrag.PARAM);
        }
        notificationFrag.setUnreadPosts(unreadPosts);
        notificationFrag.setOnUnreadPostClick(this);
        if (!frags.contains(notificationFrag)) {
            frags.add(notificationFrag);
        }
        showFrag(notificationFrag, NotificationFrag.PARAM, true);
    }

    private void initScoreFrag() {
        scoreFrag = (ScoreFrag) fm.findFragmentByTag(ScoreFrag.ARG_PARAM1);
        if (null == scoreFrag || scoreFrag.isDetached()) {
            scoreFrag = ScoreFrag.newInstance(ScoreFrag.ARG_PARAM1);
        }
        if (!frags.contains(scoreFrag)) {
            frags.add(scoreFrag);
        }
        showFrag(scoreFrag, ScoreFrag.ARG_PARAM1);
    }

    private void initCardFrag() {
        cardFrag = (CardFrag) fm.findFragmentByTag(CardFrag.ARG_PARAM1);
        if (null == cardFrag || cardFrag.isDetached()) {
            cardFrag = CardFrag.newInstance(CardFrag.ARG_PARAM1);
        }
        if (!frags.contains(cardFrag)) {
            frags.add(cardFrag);
        }
        showFrag(cardFrag, CardFrag.ARG_PARAM1);
    }

    private void initLibraryFrag() {
        baseLibraryFrag = (BaseLibraryFrag) fm.findFragmentByTag(BaseLibraryFrag.ARG_PARAM1);
        if (null == baseLibraryFrag || baseLibraryFrag.isDetached()) {
            baseLibraryFrag = BaseLibraryFrag.newInstance(BaseLibraryFrag.ARG_PARAM1);
            baseLibraryFrag.initToolbar(baseToolbar);
        }
        if (!frags.contains(baseLibraryFrag)) {
            frags.add(baseLibraryFrag);
        }

        showFrag(baseLibraryFrag, BaseLibraryFrag.ARG_PARAM1);
    }

    private void initHomeFrag() {
        homeFrag = (HomeFrag) fm.findFragmentByTag(HomeFrag.ARG_PARAM1);
        if (null == homeFrag || homeFrag.isDetached()) {
            homeFrag = HomeFrag.newInstance(HomeFrag.ARG_PARAM1);
        }
        if (!frags.contains(homeFrag)) {
            frags.add(homeFrag);
        }
        showFrag(homeFrag, HomeFrag.ARG_PARAM1);

    }

    private void initBaseMySaleFrag(int param) {

        baseMySaleFrag = (BaseMySaleFrag) fm.findFragmentByTag(BaseMySaleFrag.ARG_PARAM1);
        if (null == baseMySaleFrag || baseMySaleFrag.isDetached()) {
            baseMySaleFrag = BaseMySaleFrag.newInstance(param);
        }
        if (!frags.contains(baseMySaleFrag)) {
            frags.add(baseMySaleFrag);
        }
        baseMySaleFrag.initToolbar(baseToolbar);
        showFrag(baseMySaleFrag, null);

    }

    private void initNewsFrag() {
        newsFrag = (NewsFrag) fm.findFragmentByTag(NewsFrag.ARG_PARAM1);
        if (null == newsFrag || newsFrag.isDetached()) {
            newsFrag = NewsFrag.newInstance(NewsFrag.ARG_PARAM1);
        }
        if (!frags.contains(newsFrag)) {
            frags.add(newsFrag);
        }
        showFrag(newsFrag, NewsFrag.ARG_PARAM1);

    }

    private void showFrag(Fragment frag, String tag) {
        showFrag(frag, tag, false);
    }

    private void showFrag(Fragment frag, String tag, boolean b) {
        FragmentTransaction ft = fm.beginTransaction();
        if (!frag.isAdded()) {
            ft.add(R.id.fram, frag, tag);
        }
        for (Fragment f : frags) {
            if (f == frag) {
                ft.show(frag);
            } else {
                ft.hide(f);
            }
        }
        if (b) {
            ft.addToBackStack(null);
        }
        ft.commit();
        fm.executePendingTransactions();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && null != drawerLayout) {
            if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                drawerLayout.closeDrawers();
            } else {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
            return true;
        }
        return false;
    }


    private void isNeedHuaName() {
        if (null != user && TextUtils.isEmpty(user.getHuaName())) {
            showHuaNameDialog(false);
        }
    }

    private void showHuaNameDialog(boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.huaname, null);
        final AlertDialog ad = builder.setTitle(getString(R.string.sethuaname)).setView(view).setCancelable(cancelable).create();
        ad.show();
        final EditText edtHuaName = (EditText) view.findViewById(R.id.edtHuaName);
        TextView btnEnsure = (TextView) view.findViewById(R.id.btnEnsure);
        btnEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String huaName = edtHuaName.getText().toString();
                if (TextUtils.isEmpty(huaName)) {
                    MyToast.show(getApplicationContext(), getString(R.string.please_to_input, getString(R.string.nicheng)));
                } else {

                    uploadHuaName(huaName.trim(), ad);
                }
            }
        });


    }

    private void uploadHuaName(final String huaName, final AlertDialog alertDialog) {
        NetRxWrap.wrap(GetFunApi.updateHuaName(huaName)).subscribe(new BaseSubscriber<String>() {
            @Override
            protected void onError(ApiException ex) {
                MyToast.show(getApplicationContext(), getString(R.string.update_fail));
            }

            @Override
            protected void onPermissionError(ApiException ex) {
                MyToast.show(getApplicationContext(), getString(R.string.unlogin));
            }

            @Override
            protected void onResultError(ApiException ex) {
                MyToast.show(getApplicationContext(), ex.getDisplayMessage());
            }

            @Override
            public void onNextResult(String aVoid) {
                MyToast.show(getApplicationContext(), getString(R.string.update_success));
                user.setHuaName(huaName);
                SharedPreferencesUtils.saveHuaName(getApplicationContext(), huaName);
                baseApplication.setUser(user);
                alertDialog.dismiss();
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

            final User user = SharedPreferencesUtils.getUser(getApplicationContext());
            if (null == user) {
                jump2Login("");
                return;
            }

            String password = SharedPreferencesUtils.getUserPassword(getApplicationContext(), user.getSno());

            if (TextUtils.isEmpty(password)) {
                jump2Login(user.getSno());
                return;
            }
            LoginAction.login(false, MainActivity.this, user.getSno(), password, new LoginAction.CallBack() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onFail(ApiException ex) {
                    MyToast.show(getApplicationContext(), getString(R.string.id_verify_fail) + ex.getDisplayMessage());
                    jump2Login(user.getSno());
                }
            });
        }
    }

    private void jump2Login(String str) {
        exitAccount(LoginAty.ACCOUNT, str);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        UnreadPostTak();
        return super.onCreateOptionsMenu(menu);
    }

    private void UnreadPostTak() {
        if (isNoShow && !isClickNo) {
            if (null != notificationImaheView) {
                notificationImaheView.clearAnimation();
            }
            menu.findItem(R.id.action_notification).setActionView(null);
            menu.findItem(R.id.action_notification).setActionView(notificationImaheView);
        } else {
            isNoShow = false;
            isClickNo = false;
            if (null != notificationImaheView) {
                notificationImaheView.clearAnimation();
            }
            menu.findItem(R.id.action_notification).setActionView(null);
        }
        nowTime = System.currentTimeMillis();
        if (nowTime - lastTime >= Constants.NOTIFICATION_GET_TIME || lastTime == 0) {
            isNoShow = false;
            getUnreadPost(menu.findItem(R.id.action_notification));
            lastTime = nowTime;
        }
    }


    private void getUnreadPost(final MenuItem item) {
        if (null == user || null == item) {
            return;
        }
        final List<Post> posts = SharedPreferencesUtils.getFocusPostId(getApplicationContext());
        if (null == posts) {
            return;
        }
        if (null == posteds) {
            posteds = new HashSet<>();
        }
        Iterator<Post> iterable = posteds.iterator();
        deletePost = new ArrayList<>();
        while (iterable.hasNext()) {
            Post post = iterable.next();
            for (Post p : posts) {
                if (p.equals(post)) {
                    deletePost.add(p);
                }
            }
        }
        if (null != deletePost) {
            for (Post post : deletePost) {
                posts.remove(post);
            }
        }
        for (final Post post : posts) {
            CommentAction focusAction = new CommentAction(getApplicationContext());
            focusAction.queryCount(post, new CommentAction.OnCountListener() {
                @Override
                public void onCommentCount(final int i) {
                    int a = SharedPreferencesUtils.isFocusPostUpdate(getApplicationContext(), post.getObjectId(), i);
                    if (a > 0) {
                        post.setCommentCount(i);
                        unreadPosts.add(new UnreadPost(post, a));
                        if (null == notificationImaheView) {
                            notificationImaheView = (NotificationImaheView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.notificationimageview_layout, null);
                            notificationImaheView.clearAnimation();
                            notificationImaheView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    posteds.addAll(posts);
                                    isClickNo = true;
                                    notificationImaheView.clearAnimation();
                                    item.setActionView(null);
                                    initNotificationFrag(unreadPosts);
                                }
                            });
                        }
                        if (!isNoShow) {
                            isNoShow = true;
                            if (null != notificationImaheView) {
                                notificationImaheView.clearAnimation();
                            }
                            item.setActionView(null);
                            item.setActionView(notificationImaheView);
                        }
                    } else {
                        MyLog.print("暂无新消息", "");
                    }
                }
            });
        }
    }

    private boolean b;


    @Override
    public boolean onMenuItemClick(final MenuItem item) {
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
            case R.id.about:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.aboutconten).setTitle(R.string.about).setIcon(R.mipmap.ic_launcher).create().show();
                break;
            case R.id.update:
                try {
                    BmobUpdateAgent.forceUpdate(MainActivity.this);
                } catch (Exception e) {

                }
                break;
            case R.id.feedback:
                openFeedBack();
                break;
            case R.id.action_notification:
////                getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
//                invalidateOptionsMenu();
                initNotificationFrag(unreadPosts);
                break;
            case R.id.action_share:
                if (null != newsFrag && newsFrag.isVisible()) {
                    if (null != newsFrag.getNewsDetailFrag() && newsFrag.getNewsDetailFrag().isVisible()) {
                        newsFrag.getNewsDetailFrag().shareNews();
                        break;
                    }
                }
                ShareUtils.shareDefautl(this);
                break;
        }
        return false;
    }

    private void openFeedBack() {
        Map<String, String> map = new HashMap<>();
        map.put("avatar", SharedPreferencesUtils.getUserHeader(getApplicationContext()));
        FeedbackAPI.setUICustomInfo(map);
        FeedbackAPI.openFeedbackActivity(getApplicationContext());
    }

    @Override
    public void onCapture() {
        IMAGE_PATH = MyUtils.getDiskCacheDir(getApplicationContext(), System.currentTimeMillis() + user.getSno() + getString(R.string.jpg));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(IMAGE_PATH));
        startActivityForResult(intent, IMAGE_FROM_CAPTURE);


    }

    @Override
    public void onSelect() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IMAGE_FROM_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {
                    IMAGE_CLIP_PATH = MyUtils.getDiskCacheDir(getApplicationContext(), System.currentTimeMillis() + user.getSno() + ".jpg");
                    if (data != null) {
                        if (data.hasExtra("data")) {
                            Bitmap bitmap = data.getParcelableExtra("data");
                            Intent intent = PhotoClipTool.ClipFromBitmap(bitmap,
                                    PHOTO_SIZE, IMAGE_CLIP_PATH);
                            startActivityForResult(intent, PHOTO_CLIP);
                        }
                    } else {
                        Uri uri = Uri.fromFile(IMAGE_PATH);
                        Intent intent;

                        intent = PhotoClipTool.ClipFromUri(uri,
                                PHOTO_SIZE, IMAGE_CLIP_PATH);
                        startActivityForResult(intent, PHOTO_CLIP);
                    }
                }
                break;
            case IMAGE_FROM_ALBUM:
                if (resultCode == Activity.RESULT_OK) {
                    IMAGE_CLIP_PATH = MyUtils.getDiskCacheDir(getApplicationContext(), System.currentTimeMillis() + user.getSno() + ".jpg");
                    Uri ImgUri = data.getData();
                    if (ImgUri != null) {
                        Intent intent = PhotoClipTool.ClipFromUri(ImgUri,
                                PHOTO_SIZE, IMAGE_CLIP_PATH);
                        startActivityForResult(intent, PHOTO_CLIP);
                    } else {
                        Bundle bundle = data.getExtras();
                        if (bundle != null) {
                            Bitmap bitmap = bundle.getParcelable("data");
                            Intent intent = PhotoClipTool.ClipFromBitmap(bitmap,
                                    PHOTO_SIZE, IMAGE_CLIP_PATH);
                            startActivityForResult(intent, PHOTO_CLIP);
                        }
                    }
                }
                break;
            case PHOTO_CLIP:
                if (resultCode == Activity.RESULT_OK) {
                    UploadAction.uploadHeader(MainActivity.this,IMAGE_CLIP_PATH);
                    break;
                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        navigationHeaderView.unregisterReceive();
    }

    /**
     * 获取被点击item的图标的颜色
     *
     * @param menuItem
     */
    protected void getColor(MenuItem menuItem) {
        final int backGroundColor = ((ColorDrawable) navigationView.getBackground()).getColor();
        if (null == navigationView.getItemTextColor()) {
            return;
        }
        final int textColor = navigationView.getItemTextColor().getDefaultColor();
        Bitmap bitmap = ((BitmapDrawable) menuItem.getIcon()).getBitmap();
        Palette.Builder builder = new Palette.Builder(bitmap);
        builder.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                if (palette == null || null == palette.getVibrantSwatch()) {
                    return;
                }
                int checkedTextColor = palette.getVibrantSwatch().getRgb();
                int checkedBackgroundColor = MyUtils.getBrighterColor(checkedTextColor);
                if (checkedTextColor != 0 && checkedBackgroundColor != 0) {
                    int c = navigationView.getChildCount();
                    for (int i = 0; i < c; i++) {
                        ViewGroup viewGroup = (ViewGroup) navigationView.getChildAt(i);
                        for (int j = 0; j < viewGroup.getChildCount(); j++) {
                            View view = viewGroup.getChildAt(j);
                            if (view instanceof TextView) {
                                if (((TextView) view).getText().toString().equals(currentClickItem)) {
                                    ((TextView) view).setTextColor(checkedTextColor);
                                    view.setBackgroundColor(checkedBackgroundColor);
                                } else {
                                    view.setBackgroundColor(backGroundColor);
                                    ((TextView) view).setTextColor(textColor);
                                }
                            }
                        }
                    }
                }

            }
        });

    }

    @Override
    public void changeHuaName() {
        showHuaNameDialog(true);
    }

    @Override
    public void onUnreadPostClick(Post post) {
        isUnreadPostOpen = true;
        initPostFrag(PostFrag.POSTDETAIL, post);
    }
}
