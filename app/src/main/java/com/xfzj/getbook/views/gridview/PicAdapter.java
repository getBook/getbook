package com.xfzj.getbook.views.gridview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.common.PicPath;
import com.xfzj.getbook.utils.MyUtils;
import com.xfzj.getbook.views.view.NetImageView;

import java.util.List;

/**
 * Created by zj on 2016/2/8.
 */
public abstract class PicAdapter extends BaseGridViewAdapter {

    private GridView gv;
    private int width;
    private int height;
    private int padding;

    public PicAdapter(Context c, List<PicPath> paths, GridView gv) {

        super(c, paths);
        this.gv = gv;
    }


    @Override
    public int getViewTypeCount() {
        return 1;
    }


    @Override
    public int getCount() {
        return paths.size();
    }


    @Override
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        NetImageView imageView;

        if (convertView == null) {
            imageView = new NetImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(width, height));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(padding, padding, padding, padding);
//            Drawable d= ContextCompat.getDrawable(mContext, R.drawable.gridview);
//            imageView.setBackground(d);
            imageView.setBackgroundResource(R.drawable.gridview);

            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        } else {
            imageView = (NetImageView) convertView;
        }
        setData(position, imageView, width, height);
        return imageView;
    }

    protected abstract void setData(int position, ImageView imageView, int width, int height);


    protected abstract void setLastImage(ImageView imageView, int lastSrc);

    protected abstract void loadImage(String uri, int width, int height, ImageView imageView);

    /**
     * 设置图像的宽高，dp值
     *
     * @param width
     * @param height
     */
    public void setImageDimen(int width, int height) {
        this.width = (int) MyUtils.dp2px(mContext, width);
        this.height = (int) MyUtils.dp2px(mContext, height);
    }

    /**
     * 设置图像的padding,dp值
     *
     * @param padding
     */
    public void setPadding(int padding) {
        this.padding = (int) MyUtils.dp2px(mContext, padding);
    }

    @Override
    protected BaseViewHolder getViewHolder() {
        return null;
    }


    public boolean isFull() {
        if (null != paths) {
            return paths.size() == 4;
        }
        return false;
    }
}
