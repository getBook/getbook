package com.xfzj.getbook.action;

import android.content.Context;
import android.text.TextUtils;

import com.xfzj.getbook.BaseApplication;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.net.HttpHelper;
import com.xfzj.getbook.net.IHttpHelper;
import com.xfzj.getbook.utils.MyLog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by zj on 2016/1/28.
 */
public class LoginAction extends BaseAction {

    public static final int SUCCESS = 0;
    public static final int Fail = 1;
    public static final int SUCCESS_SAMEUSER = 2;
    public static final int SUCCESS_UPDATE = 3;
    public static final int SUCCESS_REGISTER = 4;
    private User currUser;
    private User newUser;
    private Context context;
    private BaseApplication baseApplication;

    public LoginAction(Context context) {
        currUser = BmobUser.getCurrentUser(context, User.class);
        this.context = context;
        baseApplication = (BaseApplication) context.getApplicationContext();

    }

    public User getNewUser() {
        return newUser;
    }

    /**
     * 登陆学校系统以及bmob
     *
     * @param userName
     * @param password
     * @return
     */
    public CallBack loginAll(String userName, String password,CallBack callBack) {
        String msg = signIn(userName, password);
        if (TextUtils.isEmpty(msg)) {
            loginBmob(userName, password,callBack);
            return callBack;
        }
        if (hasUser()) {
            if (isSameUser()) {
                updateUserMsg();
                return callBack;
            } else {
                logOutBmob();
                registerBmob(newUser,callBack);
                return callBack;
            }
        } else {
            registerBmob(newUser, callBack);
            return callBack;
        }
    }

    public void loginBmob(String userName, String password, CallBack callBack) {
        User user = new User();
        user.setUsername(userName);
        user.setPassword(password);
        loginBmob(user, callBack);
    }

    /**
     * 登陆学校的系统，获取最新的msg
     *
     * @param account
     * @param password
     * @return user为空，说明未登陆成功
     */
    private String signIn(String account, String password) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("account", account);
            map.put("schoolCode", "nuist");
            map.put("signType", "SynSno");
            map.put("password", password);
            String result = new String(HttpHelper.getInstance().DoConnection(BaseHttp.SignInAndGetUserPlus, IHttpHelper.METHOD_POST, map), "utf-8");
            MyLog.print("sigin", result);
            JSONObject jsonObject = new JSONObject(result);
            boolean isSucc = jsonObject.getBoolean("success");
            if (isSucc) {
                String msg = jsonObject.getString("msg");
                this.newUser = gson.fromJson(jsonObject.getJSONObject("obj").toString(), User.class);
                this.newUser.setPassword(password);
                this.newUser.setUsername(account);
                this.newUser.setMsg(msg);
                MyLog.print("sigin", newUser.toString());
                return this.newUser.getMsg();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 是否是同一个用户
     *
     * @return
     */
    public boolean isSameUser() {
        if (null == newUser) {
            return false;
        }

        if (null == currUser) {
            return false;
        }
        MyLog.print("isSameUser", newUser.toString() + "\n" + currUser.toString());
        return newUser.equals(currUser);
    }

    /**
     * 当前是否存在用户
     *
     * @return
     */
    public boolean hasUser() {
        if (null == currUser) {
            return false;
        }
        return true;
    }

    /**
     * 更新用户的msg字段
     */
    private void updateUserMsg() {

        newUser.update(context, currUser.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                baseApplication.user = newUser;
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    /**
     * 退出账户
     */
    public void logOutBmob() {
        BmobUser.logOut(context);
        currUser = BmobUser.getCurrentUser(context, User.class);
        baseApplication.user = null;
    }

    /**
     * 注册bmob用户
     *
     * @param user
     */
    private void registerBmob(final User user, final CallBack callBack) {
        user.setEmail("sendi@163.com");
        user.signUp(context, new SaveListener() {
            @Override
            public void onSuccess() {
                MyLog.print("register", "onsuccess");
                loginBmob(user,callBack);
            }

            @Override
            public void onFailure(int i, String s) {
                //已经注册过了就进行登陆
                if (i == 202) {
                    loginBmob(user,callBack);
                }
            }
        });
    }

    /**
     * 登陆bmob
     */
    private void loginBmob(final User user, final CallBack callBack) {
        user.login(context, new SaveListener() {
            @Override
            public void onSuccess() {
                MyLog.print("loginBmob", "onSuccess");
                baseApplication.user = user;
                if (null != callBack) {
                    callBack.onSuccess();

                }
            }

            @Override
            public void onFailure(int i, String s) {
                MyLog.print("loginBmob", "onFailure"+s);
                if (null != callBack) {
                    callBack.onFail();
                    
                }
            }
        });
    }

    public interface CallBack {
        void onSuccess();

        void onFail();
    }
}
