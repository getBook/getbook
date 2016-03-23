package com.xfzj.getbook.async;

import android.content.Context;

import com.xfzj.getbook.BaseApplication;
import com.xfzj.getbook.common.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zj on 2016/3/14.
 */
public abstract class UcardAsyncTask<K, V, T> extends BaseAsyncTask<K, V, T> {
    protected User user;
    protected Map<String, String> param;
    protected OnUcardTaskListener<T> onUcardTaskListener;
    public UcardAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected T doExcute(K[] params) {
        BaseApplication baseApplication = (BaseApplication) context.getApplicationContext();
        if (null == baseApplication) {
            return null;
        }
        user = baseApplication.getUser();
        if (null == user) {
            return null;
        }
        param = new HashMap<>();
        param.put("schoolCode", "nuist");
        param.put("iPlanetDirectoryPro", user.getMsg());
        return excute(params);
    }

    protected abstract T excute(K[] params);

    public void setOnUcardTaskListener(UcardAsyncTask.OnUcardTaskListener<T> onUcardTaskListener) {
        this.onUcardTaskListener = onUcardTaskListener;
    }

    public interface OnUcardTaskListener<T>{
        void onSuccess(T t);

        void onFail(String s);
    }
    
}
