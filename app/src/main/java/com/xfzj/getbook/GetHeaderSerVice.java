package com.xfzj.getbook;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.xfzj.getbook.async.UserHeadAsync;
import com.xfzj.getbook.common.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by zj on 2016/2/28.
 */
public class GetHeaderSerVice extends Service implements UserHeadAsync.LoadBitmapCallBack {
    private UserHeadAsync userHeadAsync;
    private User user;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        user = BmobUser.getCurrentUser(getApplicationContext(), User.class);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        BmobQuery<User> userBmobQuery = new BmobQuery<>();
        userBmobQuery.addWhereEqualTo("sno", user.getSno());
        userBmobQuery.findObjects(getApplicationContext(), new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                User user = list.get(0);
                String header = user.getHeader();
                if (TextUtils.isEmpty(header) || !header.endsWith(".jpg")) {
                    UserHeadAsync userHeadAsync = new UserHeadAsync(getApplicationContext(), user.getSno());
                    userHeadAsync.execute();
                    userHeadAsync.setCallBack(GetHeaderSerVice.this);
                }

            }

            @Override
            public void onError(int i, String s) {

            }
        });


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onSuccess(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        
        BmobProFile.getInstance(getApplicationContext()).upload(str, new UploadListener() {
            @Override
            public void onSuccess(String s, String s1, BmobFile bmobFile) {


                user.setHeader(s);
                user.update(getApplicationContext(), new UpdateListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
            }

            @Override
            public void onProgress(int i) {

            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }

    @Override
    public void onFail() {

    }
}
