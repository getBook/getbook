package com.xfzj.getbook.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.xfzj.getbook.loader.ImageResizer;
import com.xfzj.getbook.utils.MyUtils;
import com.xfzj.getbook.views.view.BaseToolBar;

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

    public void setPaths(List<PicPath> paths) {
        this.paths = paths;
        ivs = new ArrayList<>();
        for (int i = 0; i < this.paths.size(); i++) {
            ImageView iv = new ImageView(getApplicationContext());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(this.paths.get(i).getPath(), options);
            if (options.outWidth >= 4096 || options.outHeight >= 4096) {
                options.inSampleSize = new ImageResizer().calculateInSampleSize(options, MyUtils.getScreenMetrics(getApplicationContext()).widthPixels, MyUtils.getScreenMetrics(getApplicationContext()).heightPixels);
            }
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(this.paths.get(i).getPath(), options);
            iv.setImageBitmap(bitmap);
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
        tvDelete = baseToolBar.getTv2();
        tvMiddle.setVisibility(View.VISIBLE);
        tvDelete.setVisibility(View.VISIBLE);
        tvDelete.setBackgroundResource(R.drawable.delete_select);
        RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) tvDelete.getLayoutParams();
        p.height = (int)getResources().getDimension(R.dimen.abc_action_bar_default_height_material)/2;
        p.width = (int)getResources().getDimension(R.dimen.abc_action_bar_default_height_material)/2;
        
        tvDelete.setLayoutParams(p);
        setPaths((List<PicPath>) getIntent().getSerializableExtra(PATH));
        myAdapter = new MyAdapter();
        viewPager.setAdapter(myAdapter);
        viewPager.addOnPageChangeListener(this);
        setCurrentItem(getIntent().getIntExtra(INDEX, 0));
        tvDelete.setOnClickListener(this);

    }


    private int getPathsSize() {
        if (null != paths) {
            return paths.size();
        }
        return 0;
    }

    public void setCurrentItem(int index) {
        viewPager.setCurrentItem(index);
        if (index == 0 && getPathsSize() != 0) {
            tvMiddle.setText(getString(R.string.jifenzhiji, index + 1, getPathsSize()));
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tvMiddle.setText(getString(R.string.jifenzhiji, position + 1, getPathsSize()));

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
        i.putExtra(PublishActivity.REMAIN_PATHS, (Serializable) paths);
        setResult(PublishActivity.REMAIN_PATHS_CODE, i);
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
