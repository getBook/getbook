package com.xfzj.getbook;

import android.app.Application;

import com.xfzj.getbook.common.User;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * Created by zj on 2016/1/28.
 */
public class BaseApplication extends Application {
    public User user;
            
            
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "953b4c2054c0d44e168d6725f8df4ff7");
        user = BmobUser.getCurrentUser(this, User.class);
    }
}
