package com.xfzj.getbook.async;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by zj on 2016/5/2.
 */
public class GetLibraryVerfyAsync extends BaseGetLibraryInfoAsyc<Bitmap> {
    public GetLibraryVerfyAsync(Context context) {
        super(context);
    }

    @Override
    protected boolean needCookie() {
        return true;
    }

    @Override
    protected Bitmap parse(String[] params, String result) {
        if (null != bytes) {
           return  BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        return null;
    }
}
