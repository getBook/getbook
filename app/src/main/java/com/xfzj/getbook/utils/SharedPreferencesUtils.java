package com.xfzj.getbook.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.xfzj.getbook.BaseApplication;
import com.xfzj.getbook.common.LibraryInfo;
import com.xfzj.getbook.common.Post;
import com.xfzj.getbook.common.User;
import com.xfzj.getbook.views.view.NavigationHeaderView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by zj on 2016/1/29.
 */
public class SharedPreferencesUtils {
    private static final String USERPREFERENCES = "user";
    private static final String USERNAME = "username";
    private static final String USERPASSWORD = "userpassword";
    private static final String GENDER = "gender";
    private static final String CARDNO = "cardno";
    private static final String MSG = "msg";
    private static final String HEADER = "header";
    private static final String NAME = "name";
    private static final String SNO = "sno";
    private static final String HUANAME = "huaname";
    private static final String ID = "Id";
    private static final String LIBRARYUSERINFOPREFERENCES = "libraryuserinfo";
    private static final String ACCOUNT = "account";
    private static final String COOKIE = "cookie";
    private static final String PHONE = "phone";
    private static final String OBJECTID = "objectId";
    private static final String EMAIL = "email";
    private static final String KEYBOARDHEIGHT = "keyboardHeight";
    private static final String SETTINGS = "settings";
    private static final String CARDUSE = "carduse";
    private static final String FOCUSPOST = "focuspost";
    private static final String FOCUSPOSTID = "focuspostid";
    private static final String SHARESETTING = "sharesetting";
    private static final String SHARESETTINGCOUNT = "sharesettingcount";
    private static final String SHARESETTINGB = "sharesettingb";

