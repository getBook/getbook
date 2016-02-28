package com.xfzj.getbook;

import android.app.Application;
import android.util.DisplayMetrics;
import android.view.Display;

import com.bmob.BmobConfiguration;
import com.bmob.BmobPro;
import com.xfzj.getbook.common.User;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * Created by zj on 2016/1/28.
 */
public class BaseApplication extends Application {
    public User user;
    private Display display;
    private DisplayMetrics dm;
    private static BaseApplication baseApplication;


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
        user = BmobUser.getCurrentUser(this, User.class);
        BmobConfiguration config = new BmobConfiguration.Builder(getApplicationContext()).customExternalCacheDir("缓存").build();
        BmobPro.getInstance(getApplicationContext()).initConfig(config);
    }
    
}
