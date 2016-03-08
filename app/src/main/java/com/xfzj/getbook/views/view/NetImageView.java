package com.xfzj.getbook.views.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.xfzj.getbook.BaseApplication;
import com.xfzj.getbook.R;
import com.xfzj.getbook.async.BaseAsyncTask;
import com.xfzj.getbook.loader.ImageLoader;
import com.xfzj.getbook.net.HttpHelper;
import com.xfzj.getbook.utils.MyUtils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by zj on 2016/2/24.
 */
public class NetImageView extends ImageView {


    private String url;
    private Context context;
    private File cachePath;
    private String isbn;
    private ImageLoader imageLoader;
    
    public NetImageView(Context context) {
        this(context, null);
    }

    public NetImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        imageLoader = ((BaseApplication) context.getApplicationContext()).getImageLoader();
    }

    public NetImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        imageLoader = ((BaseApplication) context.getApplicationContext()).getImageLoader();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setUrlImage(String url, String isbn) {
        if (TextUtils.isEmpty(url)) {
            setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.image_default));
            return;
        }
        setIsbn(isbn);
        setUrl(url);
        getImage();
    }

    private void getImage() {
        if (TextUtils.isEmpty(url)) {
            setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.image_default));
            return;
        }
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
                    cachePath = MyUtils.getDiskCacheDir(context, isbn + ".jpg");
                    if (cachePath.exists()) {

                        return BitmapFactory.decodeFile(cachePath.getPath());

                    } else {
                        byte[] bytes = new HttpHelper().DoConnection(url);
                        if (null == bytes) {
                            return null;
                        }

                        FileOutputStream fos = new FileOutputStream(cachePath);
                        fos.write(bytes);
                        fos.flush();
                        fos.close();

                        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.execute();

    }

    public String getCachePath() {
        return cachePath.getPath();
    }

    public void setBmobImage(final String name, final Bitmap defaultImage, int width,int height) {
        if (TextUtils.isEmpty(name)) {
            setImageBitmap(defaultImage);
            return;
        }
        setImageBitmap(defaultImage);
        imageLoader.bindBitmap(name, NetImageView.this,width,height);
    }

    public void setBmobImage(final String name, final Bitmap defaultImage) {
        setBmobImage(name, defaultImage, 0, 0);
    }
}
