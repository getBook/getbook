package com.xfzj.getbook.async;

import android.content.Context;

import com.xfzj.getbook.action.LoginAction;

/**
 * Created by zj on 2016/1/29.
 */
public class LoginAsync extends BaseAsyncTask<Integer, Integer,LoginAction.CallBack > {
    private Context context;
    private LoginAction loginAction;
    private String userName;
    private String password;
    private LoginAction.CallBack callBack;

    public LoginAsync(Context context, String userName, String password, String title, String message) {
        super(context);
        this.userName = userName;
        this.password = password;
        this.context = context;
        loginAction = new LoginAction(context);
        setProgressDialog(title, message);
    }

    public void setCallback(LoginAction.CallBack callback) {
        this.callBack = callback;
    }

    @Override
    protected void onPost(LoginAction.CallBack  callBack) {
          
        
    }
    @Override
    protected LoginAction.CallBack  doExcute(Integer[] params) {
      return  loginAction.loginAll(userName, password, callBack);
    }
}
