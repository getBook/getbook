package com.xfzj.getbook.views.gridview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.xfzj.getbook.utils.MyUtils;

import java.util.List;

/**
 * Created by zj on 2016/2/8.
 */
public class PicAddAdapter extends BaseGridViewAdapter {
    private static final int LAST = 0;
    private static final int NORMAL = 1;

    private int lastSrc;
    private int maxPics;

    private int defaultSrc;

    public PicAddAdapter(Context c, List<String> paths) {
        super(c, paths);
    }

    public void setDefaultSrc(int defaultSrc) {
        this.defaultSrc = defaultSrc;
    }


    public void setLastSrc(int lastSrc) {
        this.lastSrc = lastSrc;
    }


    public void setMaxPics(int maxPics) {
        this.maxPics = maxPics;
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @Override
    public int getCount() {
        return paths.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == paths.size())
            return LAST;
        return NORMAL;
    }

    @Override
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        int dimen = (int) MyUtils.dp2px(mContext, 85);
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(dimen, dimen));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        if (LAST == getItemViewType(position)) {
            imageView.setImageResource(lastSrc);
        } else {
            String tag = (String) imageView.getTag();
            String uri = (String) getItem(position);

            if (!uri.equals(tag)) {
                imageView.setImageResource(defaultSrc);
            }
            if (!IsGridViewIdle) {
                imageView.setTag(uri);
                imageLoader.bindBitmap(uri, imageView, dimen, dimen);
            }
        }
        return imageView;
    }

    @Override
    protected BaseViewHolder getViewHolder() {
        return null;
    }

    public boolean isLast(int position) {
        if (LAST == getItemViewType(position)) {
            return true;
        }
        return false;
    }


    public boolean isFull() {
        if (null != paths) {
            return paths.size() == 4;
        }
        return false;
    }
}
