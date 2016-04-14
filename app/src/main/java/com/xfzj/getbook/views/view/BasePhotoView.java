package com.xfzj.getbook.views.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xfzj.getbook.R;

/**
 * Created by zj on 2016/4/12.
 */
public class BasePhotoView extends PinchImageView {

    private  Context context;

    public BasePhotoView(Context context) {
        this(context,null);
    }

    public BasePhotoView(Context context, AttributeSet attr) {
        this(context, attr,0);
    }

    public BasePhotoView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        this.context = context;
    }


    public void setBmobImage(final String name) {
        if (TextUtils.isEmpty(name)) {
            setImageResource(R.mipmap.placeholder);
            return;
        }
       
        Glide.with(context).load(name).placeholder(R.mipmap.placeholder).error(R.mipmap.error).diskCacheStrategy(DiskCacheStrategy.ALL).into(BasePhotoView.this);
    }
}
