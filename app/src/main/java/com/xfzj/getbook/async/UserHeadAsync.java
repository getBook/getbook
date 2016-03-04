package com.xfzj.getbook.async;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.xfzj.getbook.common.User;
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.net.HttpHelper;
import com.xfzj.getbook.net.IHttpHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.BmobUser;

/**
 * Created by zj on 2016/1/29.
 */
public class UserHeadAsync extends BaseAsyncTask<Integer, Integer, String> {
    private User user;


    private LoadBitmapCallBack callBack;

    private Context context;
    private String key;

    public UserHeadAsync(Context context, String sno) {

        super(context);
        this.context = context;
        this.key = sno;
        user = BmobUser.getCurrentUser(context, User.class);

    }

    @Override
    protected void onPost(String str) {
        if (TextUtils.isEmpty(str)) {
            if (null != callBack) {
                callBack.onFail();

            }
        } else {
            if (null != callBack) {
                callBack.onSuccess(str);
            }
        }

    }

    @Override
    protected String doExcute(Integer[] params) {
        try {
            Map<String, String> param = new HashMap<>();
            if (null == user ) {
                return null;
            }
            param.put("sno", user.getSno());
            param.put("iPlanetDirectoryPro", user.getMsg());
            param.put("schoolCode", "nuist");
            byte[] bytes = new HttpHelper().DoConnection(BaseHttp.GetMyPhoto, IHttpHelper.METHOD_POST, param);
            if (null == bytes) {
                return null;
            }
            File file = getDiskCacheDir(System.currentTimeMillis() + key + "headerCache.jpg");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();
            return file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setCallBack(LoadBitmapCallBack callBack) {
        this.callBack = callBack;
    }

    public interface LoadBitmapCallBack {
        void onSuccess(String str);

        void onFail();
    }

    public File getDiskCacheDir(String uniqueName) {
        boolean externalStorageAvailable = Environment
                .getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        final String cachePath;
        if (externalStorageAvailable) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }

        return new File(cachePath + File.separator + uniqueName);
    }
}
