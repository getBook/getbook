package com.xfzj.getbook;

import android.app.Application;
import android.util.DisplayMetrics;
import android.view.Display;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.utils.FileUtils;
import com.xfzj.getbook.utils.SharedPreferencesUtils;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

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
//        //设置BmobConfig
        BmobConfig config =new BmobConfig.Builder()
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setBlockSize(500*1024)
                .build();
        Bmob.getInstance().initConfig(config);
        Bmob.initialize(this, "953b4c2054c0d44e168d6725f8df4ff7");
        FileUtils.createDownloadDir();
        FeedbackAPI.initAnnoy(this, "23357392");
//        BmobUpdateAgent.setUpdateCheckConfig(false);
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
