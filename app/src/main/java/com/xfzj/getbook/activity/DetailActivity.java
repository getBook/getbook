package com.xfzj.getbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;
import com.xfzj.getbook.R;
import com.xfzj.getbook.common.PicPath;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.utils.Sms;
import com.xfzj.getbook.views.view.BaseToolBar;
import com.xfzj.getbook.views.view.NetImageView;
import com.xfzj.getbook.views.view.SimpleUserView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * Created by zj on 2016/4/13.
 */
public abstract class DetailActivity extends AppActivity implements View.OnClickListener {
    @Bind(R.id.baseToolbar)
    BaseToolBar baseToolBar;
    @Bind(R.id.simpleUserView)
    SimpleUserView simpleUserView;
    @Bind(R.id.describe)
    TextView describe;
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
    @Bind(R.id.view_pager)
    AutoScrollViewPager viewPager;
    @Bind(R.id.pageIndicator)
    CirclePageIndicator pageIndicator;
    protected User user;
    
    protected List<ImageView> ivs;
    protected List<PicPath> picPaths = new ArrayList<>();
    protected List<BmobFile> bmobFiles;
    private MyAdapter myAdapter;

    /**
     * 发送信息模块是否要显示
     */
    protected void needHiden() {
        User currentUser = BmobUser.getCurrentUser(getApplicationContext(), User.class);
        if (null != currentUser && user.equals(currentUser)) {
            llSendSms.setVisibility(View.GONE);
        } else {
            llSendSms.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        baseToolBar.initToolbar(this, getString(R.string.detail));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        onViewCreate(savedInstanceState);
        setBaseInfo();
        setPicPaths();
        needHiden();
        ibSend.setOnClickListener(this);
        myAdapter = new MyAdapter();
        viewPager.setAdapter(myAdapter);
        pageIndicator.setViewPager(viewPager);
    }

    public void setPicPaths() {
        setPics();
        for (BmobFile file : bmobFiles) {
            picPaths.add(new PicPath(PicPath.FLAG_ALBUM, file.getFileUrl(getApplicationContext())));
        }
        ivs = new ArrayList<>();
        for (int i = 0; i < this.bmobFiles.size(); i++) {
            NetImageView iv = new NetImageView(getApplicationContext());
            iv.setBmobthumbnail(this.bmobFiles.get(i), NetImageView.LARGE_WIDTH, NetImageView.LARGE_HEIGHT);
            iv.setAdjustViewBounds(true);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailActivity.this, ViewPagerAty.class);
                    intent.putExtra(ViewPagerAty.PATH, (Serializable) picPaths);
                    intent.putExtra(ViewPagerAty.INDEX, viewPager.getCurrentItem());
                    intent.putExtra(ViewPagerAty.FROM, ViewPagerAty.VIEW);
                    startActivity(intent);
                }
            });
            ivs.add(iv);
        }
    }

    protected abstract void onViewCreate(Bundle savedInstanceState);

    protected abstract void setBaseInfo();

    protected abstract void setPics();

    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
            return true;
        }
        return false;
    }

    public void sendSms(String tele, String title, int id) {
        if (TextUtils.isEmpty(tele)) {
            MyToast.show(getApplicationContext(), getString(R.string.no_tele));
            return;
        }
        String moudle = getString(R.string.send_message, getString(R.string.app_name), title);
        Sms.sendSms(this, tele, moudle, id);
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return ivs.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(ivs.get(position));

            return ivs.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
