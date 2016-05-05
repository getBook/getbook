package com.xfzj.getbook.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.Debris;
import com.xfzj.getbook.utils.MyToast;

import butterknife.Bind;

/**
 * Created by zj on 2016/3/4.
 */
public class DebrisDetailAty extends DetailActivity {


    @Bind(R.id.tvTitle)
    TextView tvTitle;
    public static final String DATA = "DebrisDetailAty.class";


    private Debris debris;


    @Override
    protected void onSetContentView() {
        setContentView(R.layout.aty_debris_detail);
    }

    @Override
    protected void onViewCreate(Bundle savedInstanceState) {
        debris = getIntentData();
        if (null == debris) {
            return;
        }

        user = debris.getUser();
        if (null == user) {
            finish();
            return;
        }

        simpleUserView.update(user);
        String tips = debris.getTips();
        if (TextUtils.isEmpty(tips)) {
            tv.setText(tv.getText().toString() + getString(R.string.zanwu));
            describe.setVisibility(View.GONE);
        } else {
            describe.setText(debris.getTips());
            describe.setTextColor(getResources().getColor(R.color.primary_text));
        }
    }


    @Override
    protected void setBaseInfo() {
        tvTitle.setText(debris.getTitle());
        String strprice = debris.getDiscount();
        if (TextUtils.isEmpty(strprice)) {
            price.setText(getString(R.string.no_price));
        } else {
            price.setText(strprice);
        }

        String strnewold = debris.getNewold();
        if (TextUtils.isEmpty(strnewold)) {
            newold.setText("0" + getString(R.string.chengxin));
        } else {
            newold.setText(strnewold + getString(R.string.chengxin));
        }
        int strcount = debris.getCount();
        count.setText(strcount + "");
    }

    @Override
    protected void setPics() {
        bmobFiles = debris.getFiles();
    }

    private Debris getIntentData() {
        Debris debris = (Debris) getIntent().getSerializableExtra(DATA);
        if (null == debris || null == debris.getUser()) {
            MyToast.show(getApplicationContext(), getString(R.string.error));
            finish();
        }
        return debris;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ibSend) {
            sendSms(debris.getTele(), debris.getTitle(), R.string.debris);
        }
    }


    @Override
    protected String getName() {
        if (null != debris) {
            return debris.getTitle();
        }
        return null;
    }

    @Override
    protected String getDiscount() {
        if (null != debris) {
            return debris.getDiscount();
        }
        return null;
    }
}