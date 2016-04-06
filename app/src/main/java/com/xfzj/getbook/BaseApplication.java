package com.xfzj.getbook;

import android.app.Application;
import android.util.DisplayMetrics;
import android.view.Display;

import com.bmob.BmobConfiguration;
import com.bmob.BmobPro;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.utils.FileUtils;
import com.xfzj.getbook.utils.SharedPreferencesUtils;

import cn.bmob.v3.Bmob;

/**
 * Created by zj on 2016/1/28.
 */
public class BaseApplication extends Application {
    private Display display;
    private DisplayMetrics dm;
    private static BaseApplication baseApplication;
    private User user;

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
        MobclickAgent.openActivityDurationTrack(false);
        AnalyticsConfig.enableEncrypt(true);
        MobclickAgent.setDebugMode(false);
        Bmob.initialize(this, "953b4c2054c0d44e168d6725f8df4ff7");
        BmobConfiguration config = new BmobConfiguration.Builder(getApplicationContext()).customExternalCacheDir("cache").build();
        BmobPro.getInstance(getApplicationContext()).initConfig(config);
        FileUtils.createDownloadDir();
    }

   

    public User getUser() {
        if (null == user) {
            return SharedPreferencesUtils.getUser(getApplicationContext());
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
