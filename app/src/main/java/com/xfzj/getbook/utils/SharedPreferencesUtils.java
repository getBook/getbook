package com.xfzj.getbook.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.xfzj.getbook.common.User;

import java.lang.reflect.Field;

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

    public static void saveUser(Context context, User user) {

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
        if (user.getId()!=0) {
            editor.putInt(ID, user.getId());
        }
        editor.putBoolean(GENDER, user.isGender());
        if (!TextUtils.isEmpty(user.getHuaName())) {
            editor.putString(HUANAME, user.getHuaName());
        }
        editor.apply();
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
        int id= sp.getInt(ID, 0);
        return new User(name, gender, huaName, sno, cardno, msg,id);
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
        SharedPreferences sp = context.getSharedPreferences(USERPREFERENCES, Context.MODE_PRIVATE);
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
}
