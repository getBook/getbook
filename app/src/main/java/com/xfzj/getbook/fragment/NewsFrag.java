package com.xfzj.getbook.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.xfzj.getbook.R;
import com.xfzj.getbook.common.News;
import com.xfzj.getbook.utils.AppAnalytics;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zj on 2016/3/16.
 */
public class NewsFrag extends BaseFragment implements NewsShowFrag.OnNewsClick, View.OnClickListener {

    private NewsShowFrag newsShowFrag;
    private FragmentManager fm;
    private NewsDetailFrag newsDetailFrag;
    private DownloadFrag downloadFrag;
    private FloatingActionButton fab;
    public static final String ARG_PARAM1 = "NewsFrag.class";

    private ImageView iv;
    private String mParam1;
    private List<Fragment> frags = new ArrayList<>();

    public static NewsFrag newInstance(String param1) {
        NewsFrag fragment = new NewsFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public NewsFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        fm = getChildFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.aty_newaty, container, false);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        if (null == savedInstanceState) {
            initNewsShowFrag();
        }

        return view;
    }

    public NewsDetailFrag getNewsDetailFrag() {
        return newsDetailFrag;
    }

    private void initNewsShowFrag() {
        
        newsShowFrag = (NewsShowFrag) fm.findFragmentByTag(NewsShowFrag.PARAM);
        if (null == newsShowFrag || newsShowFrag.isDetached()) {
            newsShowFrag = NewsShowFrag.newInstance(NewsShowFrag.PARAM);
        }
        fab.setVisibility(View.VISIBLE);
        newsShowFrag.setOnNewsClick(this);
        newsShowFrag.setFab(fab);
        showFrag(newsShowFrag, NewsShowFrag.PARAM);

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    private void showFrag(Fragment frag, String tag) {
        FragmentTransaction ft = fm.beginTransaction();

        if (!frag.isAdded()) {
            frags.add(frag);
            ft.add(R.id.fram1, frag, tag);
        }
        for (Fragment f : frags) {
            if (f == frag) {
                ft.show(frag);
            } else {
                ft.hide(f);
            }
        }
        ft.addToBackStack(null);
        ft.commit();
        fm.executePendingTransactions();

    }


    @Override
    public void onClick(News news) {
        AppAnalytics.onEvent(getActivity(), AppAnalytics.C_SA_DET);
        initNewsDetailFrag(news.getHref(), news.getTitle());
    }

    private void initNewsDetailFrag(String param,String title) {
        newsDetailFrag = (NewsDetailFrag) fm.findFragmentByTag(NewsDetailFrag.PARAM);
        if (null == newsDetailFrag || newsDetailFrag.isDetached()) {
            newsDetailFrag = NewsDetailFrag.newInstance(NewsDetailFrag.PARAM);
        }
        fab.setVisibility(View.VISIBLE);
        newsDetailFrag.setFab(fab);
        showFrag(newsDetailFrag, NewsDetailFrag.PARAM);
        newsDetailFrag.setHref(param);
        newsDetailFrag.setTitle(title);
        
    }

    private void initDownloadFrag() {
        downloadFrag = (DownloadFrag) fm.findFragmentByTag(DownloadFrag.PARAM);
        if (null == downloadFrag || downloadFrag.isDetached()) {
            downloadFrag = DownloadFrag.newInstance(DownloadFrag.PARAM);
        }
        fab.setVisibility(View.GONE);
        
        showFrag(downloadFrag, null);

    }

    @Override
    public void onClick(View v) {
        initDownloadFrag();
    }
    
   
}
