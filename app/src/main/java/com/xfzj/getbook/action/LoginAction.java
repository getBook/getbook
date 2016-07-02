package com.xfzj.getbook.action;

import android.app.ProgressDialog;
import android.content.Context;

import com.xfzj.getbook.BaseApplication;
import com.xfzj.getbook.R;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.newnet.ApiException;
import com.xfzj.getbook.newnet.GetFunApi;
import com.xfzj.getbook.newnet.NetRxWrap;
import com.xfzj.getbook.newnet.NormalSubscriber;
import com.xfzj.getbook.utils.AppAnalytics;
import com.xfzj.getbook.utils.SharedPreferencesUtils;

import rx.functions.Action1;

/**
 * Created by zj on 2016/1/28.
 */
public class LoginAction extends BaseAction {
    public static ProgressDialog setProgressDialog(Context context, String title, String message) {
        ProgressDialog pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setTitle(title);
        pd.setMessage(message);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        return pd;
    }

    public static void login(final boolean needPd, final Context context, final String sno, final String password, final CallBack callBack) {
        NetRxWrap.wrap(GetFunApi.login(sno, password)).doOnNext(new Action1<User>() {
            @Override
            public void call(User user) {
                user.setPassword(password);
                user.setUsername(sno);
                ((BaseApplication) context.getApplicationContext()).setUser(user);
                SharedPreferencesUtils.saveUser(context, user);

            }
        }).subscribe(new NormalSubscriber<User>(context, needPd, null, context.getString(R.string.logining), false) {
            @Override
            protected void onNextResult(User user) {
                AppAnalytics.onEvent(context, AppAnalytics.LOGIN_SUCCESS);
                if (null != callBack) {
                    callBack.onSuccess();
                }
            }

            @Override
            protected void onFail(ApiException ex) {
                AppAnalytics.onEvent(context, AppAnalytics.LOGIN_FAIL);
                if (null != callBack) {
                    callBack.onFail(ex);
                }
            }
        });
    }

    public interface CallBack {
        void onSuccess();

        void onFail(ApiException ex);
    }
}
