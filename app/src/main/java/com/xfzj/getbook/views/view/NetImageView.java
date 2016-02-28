package com.xfzj.getbook.views.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.async.BaseAsyncTask;
import com.xfzj.getbook.net.HttpHelper;

/**
 * Created by zj on 2016/2/24.
 */
public class NetImageView extends ImageView {


    private String url;
    private Context context;

    public NetImageView(Context context) {
        this(context, null);
    }

    public NetImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public NetImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUrlImage(String url) {
        if (TextUtils.isEmpty(url)) {
            setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.image_default));
            return;
        }
        setUrl(url);
        getImage();
    }

    private void getImage() {
        new BaseAsyncTask<Void, Void, Bitmap>(context) {
            @Override
            protected void onPost(Bitmap bitmap) {
                if (null != bitmap) {
                    setImageBitmap(bitmap);
                } else {
                    setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.image_default));
                }
            }

            @Override
            protected Bitmap doExcute(Void[] params) {
                try {
                    byte[] bytes = new HttpHelper().DoConnection(url);
                    if (null == bytes) {
                        return null;
                    }
                    return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.execute();
    }


}
