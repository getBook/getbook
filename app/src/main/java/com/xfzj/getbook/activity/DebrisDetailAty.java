package com.xfzj.getbook.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xfzj.getbook.BaseApplication;
import com.xfzj.getbook.R;
import com.xfzj.getbook.common.Debris;
import com.xfzj.getbook.common.PicPath;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.loader.ImageLoader;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.utils.MyUtils;
import com.xfzj.getbook.utils.Sms;
import com.xfzj.getbook.views.view.BaseToolBar;
import com.xfzj.getbook.views.view.NetImageView;
import com.xfzj.getbook.views.view.SimpleUserView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.BmobUser;

/**
 * Created by zj on 2016/3/4.
 */
public class DebrisDetailAty extends AppActivity implements View.OnClickListener {
    @Bind(R.id.baseToolbar)
    BaseToolBar baseToolBar;
    @Bind(R.id.simpleUserView)
    SimpleUserView simpleUserView;
    @Bind(R.id.describe)
    TextView describe;
    @Bind(R.id.llPics)
    LinearLayout llPics;
    @Bind(R.id.ibSend)
    ImageButton ibSend;
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.newold)
    TextView newold;
    @Bind(R.id.count)
    TextView count;
    @Bind(R.id.tv)
    TextView tv;
    @Bind(R.id.llSendSms)
    LinearLayout llSendSms;
    @Bind(R.id.tvTitle)
    TextView tvTitle;
    public static final String DATA = "DebrisDetailAty.class";


    private Debris debris;

    private User user;

    private ImageLoader imageLoader;

    @Override
    protected void onSetContentView() {
        setContentView(R.layout.aty_debris_detail);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        baseToolBar.initToolbar(this, getString(R.string.detail));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        debris = getIntentData();
        if (null == debris) {
            return;
        }
        
        user = debris.getUser();
        if (null == user) {
            finish();
            return;
        }
        imageLoader = ((BaseApplication) getApplicationContext()).getImageLoader();

        simpleUserView.update(user);
        String tips = debris.getTips();
        if (TextUtils.isEmpty(tips)) {
            tv.setText(tv.getText().toString() + getString(R.string.zanwu));
            describe.setVisibility(View.GONE);
        } else {
            describe.setText(debris.getTips());
            describe.setTextColor(getResources().getColor(R.color.primary_text));
        }

        ibSend.setOnClickListener(this);
        setBaseInfo();
        setDebrisPics();
        needHiden();

    }

    /**
     * 发送信息模块是否要显示
     */
    private void needHiden() {
        User currentUser = BmobUser.getCurrentUser(getApplicationContext(), User.class);
        if (null != currentUser && user.equals(currentUser)) {
            llSendSms.setVisibility(View.GONE);
        } else {
            llSendSms.setVisibility(View.VISIBLE);
        }
    }

    private void setBaseInfo() {
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


    private void setDebrisPics() {
        String[] pics = debris.getPics();
        final List<PicPath> lists = new ArrayList<>();
        for (String str : pics) {
            lists.add(new PicPath(PicPath.FLAG_ALBUM, str));
        }
        
        NetImageView[] ivs = new NetImageView[pics.length];
        int width = MyUtils.getScreenWidth(getApplicationContext());
        int margin = (int) MyUtils.dp2px(getApplicationContext(), 30.0f);
        int marginRight = (int) MyUtils.dp2px(getApplicationContext(), 15.0f);
        Bitmap bp = BitmapFactory.decodeResource(getResources(), R.mipmap.default_book);
        for (int i = 0; i < ivs.length; i++) {
            ivs[i] = new NetImageView(getApplicationContext());
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(width - margin, ViewGroup.LayoutParams.WRAP_CONTENT);
            p.gravity = Gravity.CENTER_HORIZONTAL;
            p.setMargins(0, 0, 0, marginRight);
            ivs[i].setLayoutParams(p);
            ivs[i].setAdjustViewBounds(true);
            ivs[i].setScaleType(ImageView.ScaleType.FIT_XY);

            ivs[i].setBmobImage(pics[i], bp,width, width);
            final int finalI = i;
            ivs[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyToast.show(getApplicationContext(), finalI +"");
                    Intent intent = new Intent(DebrisDetailAty.this, ViewPagerAty.class);
                    intent.putExtra(ViewPagerAty.PATH, (Serializable) lists);
                    intent.putExtra(ViewPagerAty.INDEX, finalI);
                    intent.putExtra(ViewPagerAty.FROM, ViewPagerAty.VIEW);
                    startActivity(intent);
                    
                }
            });
            llPics.addView(ivs[i]);
        }
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
            String tele = debris.getTele();
            if (TextUtils.isEmpty(tele)) {
                MyToast.show(getApplicationContext(), getString(R.string.no_tele));
                return;
            }
            String moudle = getString(R.string.send_message, getString(R.string.app_name), debris.getTitle());
            Sms.sendSms(getApplicationContext(), tele, moudle);
        } 

    }
}