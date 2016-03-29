package com.xfzj.getbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.async.GetBillAsync;
import com.xfzj.getbook.async.UcardAsyncTask;
import com.xfzj.getbook.common.Bill;
import com.xfzj.getbook.views.view.BaseToolBar;

import java.util.List;

import butterknife.Bind;

/**
 * Created by zj on 2016/3/28.
 */
public class BillAty extends AppActivity implements UcardAsyncTask.OnUcardTaskListener<List<Bill>>, View.OnClickListener {
    public static final String YEAR = "year";
    public static final String STARTTIME = "starttime";
    public static final String ENDTIME = "endtime";
    @Bind(R.id.baseToolbar)
    BaseToolBar baseToolBar;
    @Bind(R.id.tv)
    TextView tv;
    @Bind(R.id.llError)
    LinearLayout llError;
    @Bind(R.id.btn)
    Button btn;
    @Bind(R.id.scrollView)
    ScrollView scrollView;

    private String year, startTime, endTime;

    @Override
    protected void onSetContentView() {
        setContentView(R.layout.activity_bill);

    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (null == intent) {
            finish();
            return;
        }
        year = intent.getStringExtra(YEAR);
        startTime = intent.getStringExtra(STARTTIME);
        endTime = intent.getStringExtra(ENDTIME);
        if (TextUtils.isEmpty(year) || TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
            finish();
            return;
        }
        btn.setOnClickListener(this);
        baseToolBar.initToolbar(this, getString(R.string.year_bill, year));
        query();


    }

    private void query() {
        GetBillAsync getBillAsync = new GetBillAsync(BillAty.this);
        getBillAsync.execute(startTime, endTime);
        getBillAsync.setOnUcardTaskListener(this);
    }

    @Override
    public void onSuccess(List<Bill> bills) {
        llError.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
        
        
        


    }

    @Override
    public void onFail(String s) {
        llError.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);


    }

    @Override
    public void onClick(View v) {
        query();
    }
}
