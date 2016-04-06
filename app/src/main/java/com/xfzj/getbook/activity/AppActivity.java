package com.xfzj.getbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.xfzj.getbook.BaseApplication;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.utils.AppAnalytics;
import com.xfzj.getbook.utils.SharedPreferencesUtils;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

/**
 * Created by zj on 2016/1/29.
 */
public abstract class AppActivity extends AppCompatActivity {
    private static final int CPU_COUNT = Runtime.getRuntime()
            .availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final long KEEP_ALIVE = 10L;
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(5);

        public Thread newThread(Runnable r) {
            return new Thread(r, "ImageLoader#" + mCount.getAndIncrement());
        }
    };

    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
            KEEP_ALIVE, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(), sThreadFactory);

    public static Executor getThreadPoolExecutor() {
        return THREAD_POOL_EXECUTOR;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        onSetContentView();
        ButterKnife.bind(this);
        onCreateView(savedInstanceState);

    }

    /**
     * 只用来做{@link #setContentView(View)}
     */
    protected abstract void onSetContentView();

    /**
     * 不用重写{@link #onCreate(Bundle)}方法
     * 此方法作用相当于{@link #onCreate(Bundle)}方法
     */
    public abstract void onCreateView(Bundle savedInstanceState);

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();
        AppAnalytics.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppAnalytics.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void exitApp() {
        Intent i = new Intent(this, FlashActivity.class);
        i.putExtra(FlashActivity.FROM, FlashActivity.EXITAPP);
        startActivity(i);
        finish();
    }

    protected void exitAccount(String key, String value) {
        Intent i = new Intent(this, FlashActivity.class);
        i.putExtra(FlashActivity.FROM, FlashActivity.EXITACCOUNT);
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            i.putExtra(key, value);
        }
        startActivity(i);
        finish();
    }

    protected void exitAccount() {
        exitAccount(null, null);
        SharedPreferencesUtils.clearUser(getApplicationContext());
        BmobUser.logOut(getApplicationContext());
        ((BaseApplication) getApplication()).setUser(BmobUser.getCurrentUser(getApplicationContext(), User.class));
    }
}
