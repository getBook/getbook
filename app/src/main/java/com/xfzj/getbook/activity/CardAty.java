package com.xfzj.getbook.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.async.BaseAsyncTask;
import com.xfzj.getbook.async.GetCardInfoAsync;
import com.xfzj.getbook.common.Card;
import com.xfzj.getbook.views.view.BaseToolBar;

import butterknife.Bind;

/**
 * Created by zj on 2016/3/14.
 */
public class CardAty extends AppActivity implements View.OnClickListener, BaseAsyncTask.onTaskListener<Card> {
    @Bind(R.id.tvCardNo)
    TextView tvCardNo;
    @Bind(R.id.baseToolbar)
    BaseToolBar baseToolBar;
    @Bind(R.id.framStatus)
    FrameLayout framStatus;
    @Bind(R.id.tvUnnormal)
    TextView tvUnnormal;
    @Bind(R.id.tvRemain)
    TextView tvRemain;
    @Bind(R.id.ivChongZhi)
    ImageView ivChongZhi;
    @Bind(R.id.tvGuoduRemain)
    TextView tvGuoduRemain;
    @Bind(R.id.tvLiuShui)
    TextView tvLiuShui;
    @Bind(R.id.tvBuZhu)
    TextView tvBuZhu;
    @Bind(R.id.tvXiuGaiMiMa)
    TextView tvXiuGaiMiMa;
    @Bind(R.id.tvGuaShi)
    TextView tvGuaShi;
    @Bind(R.id.btn)
    Button btn;
    @Bind(R.id.ll)
    LinearLayout ll;
    @Bind(R.id.llError)
    LinearLayout llError;
    private GetCardInfoAsync getCardInfoAsync;
    @Override
    protected void onSetContentView() {
        setContentView(R.layout.card_layout);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        baseToolBar.initToolbar(this, getString(R.string.yikatong));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ivChongZhi.setOnClickListener(this);
        tvLiuShui.setOnClickListener(this);
        tvBuZhu.setOnClickListener(this);
        tvXiuGaiMiMa.setOnClickListener(this);
        tvGuaShi.setOnClickListener(this);
        btn.setOnClickListener(this);
        getCardInfo();

    }

    private void getCardInfo() {
        getCardInfoAsync=new GetCardInfoAsync(CardAty.this);
        getCardInfoAsync.setOnTaskListener(this);
        getCardInfoAsync.execute();
    }
    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivChongZhi:
                break;
            case R.id.tvLiuShui:
                break;
            case R.id.tvBuZhu:
                break;
            case R.id.tvXiuGaiMiMa:
                break;
            case R.id.tvGuaShi:
                break;
            case R.id.btn:
                getCardInfo();
                break;
        }
    }

    @Override
    public void onSuccess(Card card) {
        ll.setVisibility(View.VISIBLE);
        llError.setVisibility(View.GONE);
        
        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/Farrington.ttf");
        tvCardNo.setTypeface(typeface);
        String cno = card.getBankno();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cno.length(); i++) {
            sb.append(cno.charAt(i) + " ");
        }
        tvCardNo.setText(sb.toString());
        tvRemain.setText(card.getCardbalance());
        tvGuoduRemain.setText(card.getPretmpbalance());
    }

    @Override
    public void onFail() {
        ll.setVisibility(View.GONE);
        llError.setVisibility(View.VISIBLE);
    }
}
