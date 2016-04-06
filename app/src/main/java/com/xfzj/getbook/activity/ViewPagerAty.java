package com.xfzj.getbook.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.PicPath;
import com.xfzj.getbook.views.view.BaseToolBar;
import com.xfzj.getbook.views.view.NetImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by zj on 2016/2/28.
 */
public class ViewPagerAty extends AppActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    public static final String PATH = "picpaths";
    public static final String INDEX = "index";
    public static final String FROM = "ViewPagerAty.class";
    public static final String VIEW = "View";
    public static final String EDIT = "Edit";
    @Bind(R.id.baseToolbar)
    BaseToolBar baseToolBar;

    @Bind(R.id.viewPager)
    ViewPager viewPager;

    private TextView tvMiddle, tvDelete;

    private List<ImageView> ivs;
    /**
     * 传进来的图片路径
     */
    private List<PicPath> paths = new ArrayList<>();
    private MyAdapter myAdapter;

    public void setPicPaths(List<PicPath> paths) {
        this.paths = paths;
        ivs = new ArrayList<>();
        for (int i = 0; i < this.paths.size(); i++) {
            NetImageView iv = new NetImageView(getApplicationContext());
            
            iv.setBmobImageWith(this.paths.get(i).getPath(), BitmapFactory.decodeResource(getResources(), R.mipmap.image_default));
            ivs.add(iv);
        }
    }

    @Override
    protected void onSetContentView() {
        setContentView(R.layout.viewpager);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        baseToolBar.initToolbar(this, "");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvMiddle = baseToolBar.getTv3();
       String from= getIntent().getStringExtra(FROM);
        if (from.equals(EDIT)) {
            tvDelete = baseToolBar.getTv2();
            tvDelete.setVisibility(View.VISIBLE);
            tvDelete.setBackgroundResource(R.drawable.delete_select);
            RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) tvDelete.getLayoutParams();
            p.height = (int)getResources().getDimension(R.dimen.abc_action_bar_default_height_material)/2;
            p.width = (int)getResources().getDimension(R.dimen.abc_action_bar_default_height_material)/2;
            tvDelete.setLayoutParams(p);
            tvDelete.setOnClickListener(this);
         
        }

        setPicPaths((List<PicPath>) getIntent().getSerializableExtra(PATH));
        
        tvMiddle.setVisibility(View.VISIBLE);
        myAdapter = new MyAdapter();
        viewPager.setAdapter(myAdapter);
        viewPager.addOnPageChangeListener(this);
        setCurrentItem(getIntent().getIntExtra(INDEX, 0));
      

    }




    private int getPathsSize() {
        if (null != paths) {
            return paths.size();
        }
        return 0;
    }

    public void setCurrentItem(int index) {
        viewPager.setCurrentItem(index);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        tvMiddle.setText(getString(R.string.jifenzhiji, position + 1, myAdapter.getCount()));
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBackPressed() {
        jump2back();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        jump2back();
        return true;
    }

    private void jump2back() {
        Intent i = new Intent();
        i.putExtra(PublishSecondBookActivity.REMAIN_PATHS, (Serializable) paths);
        setResult(PublishSecondBookActivity.REMAIN_PATHS_CODE, i);
        System.gc();
        finish();
    }

    @Override
    public void onClick(View v) {
        final int index = viewPager.getCurrentItem();
        new AlertDialog.Builder(ViewPagerAty.this).setTitle(getString(R.string.tishi)).setMessage(getString(R.string.want_delete)).setNegativeButton(getString(R.string.cancel), null).setPositiveButton(getString(R.string.ensure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                paths.remove(index);
                
                ivs.remove(index);
                myAdapter.notifyDataSetChanged();
                if (getPathsSize() == 0) {
                    jump2back();
                }
                if (index == getPathsSize()) {
                    setCurrentItem(index);
                } else {
                    setCurrentItem(index);
                }

            }
        }).create().show();


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
