package com.xfzj.getbook.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.async.GetNewsListLoader;
import com.xfzj.getbook.common.News;
import com.xfzj.getbook.fragment.DownloadFrag;
import com.xfzj.getbook.fragment.NewsDetailFrag;
import com.xfzj.getbook.fragment.NewsShowFrag;
import com.xfzj.getbook.views.view.BaseToolBar;

import butterknife.Bind;

/**
 * Created by zj on 2016/3/16.
 */
public class NewsAty extends AppActivity implements NewsShowFrag.OnNewsClick, View.OnClickListener {
    @Bind(R.id.baseToolbar)
    BaseToolBar baseToolBar;
    private NewsShowFrag newsShowFrag;
    private FragmentManager fm;
    private GetNewsListLoader getNewsListLoader;
    private NewsDetailFrag newsDetailFrag;
    private DownloadFrag downloadFrag;
    @Bind(R.id.fram1)
    FrameLayout fram1;

    @Bind(R.id.fram2)
    FrameLayout fram2;
    private ImageView iv;

    @Override
    protected void onSetContentView() {

        setContentView(R.layout.aty_newaty);

    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        baseToolBar.initToolbar(this, "教务公告");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fm = getSupportFragmentManager();
        iv = baseToolBar.getIv();
        iv.setVisibility(View.VISIBLE);
        iv.setImageResource(R.mipmap.download);
        iv.setOnClickListener(this);
            initNewsShowFrag();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initNewsShowFrag() {
        newsShowFrag = (NewsShowFrag) fm.findFragmentByTag(NewsShowFrag.PARAM);
        if (null == newsShowFrag || newsShowFrag.isDetached()) {
            newsShowFrag = NewsShowFrag.newInstance(NewsShowFrag.PARAM);
        }
        newsShowFrag.setOnNewsClick(this);
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fram1, newsShowFrag);
        ft.commit();
        fm.executePendingTransactions();
        fram1.setVisibility(View.VISIBLE);
        fram2.setVisibility(View.GONE);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(News news) {
        initNewsDetailFrag(news.getHref());

    }

    private void initNewsDetailFrag(String param) {
        newsDetailFrag = (NewsDetailFrag) fm.findFragmentByTag(param);
        if (null == newsDetailFrag || newsDetailFrag.isDetached()) {
            newsDetailFrag = NewsDetailFrag.newInstance(param);
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fram2, newsDetailFrag).commit();
        fm.executePendingTransactions();
        fram1.setVisibility(View.GONE);
        fram2.setVisibility(View.VISIBLE);
    }

    private void initDownloadFrag() {
        downloadFrag = (DownloadFrag) fm.findFragmentByTag(DownloadFrag.PARAM);
        if (null == downloadFrag || downloadFrag.isDetached()) {
            downloadFrag = DownloadFrag.newInstance(DownloadFrag.PARAM);
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fram2, downloadFrag).commit();
        fm.executePendingTransactions();
        fram1.setVisibility(View.GONE);
        fram2.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            handleBack();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        handleBack();
    }

    private void handleBack() {
        if (null != newsDetailFrag && newsDetailFrag.isAdded()) {
            fm.beginTransaction().remove(newsDetailFrag).commit();
            fm.executePendingTransactions();
            fram1.setVisibility(View.VISIBLE);
            fram2.setVisibility(View.GONE);
        } else if (null != downloadFrag && downloadFrag.isAdded()) {
            iv.setVisibility(View.VISIBLE);
            fm.beginTransaction().remove(downloadFrag).commit();
            fm.executePendingTransactions();
            fram1.setVisibility(View.VISIBLE);
            fram2.setVisibility(View.GONE);
        } else {
            finish();
        }
    }
    @Override
    public void onClick(View v) {
        initDownloadFrag();
        iv.setVisibility(View.GONE);
    }
}
