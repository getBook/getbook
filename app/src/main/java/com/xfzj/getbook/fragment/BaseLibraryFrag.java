package com.xfzj.getbook.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xfzj.getbook.R;
import com.xfzj.getbook.views.view.BaseToolBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zj on 2016/3/16.
 */
public class BaseLibraryFrag extends BaseFragment implements LibraryFrag.OnFabClickLinstener {

    private LibraryFrag libraryFrag;
    private FragmentManager fm;
    private AsordFrag asordFrag;
    private RecommendFrag recommendFrag;
    public static final String ARG_PARAM1 = "BaseLibraryFrag.class";

    private String mParam1;
    private List<Fragment> frags = new ArrayList<>();
    private BaseToolBar toolbar;

    public static BaseLibraryFrag newInstance(String param1) {
        BaseLibraryFrag fragment = new BaseLibraryFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public BaseLibraryFrag() {
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
        View view = inflater.inflate(R.layout.aty_baselibrary, container, false);
//        fab = (FloatingActionButton) view.findViewById(R.id.fab);
//        fab.setOnClickListener(this);
        if (null == savedInstanceState) {
            initLibraryFrag();
        }

        return view;
    }

    private void initLibraryFrag() {

        libraryFrag = (LibraryFrag) fm.findFragmentByTag(LibraryFrag.ARG_PARAM1);
        if (null == libraryFrag || libraryFrag.isDetached()) {
            libraryFrag = LibraryFrag.newInstance(LibraryFrag.ARG_PARAM1);
        }
//        fab.setVisibility(View.VISIBLE);
//        libraryFrag.setOnNewsClick(this);
//        libraryFrag.setFab(fab);
        libraryFrag.setOnFabClickLinstener(this);
        showFrag(libraryFrag, LibraryFrag.ARG_PARAM1);

    }

   
    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (null != libraryFrag) {
            libraryFrag.onHiddenChanged(hidden);
        }
        if (null != recommendFrag) {
            if(hidden||!recommendFrag.isVisible()) {
                recommendFrag.setVisibilty(View.GONE);
            }else {
                recommendFrag.setVisibilty(View.VISIBLE);
            }
        }
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


    private void initAsordFrag() {
        asordFrag = (AsordFrag) fm.findFragmentByTag(AsordFrag.ARG_PARAM1);
        if (null == asordFrag || asordFrag.isDetached()) {
            asordFrag = AsordFrag.newInstance(AsordFrag.ARG_PARAM1);
        }
//        fab.setVisibility(View.GONE);

        showFrag(asordFrag, null);

    }


    @Override
    public void onClickRecommendHistory() {
        initAsordFrag();
    }

    @Override
    public void onClickRecommend() {
        initRecommendFrag();
    }

    private void initRecommendFrag() {
        recommendFrag = (RecommendFrag) fm.findFragmentByTag(RecommendFrag.ARG_PARAM1);
        if (null == recommendFrag || recommendFrag.isDetached()) {
            recommendFrag = RecommendFrag.newInstance(RecommendFrag.ARG_PARAM1);
        }
        recommendFrag.initToolbar(toolbar);
        showFrag(recommendFrag, RecommendFrag.ARG_PARAM1);


    }


    public void initToolbar(BaseToolBar baseToolbar) {
        this.toolbar = baseToolbar;
    }

    public boolean isOriginState() {
        if (null == recommendFrag) {
            return true;
        }
        return recommendFrag.isOriginState();
    }

    public void setVisibilty(int i) {
        if (null == recommendFrag) {
            return;
        }
        recommendFrag.setVisibilty(i);
    }
    public  boolean isRecommendFragShowing() {
        if (null == recommendFrag) {
            return false;
        }
        return recommendFrag.isVisible();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RecommendFrag.SCAN && resultCode == RecommendFrag.SCAN && null != data && null != recommendFrag) {
            recommendFrag.setIsbn(data.getStringExtra(RecommendFrag.ISBN));
        }
    }
}
