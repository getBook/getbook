package com.xfzj.getbook.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zj on 2016/3/24.
 */
public class BuZhuFrag extends Fragment {
    public static final String PARAM = "BuZhuFrag.class";
    
    private String param;


    public BuZhuFrag() {
        
    }


    public static BuZhuFrag newInstance(String param) {
        BuZhuFrag buZhuFrag = new BuZhuFrag();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM, param);
        buZhuFrag.setArguments(bundle);
        return buZhuFrag;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            param = getArguments().getString(PARAM);
        }
        
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
