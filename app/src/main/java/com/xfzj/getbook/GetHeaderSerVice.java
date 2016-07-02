package com.xfzj.getbook;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.xfzj.getbook.action.QueryAction;
import com.xfzj.getbook.action.UploadAction;
import com.xfzj.getbook.async.BaseAsyncTask;
import com.xfzj.getbook.async.UserHeadAsync;
import com.xfzj.getbook.common.User;

import cn.bmob.v3.datatype.BmobFile;

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
            QueryAction queryAction=new QueryAction(getApplicationContext());
            queryAction.queryUserSelf(user);
            queryAction.setOnQueryListener(new QueryAction.OnQueryListener<User>() {
                @Override
                public void onSuccess(User user) {
                    GetHeaderSerVice.this.user = user;
                    BmobFile header = user.getBmobHeader();
                    if (null == header) {
                        UserHeadAsync userHeadAsync = new UserHeadAsync(getApplicationContext(), user.getSno());
                        userHeadAsync.execute();
                        userHeadAsync.setOnTaskListener(GetHeaderSerVice.this);
                    } else  {
                        UploadAction.saveHeader(getApplicationContext(), header.getFileUrl(getApplicationContext()));
                    }
                }

                @Override
                public void onFail() {

                }
            });
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onSuccess(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        UploadAction uploadAction = new UploadAction();
//        uploadAction.uploadHeader(getApplicationContext(), user, str);
    }

    @Override
    public void onFail() {

    }
}
