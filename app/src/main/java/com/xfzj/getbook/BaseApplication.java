package com.xfzj.getbook;

import android.app.Application;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.Display;

import com.bmob.BmobConfiguration;
import com.bmob.BmobPro;
import com.xfzj.getbook.loader.ImageLoader;

import cn.bmob.v3.Bmob;

/**
 * Created by zj on 2016/1/28.
 */
public class BaseApplication extends Application {
    private Display display;
    private DisplayMetrics dm;
    private static BaseApplication baseApplication;
    private ImageLoader imageLoader;

    public BaseApplication() {

    }


    public static BaseApplication newInstance() {
        if (null == baseApplication) {
            baseApplication = new BaseApplication();
        }
        return baseApplication;
    }

    @Override

    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "953b4c2054c0d44e168d6725f8df4ff7");
        BmobConfiguration config = new BmobConfiguration.Builder(getApplicationContext()).customExternalCacheDir("cache").build();
        BmobPro.getInstance(getApplicationContext()).initConfig(config);
        imageLoader = ImageLoader.build(getApplicationContext(), BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
    }


    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
