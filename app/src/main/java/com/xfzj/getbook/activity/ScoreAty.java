package com.xfzj.getbook.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xfzj.getbook.R;
import com.xfzj.getbook.async.BaseAsyncTask;
import com.xfzj.getbook.async.ScoreQueryAsync;
import com.xfzj.getbook.common.Score;
import com.xfzj.getbook.utils.AppAnalytics;
import com.xfzj.getbook.views.view.BaseToolBar;
import com.xfzj.getbook.views.view.WrapWrapScoreItemView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by zj on 2016/3/14.
 */
public class ScoreAty extends AppActivity implements View.OnClickListener, BaseAsyncTask.onTaskListener<List<List<Score>>> {
    @Bind(R.id.ll)
    LinearLayout ll;
    @Bind(R.id.llError)
    LinearLayout llError;
    @Bind(R.id.btn)
    Button btn;
@Bind(R.id.baseToolbar)
    BaseToolBar baseToolBar;
    private ScoreQueryAsync scoreQueryAsync;

    @Override
    protected void onSetContentView() {

        setContentView(R.layout.score_aty);

    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        btn.setOnClickListener(this);
        baseToolBar.initToolbar(this, getString(R.string.grades));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        doQuery();


    }

    private void doQuery() {
        scoreQueryAsync = new ScoreQueryAsync(ScoreAty.this);
        scoreQueryAsync.setOnTaskListener(this);
        scoreQueryAsync.execute();

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
        AppAnalytics.onEvent(getApplicationContext(), AppAnalytics.S_S);
        ll.removeAllViews();
        ll.setVisibility(View.VISIBLE);
        llError.setVisibility(View.GONE);
        for (List<Score> scores : lists) {
            WrapWrapScoreItemView wrapWrapScoreItemView = new WrapWrapScoreItemView(getApplicationContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            wrapWrapScoreItemView.setLayoutParams(lp);

            wrapWrapScoreItemView.update(scores);
            ll.addView(wrapWrapScoreItemView);
        }


    }

    @Override
    public void onFail() {
        AppAnalytics.onEvent(getApplicationContext(), AppAnalytics.S_F);
        ll.setVisibility(View.GONE);
        llError.setVisibility(View.VISIBLE);
    }
  
}
