package com.xfzj.getbook.views.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.GetAccessUrlListener;
import com.xfzj.getbook.R;
import com.xfzj.getbook.async.BaseAsyncTask;
import com.xfzj.getbook.loader.ImageLoader;
import com.xfzj.getbook.net.HttpHelper;
import com.xfzj.getbook.utils.MyUtils;

import java.io.File;
import java.io.FileOutputStream;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by zj on 2016/2/24.
 */
public class NetImageView extends ImageView {


    private String url;
    private Context context;
    private File cachePath;
    private String isbn;

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

    public void setBmobImage(final String name, final Bitmap defaultImage, final ImageLoader imageLoader) {
        if (TextUtils.isEmpty(name)) {
            setImageBitmap(defaultImage);
            return;
        }

        BmobProFile.getInstance(context).getAccessURL(name, new GetAccessUrlListener() {
            @Override
            public void onSuccess(BmobFile bmobFile) {
                String url = bmobFile.getUrl();
                if (TextUtils.isEmpty(url)) {
                    setImageBitmap(defaultImage);
                } else {
                    imageLoader.bindBitmap(url, NetImageView.this);
                }
            }

            @Override
            public void onError(int i, String s) {
                setImageBitmap(defaultImage);
            }
        });
//        BmobProFile.getInstance(context).download(name, new DownloadListener() {
//            @Override
//            public void onSuccess(String s) {
//                Bitmap bp = BitmapFactory.decodeFile(s);
//                if (null == bp) {
//                    MyLog.print("解析不了", name + "    " + s);
//                    setImageBitmap(defaultImage);
//                } else {
//                    setImageBitmap(bp);
//                }
//            }
//
//            @Override
//            public void onProgress(String s, int i) {
//                MyLog.print(s, i + "");
//            }
//
//            @Override
//            public void onError(int i, String s) {
//                setImageBitmap(defaultImage);
//            }
//        });
    }
}
