package com.xfzj.getbook.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xfzj.getbook.R;
import com.xfzj.getbook.activity.AppActivity;
import com.xfzj.getbook.async.ScoreQueryAsync;
import com.xfzj.getbook.async.UcardAsyncTask;
import com.xfzj.getbook.common.Score;
import com.xfzj.getbook.utils.AppAnalytics;
import com.xfzj.getbook.views.view.WrapWrapScoreItemView;

import java.util.List;

/**
 * Created by zj on 2016/3/14.
 */
public class ScoreFrag extends BaseFragment implements View.OnClickListener,UcardAsyncTask.OnUcardTaskListener<List<List<Score>>> {
    public static final String ARG_PARAM1 = "ScoreFrag.class";
    
    LinearLayout ll;
    LinearLayout llError;
    Button btn;

    private ScoreQueryAsync scoreQueryAsync;
    private String mParam1;


    public static ScoreFrag newInstance(String param1) {
        ScoreFrag fragment = new ScoreFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public ScoreFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.score_aty, container, false);
        ll = (LinearLayout) view.findViewById(R.id.ll);
        llError = (LinearLayout) view.findViewById(R.id.llError);
        btn = (Button) view.findViewById(R.id.btn);
        btn.setOnClickListener(this);
        doQuery();
        return view;
    }
    
   

   

    private void doQuery() {
        scoreQueryAsync = new ScoreQueryAsync(getActivity());
        scoreQueryAsync.setOnUcardTaskListener(this);
        scoreQueryAsync.executeOnExecutor(((AppActivity)getActivity()).THREAD_POOL_EXECUTOR);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:

                doQuery();
                break;
            
        }
    }

    @Override
    public void onSuccess(List<List<Score>> lists) {
        AppAnalytics.onEvent(getActivity(), AppAnalytics.S_S);
        ll.removeAllViews();
        ll.setVisibility(View.VISIBLE);
        llError.setVisibility(View.GONE);
        for (List<Score> scores : lists) {
            WrapWrapScoreItemView wrapWrapScoreItemView = new WrapWrapScoreItemView(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            wrapWrapScoreItemView.setLayoutParams(lp);

            wrapWrapScoreItemView.update(scores);
            ll.addView(wrapWrapScoreItemView);
        }


    }

    @Override
    public void onFail(String s) {
        AppAnalytics.onEvent(getActivity(), AppAnalytics.S_F);
        ll.setVisibility(View.GONE);
        llError.setVisibility(View.VISIBLE);
    }
    
  
}
