package com.xfzj.getbook.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xfzj.getbook.R;

/**
 * Created by zj on 2016/3/25.
 */
public class LiuShuiQueryFragment extends Fragment {


    public static final String PARAM = "LiuShuiQueryFragment.class";
    public static final String HISTORY = "history";
    public static final String TODAY = "today";
    private String param;

    public LiuShuiQueryFragment() {
        
    }
    

    public static LiuShuiQueryFragment newInstance(String param) {
        LiuShuiQueryFragment liuShuiQueryFragment = new LiuShuiQueryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM, param);
        liuShuiQueryFragment.setArguments(bundle);
        return liuShuiQueryFragment;
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

        View view = inflater.inflate(R.layout.footer, null);
        return view;
    }
}