    public static void saveUser(Context context, User user) {
        if (null == user) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(USERPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (!TextUtils.isEmpty(user.getUsername())) {
            editor.putString(USERNAME, user.getUsername());
        }
        String password = getPassword(user);
        if (!TextUtils.isEmpty(password)) {
            editor.putString(USERPASSWORD, password);
        }
        if (!TextUtils.isEmpty(user.getCardno())) {
            editor.putString(CARDNO, user.getCardno());
        }
        if (!TextUtils.isEmpty(user.getMsg())) {
            editor.putString(MSG, user.getMsg());
        }
        if (!TextUtils.isEmpty(user.getName())) {
            editor.putString(NAME, user.getName());
        }
        if (!TextUtils.isEmpty(user.getSno())) {
            editor.putString(SNO, user.getSno());
        }
        if (user.getId() != 0) {
            editor.putInt(ID, user.getId());
        }
        editor.putBoolean(GENDER, user.isGender());
        if (!TextUtils.isEmpty(user.getHuaName())) {
            editor.putString(HUANAME, user.getHuaName());
        }
        if (!TextUtils.isEmpty(user.getObjectId())) {
            editor.putString(OBJECTID, user.getObjectId());
        }
        if (!TextUtils.isEmpty(user.getEmail())) {
            editor.putString(EMAIL, user.getEmail());
        }
        editor.apply();
    }

    /**
     * 存储键盘高度
     *
     * @param context
     * @param height
     */
    public static void saveSoftKeyBoard(Context context, int height) {
        SharedPreferences sp = context.getSharedPreferences(KEYBOARDHEIGHT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (height > 0) {
            editor.putInt(KEYBOARDHEIGHT, height);
            editor.apply();
        }
    }

    /**
     * 获取键盘高度
     *
     * @param context
     * @return
     */
    public static int getSoftKeyBoard(Context context) {
        SharedPreferences sp = context.getSharedPreferences(KEYBOARDHEIGHT, Context.MODE_PRIVATE);
        return sp.getInt(KEYBOARDHEIGHT, 787);
    }

    /**
     * 一卡通功能是否第一次使用
     *
     * @param context
     * @return
     */
    public static boolean CardFirstUse(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        boolean b = sp.getBoolean(CARDUSE, true);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(CARDUSE, false);
        editor.apply();
        return b;
    }

    /**
     * 保存用户关心的帖子
     *
     * @param context
     * @param postId
     * @param count
     */
    public static void saveFocusPost(Context context, String postId, int count) {
        saveFocusPostId(context, postId);
        BaseApplication baseApplication = ((BaseApplication) context.getApplicationContext());
        if (null != baseApplication) {
            User user = baseApplication.getUser();
            if (null != user) {
                SharedPreferences sp = context.getSharedPreferences(FOCUSPOST + user.getObjectId(), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt(postId, count);
                editor.apply();
            }
        }


    }

    private static void saveFocusPostId(Context context, String postId) {
        BaseApplication baseApplication = ((BaseApplication) context.getApplicationContext());
        if (null != baseApplication) {
            User user = baseApplication.getUser();
            if (null != user) {
                SharedPreferences sp = context.getSharedPreferences(FOCUSPOSTID + user.getObjectId(), Context.MODE_PRIVATE);
                String temp = sp.getString(FOCUSPOST, "");
                if (!TextUtils.isEmpty(temp) && temp.contains(postId)) {
                    return;
                }
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(FOCUSPOST, temp + postId + ",");
                editor.apply();
            }
        }
    }

    public static List<Post> getFocusPostId(Context context) {
        BaseApplication baseApplication = ((BaseApplication) context.getApplicationContext());
        if (null == baseApplication) {
            return null;
        }
        User user = baseApplication.getUser();
        if (null == user) {
            return null;
        }
        List<Post> posts = new ArrayList<>();
        SharedPreferences sp = context.getSharedPreferences(FOCUSPOSTID + user.getObjectId(), Context.MODE_PRIVATE);
        MyLog.print(sp.getString(FOCUSPOST, null), "asd");
        String s = sp.getString(FOCUSPOST, null);
        if (TextUtils.isEmpty(s)) {
            return null;
        }
        String[] id = s.split(",");
        for (String str : id) {
            posts.add(new Post(str));
        }
        return posts;

    }

    /**
     * 用户关心的帖子是否更新了
     *
     * @param context
     * @param postId
     * @param count
     * @return
     */
    public static int isFocusPostUpdate(Context context, String postId, int count) {

        BaseApplication baseApplication = ((BaseApplication) context.getApplicationContext());
        if (null == baseApplication) {
            return 0;
        }
        User user = baseApplication.getUser();
        if (null == user) {
            return 0;
        }
        SharedPreferences sp = context.getSharedPreferences(FOCUSPOST + user.getObjectId(), Context.MODE_PRIVATE);
        int a = sp.getInt(postId, -1);
        if (a == -1) {
            return 0;
        }
        return count - a;
    }


    public static void saveLibraryLoginInfo(Context context, LibraryInfo libraryInfo) {
        if (null == libraryInfo) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(LIBRARYUSERINFOPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String account = libraryInfo.getAccount();
        if (!TextUtils.isEmpty(account)) {
            editor.putString(ACCOUNT, account);
        }
        String password = libraryInfo.getPassword();
        if (!TextUtils.isEmpty(password)) {
            editor.putString(USERPASSWORD, password);
        }
        String cookie = libraryInfo.getCookie();
        if (!TextUtils.isEmpty(cookie)) {
            editor.putString(COOKIE, cookie);
        }
        editor.apply();
    }

    public static LibraryInfo getLibraryLoginInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences(LIBRARYUSERINFOPREFERENCES, Context.MODE_PRIVATE);
        return new LibraryInfo(sp.getString(ACCOUNT, ""), sp.getString(USERPASSWORD, ""), sp.getString(COOKIE, ""));

    }

    public static String getLibraryCookie(Context context) {
        SharedPreferences sp = context.getSharedPreferences(LIBRARYUSERINFOPREFERENCES, Context.MODE_PRIVATE);
        return sp.getString(COOKIE, "");

    }


    public static User getUser(Context context) {
        SharedPreferences sp = context.getSharedPreferences(USERPREFERENCES, Context.MODE_PRIVATE);
        String userName = sp.getString(USERNAME, "");
        String cardno = sp.getString(CARDNO, "");
        String msg = sp.getString(MSG, "");
        String sno = sp.getString(SNO, "");
        boolean gender = sp.getBoolean(GENDER, false);
        String huaName = sp.getString(HUANAME, "");
        String name = sp.getString(NAME, "");
        int id = sp.getInt(ID, 0);
        String objectId = sp.getString(OBJECTID, "");
        String email = sp.getString(EMAIL, "");
        return new User(name, gender, huaName, sno, cardno, msg, id, objectId, email);
    }

    public static void saveUserHeader(Context context, String header) {
        SharedPreferences sp = context.getSharedPreferences(USERPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (!TextUtils.isEmpty(header)) {
            editor.putString(HEADER, header);
        }
        editor.apply();
    }


    public static String getUserPassword(Context context, String username) {
        SharedPreferences sp = context.getSharedPreferences(USERPREFERENCES, Context.MODE_PRIVATE);
        String password = sp.getString(USERPASSWORD, "");
        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(username) && username.equals(sp.getString(USERNAME, ""))) {
            return password;
        }
        return "";

    }

    public static void saveHuaName(Context context, String huaName) {
        SharedPreferences sp = context.getSharedPreferences(USERPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (!TextUtils.isEmpty(huaName)) {
            editor.putString(HUANAME, huaName);
        }
        editor.apply();
        context.sendBroadcast(new Intent(NavigationHeaderView.ACTION_RECEIVE_HUANAME));

    }

    public static String getHuaName(Context context) {
        SharedPreferences sp = context.getSharedPreferences(USERPREFERENCES, Context.MODE_PRIVATE);
        return sp.getString(HUANAME, "");
    }

    public static String getUserHeader(Context context) {
        SharedPreferences sp = context.getSharedPreferences(USERPREFERENCES, Context.MODE_PRIVATE);
        return sp.getString(HEADER, "");
    }

    private static String getPassword(User user) {
        String password = "";

        try {
            Field field = user.getClass().getSuperclass().getDeclaredField("password");
            field.setAccessible(true);
            password = (String) field.get((BmobUser) user);
        } catch (Exception e) {

        }
        return password;
    }

    public static void clearUser(Context context) {
        clearCard(context);
        clearLibrary(context);
    }

    public static void clearCard(Context context) {
        SharedPreferences sp = context.getSharedPreferences(USERPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public static void clearLibrary(Context context) {
        SharedPreferences sp = context.getSharedPreferences(LIBRARYUSERINFOPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public static void updateMsg(Context context, String msg) {
        SharedPreferences sp = context.getSharedPreferences(USERPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (!TextUtils.isEmpty(msg)) {
            editor.putString(MSG, msg);
        }
        editor.apply();
    }

    public static void savePhoneNumber(Context context, String phone) {
        SharedPreferences sp = context.getSharedPreferences(PHONE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (!TextUtils.isEmpty(phone)) {
            editor.putString(PHONE, phone);
        }
        editor.apply();
    }

    public static String getPhoneNumber(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PHONE, Context.MODE_PRIVATE);
        return sp.getString(PHONE, "");
    }

    public static boolean isShowShare(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARESETTING, Context.MODE_PRIVATE);
        int count = sp.getInt(SHARESETTINGCOUNT, 0);
        boolean b = sp.getBoolean(SHARESETTINGB, false);
        if (b) {
            return false;
        }
        SharedPreferences.Editor editor = sp.edit();
        if (count == 2 && !b) {
            editor.putBoolean(SHARESETTINGB, true);
            editor.apply();
            return true;
        }
        editor.putInt(SHARESETTINGCOUNT, ++count);
        editor.apply();
        return false;
    }

}
