package com.xfzj.getbook.views.gridview;

import android.content.Context;
import android.widget.GridView;
import android.widget.ImageView;

import com.xfzj.getbook.common.PicPath;
import com.xfzj.getbook.views.view.NetImageView;

import java.util.List;

/**
 * Created by zj on 2016/4/18.
 */
public class PicShowAdapter extends PicAdapter {
    public PicShowAdapter(Context c, List<PicPath> paths, GridView gv) {
        super(c, paths, gv);
        setImageDimen(85, 180);
        setPadding(2);

    }

    @Override
    protected void setData(int position, ImageView imageView, int width, int height) {
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

    @Override
    protected void setLastImage(ImageView imageView, int lastSrc) {

    }

    @Override
    protected void loadImage(String uri, int width, int height, ImageView imageView) {
        if (imageView instanceof NetImageView) {
            ((NetImageView) imageView).setBmobthumbnail(uri, width, height);
        }
    }
}
