package com.xfzj.getbook;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xfzj.getbook.action.LoginAction;
import com.xfzj.getbook.action.QueryAction;
import com.xfzj.getbook.action.UploadAction;
import com.xfzj.getbook.activity.BaseActivity;
import com.xfzj.getbook.activity.BaseMySaleFrag;
import com.xfzj.getbook.activity.FlashActivity;
import com.xfzj.getbook.activity.LoginAty;
import com.xfzj.getbook.activity.SearchAty;
import com.xfzj.getbook.async.LoginAsync;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.fragment.CardFrag;
import com.xfzj.getbook.fragment.HomeFrag;
import com.xfzj.getbook.fragment.LibraryFrag;
import com.xfzj.getbook.fragment.NewsFrag;
import com.xfzj.getbook.fragment.PostFrag;
import com.xfzj.getbook.fragment.ScoreFrag;
import com.xfzj.getbook.utils.AppAnalytics;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.utils.MyUtils;
import com.xfzj.getbook.utils.PhotoClipTool;
import com.xfzj.getbook.utils.SharedPreferencesUtils;
import com.xfzj.getbook.views.view.BaseToolBar;
import com.xfzj.getbook.views.view.NavigationHeaderView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;

public class MainActivity extends BaseActivity implements NavigationHeaderView.OnHeaderClickListener, Toolbar.OnMenuItemClickListener, NavigationView.OnNavigationItemSelectedListener, NavigationHeaderView.OnHuaNameClick {
    public static final String FROM = "MainActivity.class";
    private static final int IMAGE_FROM_ALBUM = 456;

    protected static final int IMAGE_FROM_CAPTURE = 123;
    private static final int PHOTO_SIZE = 300;
    private static final int PHOTO_CLIP = 789;
    private static File IMAGE_PATH;
    private static File IMAGE_CLIP_PATH;
    @Bind(R.id.baseToolbar)
    BaseToolBar baseToolbar;

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
    private LibraryFrag libraryFrag;
    private CardFrag cardFrag;
    private ScoreFrag scoreFrag;
    public Menu menu;
    private PostFrag postFrag;

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
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
        BmobUpdateAgent.update(this);
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
        if (null != newsFrag) {
            FragmentManager cfm = newsFrag.getChildFragmentManager();
            if (null != cfm && cfm.getBackStackEntryCount() > 1) {
                cfm.popBackStack();
            } else {
                super.onBackPressed();
            }
        } else if (null != baseMySaleFrag && baseMySaleFrag.isVisible() && !baseMySaleFrag.isOriginState()) {
            baseMySaleFrag.setVisibilty(View.GONE);

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        currentClickItem = menuItem.getTitle();
        if (null != baseMySaleFrag && baseMySaleFrag.isVisible() && !baseMySaleFrag.isOriginState()) {
            baseMySaleFrag.setVisibilty(View.GONE);
        }
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
                initPostFrag();
//                startActivity(new Intent(this, PublishPostAty.class));
                
                break;
        }
        drawerLayout.closeDrawers();
        getColor(menuItem);


        return true;
    }

    private void initPostFrag() {
        postFrag = (PostFrag) fm.findFragmentByTag(PostFrag.PARAM);
        if (null == postFrag || postFrag.isDetached()) {
            postFrag = PostFrag.newInstance(PostFrag.PARAM);
        }
        if (!frags.contains(postFrag)) {
            frags.add(postFrag);
        }
        showFrag(postFrag, null);
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
        libraryFrag = (LibraryFrag) fm.findFragmentByTag(LibraryFrag.ARG_PARAM1);
        if (null == libraryFrag || libraryFrag.isDetached()) {
            libraryFrag = LibraryFrag.newInstance(LibraryFrag.ARG_PARAM1);
        }
        if (!frags.contains(libraryFrag)) {
            frags.add(libraryFrag);
        }
        showFrag(libraryFrag, LibraryFrag.ARG_PARAM1);
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

        if (null != user) {
            BmobQuery<User> query = new BmobQuery<>();
            query.addWhereEqualTo("sno", user.getSno());
            query.findObjects(getApplicationContext(), new FindListener<User>() {
                @Override
                public void onSuccess(List<User> list) {
                    if (null != list && list.size() != 0) {
                        serverUser = list.get(0);
                        if (TextUtils.isEmpty(serverUser.getHuaName())) {
                            showHuaNameDialog(false);
                        } else {
                            SharedPreferencesUtils.saveHuaName(getApplicationContext(), serverUser.getHuaName());
                            user.setHuaName(serverUser.getHuaName());

                        }
                    } else {
                        showHuaNameDialog(false);
                    }
                }

                @Override
                public void onError(int i, String s) {

                }
            });
        }
    }

    private void showHuaNameDialog(boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        final View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.huaname, null);
        final AlertDialog ad = builder.setTitle(getString(R.string.sethuaname)).setView(view).setCancelable(cancelable).create();
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

                    uploadHuaName(huaName.trim(), ad);
                }
            }
        });


    }

    private void uploadHuaName(final String huaName, final AlertDialog alertDialog) {
        QueryAction queryAction = new QueryAction(getApplicationContext(), new QueryAction.OnQueryListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                user.setHuaName(huaName);
                user.update(getApplicationContext(), serverUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        SharedPreferencesUtils.saveHuaName(getApplicationContext(), huaName);
                        user.setHuaName(huaName);
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onFailure(int i, String s) {


                    }
                });
            }

            @Override
            public void onFail() {
                MyToast.show(getApplicationContext(), "该昵称已经存在！");
            }
        });
        queryAction.queryHasHuaName(huaName);

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
            loginAsync.executeOnExecutor(THREAD_POOL_EXECUTOR);
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


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
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

        }
        return false;
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
                    QueryAction queryAction = new QueryAction(getApplicationContext(), new QueryAction.OnQueryListener<User>() {
                        @Override
                        public void onSuccess(User user) {
                            UploadAction uploadAction = new UploadAction();
                            uploadAction.uploadHeader(getApplicationContext(), user, IMAGE_CLIP_PATH);
                        }

                        @Override
                        public void onFail() {
                            MyToast.show(getApplicationContext(), "头像更换失败");
                        }
                    });
                    queryAction.queryUserSelf(user);
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
        if(null==navigationView.getItemTextColor()){
            return;
        }
        final int textColor = navigationView.getItemTextColor().getDefaultColor();
        Bitmap bitmap = ((BitmapDrawable) menuItem.getIcon()).getBitmap();
        Palette.Builder builder = new Palette.Builder(bitmap);
        builder.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                if(palette==null||null==palette.getVibrantSwatch()){
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
}
