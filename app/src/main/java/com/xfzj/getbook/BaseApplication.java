package com.xfzj.getbook;

import android.app.Application;
import android.util.DisplayMetrics;
import android.view.Display;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.utils.FaceConversionUtil;
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
        Bmob.getInstance().synchronizeTime(getApplicationContext());
        FileUtils.createDownloadDir();
        FeedbackAPI.initAnnoy(this, "23357392");
//        BmobUpdateAgent.setUpdateCheckConfig(false);
        initSocail();
        FaceConversionUtil.getInstace().getFileText(getApplicationContext());
    }

    private void initSocail() {
//        Log.LOG = false;
//
//        Config.IsToastTip = false;
        Config.isloadUrl = true;
        PlatformConfig.setWeixin("wx6bb9118d609db96b", "c9143676f95866c7cec7ffbd32228174");
        //微信 appid appsecret
        PlatformConfig.setSinaWeibo("1046885584","4592ac985a563323e1af824eb96c2a33");
        //新浪微博 appkey appsecret
        PlatformConfig.setQQZone("1105302925", "M9YJo5sA3J4RYZAE");
        // QQ和Qzone appid appkey
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
