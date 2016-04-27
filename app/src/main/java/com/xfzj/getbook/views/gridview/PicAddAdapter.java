package com.xfzj.getbook.views.gridview;

import android.content.Context;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xfzj.getbook.R;
import com.xfzj.getbook.common.PicPath;

import java.util.List;

/**
 * Created by zj on 2016/4/18.
 */
public class PicAddAdapter extends PicAdapter {
    private static final int LAST = 0;
    private static final int NORMAL = 1;

    private int lastSrc;
    private int maxPics;

    private int defaultSrc;

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

    public void setDefaultSrc(int defaultSrc) {
        this.defaultSrc = defaultSrc;
    }


    public void setLastSrc(int lastSrc) {
        this.lastSrc = lastSrc;
    }


    public void setMaxPics(int maxPics) {
        this.maxPics = maxPics;
    }

    public PicAddAdapter(Context c, List<PicPath> paths, GridView gv) {
        super(c, paths, gv);
        setImageDimen(85, 85);
        setPadding(2);

    }

    @Override
    protected void setLastImage(ImageView imageView, int lastSrc) {

        imageView.setBackgroundResource(0);
        imageView.setImageResource(lastSrc);
    }

    public boolean isLast(int position) {
        if (LAST == getItemViewType(position)) {
            return true;
        }
        return false;
    }

    @Override
    protected void setData(int position, ImageView imageView, int width, int height) {
        if (LAST == getItemViewType(position)) {
            setLastImage(imageView, lastSrc);

        } else {
//            String tag = (String) imageView.getTag();
            String uri = ((PicPath) getItem(position)).getPath();

//            if (!uri.equals(tag)) {
//                imageView.setImageResource(defaultSrc);
//            }
            if (!IsGridViewIdle) {
//                imageView.setTag(uri);
//                imageLoader.bindBitmap(uri, gv, imageView, dimen, dimen);
                loadImage(uri, width, height, imageView);

            }
        }
    }

    @Override
    protected void loadImage(String uri, int width, int height, ImageView imageView) {
        Glide.with(mContext).load(uri).placeholder(R.mipmap.placeholder).error(R.mipmap.error).override(width, height).into(imageView);
    }
}
