package com.xfzj.getbook.async;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xfzj.getbook.BaseApplication;
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.net.HttpHelper;
import com.xfzj.getbook.net.IHttpHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zj on 2016/1/29.
 */
public class UserHeadAsync extends BaseAsyncTask<Integer, Integer, Bitmap> {
    private BaseApplication baseApplication;


    private LoadBitmapCallBack callBack;

    public UserHeadAsync(Context context) {
        super(context);
        baseApplication = (BaseApplication) context.getApplicationContext();

    }

    @Override
    protected void onPost(Bitmap bitmap) {
        if (null == bitmap) {
            if (null != callBack) {
                callBack.onFail();
                
            }
        }else {
            if (null != callBack) {
                callBack.onSuccess(bitmap);
            }
        }

    }

    @Override
    protected Bitmap doExcute(Integer[] params) {
        Map<String, String> param = new HashMap<>();
        if (null == baseApplication || null == baseApplication.user) {
            return null;
        }
        param.put("sno", baseApplication.user.getSno());
        param.put("iPlanetDirectoryPro", baseApplication.user.getMsg());
        param.put("schoolCode", "nuist");
        byte[] bytes = HttpHelper.getInstance().DoConnection(BaseHttp.GetMyPhoto, IHttpHelper.METHOD_POST, param);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public void setCallBack(LoadBitmapCallBack callBack) {
        this.callBack = callBack;
    }

    public interface LoadBitmapCallBack {
        void onSuccess(Bitmap bitmap);

        void onFail();
    }
}
