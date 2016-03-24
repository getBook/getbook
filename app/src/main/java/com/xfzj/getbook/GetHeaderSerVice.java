package com.xfzj.getbook;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.xfzj.getbook.async.BaseAsyncTask;
import com.xfzj.getbook.async.UserHeadAsync;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.utils.SharedPreferencesUtils;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by zj on 2016/2/28.
 */
public class GetHeaderSerVice extends Service implements BaseAsyncTask.onTaskListener<String> {
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

    }


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        user = ((BaseApplication) getApplication()).getUser();
        if (null != user) {
            BmobQuery<User> userBmobQuery = new BmobQuery<>();
            userBmobQuery.addWhereEqualTo("sno", user.getSno());
            userBmobQuery.findObjects(getApplicationContext(), new FindListener<User>() {
                @Override
                public void onSuccess(List<User> list) {
                    User user = list.get(0);
                    String header = user.getHeader();
                    String localHeader = SharedPreferencesUtils.getUserHeader(getApplicationContext());
                    if (TextUtils.isEmpty(header) || !header.endsWith(".jpg")) {
                        UserHeadAsync userHeadAsync = new UserHeadAsync(getApplicationContext(), user.getSno());
                        userHeadAsync.execute();
                        userHeadAsync.setOnTaskListener(GetHeaderSerVice.this);
                    } else if (TextUtils.isEmpty(localHeader)) {
                        saveHeader(header);
                    }
                }

                @Override
                public void onError(int i, String s) {

                }
            });
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void saveHeader(String header) {
        SharedPreferencesUtils.saveUserHeader(getApplicationContext(), header);
        sendBroadcast(new Intent("com.xfzj.getbook.receiveHeader"));
        
    }

    @Override
    public void onSuccess(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        final BmobFile bmobFile = new BmobFile(new File(str));
        bmobFile.uploadblock(getApplicationContext(), new UploadFileListener() {
            @Override
            public void onSuccess() {

                user.setHeader(bmobFile.getFileUrl(getApplicationContext()));
                saveHeader(bmobFile.getFileUrl(getApplicationContext()));
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
            public void onFailure(int i, String s) {

            }
        });
    }

    @Override
    public void onFail() {

    }
}
