package com.xfzj.getbook.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by zj on 2016/1/29.
 */
public abstract class AppActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
   
        onSetContentView();
        ButterKnife.bind(this);
        onCreateView(savedInstanceState);

    }

    /**
     * 只用来做{@link #setContentView(View)}
     *
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
    }

    @Override
    protected void onPause() {
        super.onPause();
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
}
