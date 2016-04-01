package com.xfzj.getbook.async;

import android.content.Context;

import com.xfzj.getbook.action.LoginAction;

/**
 * Created by zj on 2016/1/29.
 */
public class LoginAsync extends BaseAsyncTask<Integer, Integer, LoginAction.CallBack> {
    private Context context;
    private LoginAction loginAction;
    private String userName;
    private String password;
    private LoginAction.CallBack callBack;
    private String huaName;

   
    public LoginAsync(Context context, String userName, String password) {
        this(context, null, userName, password);
    }


    public LoginAsync(Context context, String huaName, String userName, String password) {
        super(context);
        this.userName = userName;
        this.password = password;
        this.context = context;
        this.huaName = huaName;
        loginAction = new LoginAction(context);
    }

    public void setCallback(LoginAction.CallBack callback) {
        this.callBack = callback;
    }

    @Override
    protected void onPost(LoginAction.CallBack callBack) {


    }

    @Override
    protected LoginAction.CallBack doExcute(Integer[] params) {
        return loginAction.loginAll(huaName, userName, password, callBack);
    }
}
