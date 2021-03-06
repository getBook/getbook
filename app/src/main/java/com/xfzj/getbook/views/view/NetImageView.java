package com.xfzj.getbook.views.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.xfzj.getbook.R;
import com.xfzj.getbook.async.BaseAsyncTask;
import com.xfzj.getbook.async.GetBmobthumbnail;
import com.xfzj.getbook.common.BmobThumbnail;
import com.xfzj.getbook.net.HttpHelper;
import com.xfzj.getbook.utils.MyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by zj on 2016/2/24.
 */
public class NetImageView extends ImageView {
    public static final int SMALL_WIDTH = 200;
    public static final int SMALL_HEIGHT = 200;
    public static final int LARGE_WIDTH = 300;
    public static final int LARGE_HEIGHT = 800;
    private String url;
    private Context context;
    private File cachePath;
    private String isbn;
    private static final int CPU_COUNT = Runtime.getRuntime()
            .availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final long KEEP_ALIVE = 10L;

    private static Map<String, String> map = new HashMap<>();
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "ImageLoader#" + mCount.getAndIncrement());
        }
    };

    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
            KEEP_ALIVE, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(), sThreadFactory);
    /**
     * glide图片缓存路径
     */
    private String cahceName;

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

    public String getCahceName() {
        return cahceName;
    }

    public void setCahceName(String cahceName) {
        this.cahceName = cahceName;
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
        }.executeOnExecutor(THREAD_POOL_EXECUTOR);

    }

    public String getCachePath() {
        return cachePath.getPath();
    }

    public void setNetImage(final String name) {
        setNetImage(name, null);
    }
    public void setNetImage(final String name, final OnImageCallBack onImageCallBack) {
        if (TextUtils.isEmpty(name)) {
            setImageResource(R.mipmap.placeholder);
            return;
        }
        this.cahceName = name;
        setImageResource(R.mipmap.placeholder);
        Glide.with(context).load(name).error(R.mipmap.error).diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate().into(new ViewTarget<NetImageView, GlideDrawable>(NetImageView.this) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                setImageDrawable(resource.getCurrent());
                if (null != onImageCallBack) {
                    onImageCallBack.onLoaded();
                }
            }


        });
    }
    public interface OnImageCallBack{
        void onLoaded();
    }
    /**
     * 设置bmobfile上传图片的缩略图
     *
     * @param bmobFile
     */
    public void setBmobthumbnail(final BmobFile bmobFile, int width, int height) {
        if (null == bmobFile) {
            setImageResource(R.mipmap.placeholder);
            return;
        }
        setBmobthumbnail(bmobFile.getFileUrl(context), width, height);


    }

    public void setBmobthumbnail(final String uri, int width, int height) {
        if (TextUtils.isEmpty(uri)) {
            setImageResource(R.mipmap.placeholder);
            return;
        }
        setImageResource(R.mipmap.placeholder);
        if (map.containsKey(uri)) {
            Glide.with(context).load(map.get(uri)).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.placeholder).error(R.mipmap.error).dontAnimate().into(NetImageView.this);
            return;
        }
        BmobThumbnail bmobThumbnail = new BmobThumbnail(uri, width, height);
        GetBmobthumbnail getBmobthumbnail = new GetBmobthumbnail(context);
        getBmobthumbnail.executeOnExecutor(THREAD_POOL_EXECUTOR, bmobThumbnail);
        getBmobthumbnail.setOnTaskListener(new BaseAsyncTask.onTaskListener<String>() {
            @Override
            public void onSuccess(String s) {
                map.put(uri, s);
                cahceName = s;
                try {
                    Glide.with(context).load(s).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.placeholder).error(R.mipmap.error).dontAnimate().into(NetImageView.this);
                } catch (Exception e) {
                    setImageResource(R.mipmap.placeholder);
                }
            }

            @Override
            public void onFail() {
                setImageResource(R.mipmap.placeholder);
            }
        });
    }

    /**
     * 设置bmobfile上传的图片
     *
     * @param image
     */
    public void setBmobFileImage(BmobFile image) {
        if (null == image) {
            setImageResource(R.mipmap.placeholder);
            return;
        }
        setImageResource(R.mipmap.placeholder);
        Glide.with(context).load(image.getFileUrl(context)).placeholder(R.mipmap.placeholder).diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate().into(NetImageView.this);
    }
}
