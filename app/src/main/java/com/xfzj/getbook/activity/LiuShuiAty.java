package com.xfzj.getbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.xfzj.getbook.R;
import com.xfzj.getbook.fragment.BuZhuFrag;
import com.xfzj.getbook.fragment.LiuShuiFrag;
import com.xfzj.getbook.views.view.BaseToolBar;

import butterknife.Bind;

/**
 * Created by zj on 2016/3/24.
 */
public class LiuShuiAty extends AppActivity {
    public static final String FROM = "from";
    public static final String FROMLIUSHUI = "流水查询";
    public static final String FROMBUZHU = "补助流水";
    @Bind(R.id.baseToolbar)
    BaseToolBar baseToolBar;
    Toolbar toolbar;
    private LiuShuiFrag liuShuiFrag;
    private BuZhuFrag buZhuFrag;
    private FragmentManager fm;

    @Override
    protected void onSetContentView() {
        setContentView(R.layout.atyliushui);

    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        fm = getSupportFragmentManager();
        Intent intent = getIntent();
        String title = intent.getStringExtra(LiuShuiAty.FROM);
        setToolBarTitle(title);
        if (FROMLIUSHUI.equals(title)) {

            initLiuShuiFrag();
        } else if (FROMBUZHU.equals(title)) {

            initBuZhuFrag();
        } else {
            finish();
        }

    }

    private void initBuZhuFrag() {
        buZhuFrag = (BuZhuFrag) fm.findFragmentByTag(BuZhuFrag.PARAM);
        if (null == buZhuFrag || buZhuFrag.isDetached()) {
            buZhuFrag = BuZhuFrag.newInstance(BuZhuFrag.PARAM);
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fram, buZhuFrag).commit();
        fm.executePendingTransactions();

    }

    private void initLiuShuiFrag() {
        liuShuiFrag = (LiuShuiFrag) fm.findFragmentByTag(LiuShuiFrag.PARAM);
        if (null == liuShuiFrag || liuShuiFrag.isDetached()) {
            liuShuiFrag = LiuShuiFrag.newINstance(LiuShuiFrag.PARAM);
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fram, liuShuiFrag).commit();
        fm.executePendingTransactions();
    }

    private void setToolBarTitle(String title) {
        baseToolBar.initToolbar(this, title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
