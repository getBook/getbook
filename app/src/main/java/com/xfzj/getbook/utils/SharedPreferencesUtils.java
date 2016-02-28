package com.xfzj.getbook.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by zj on 2016/1/29.
 */
public class SharedPreferencesUtils {
    private static final String USERPREFERENCES = "user";
    private static final String USERNAME = "username";
    private static final String USERPASSWORD = "userpassword";
    
    public static void saveUser(Context context, String username, String password) {
        SharedPreferences sp = context.getSharedPreferences(USERPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (!TextUtils.isEmpty(username)) {
            editor.putString(USERNAME, username);
        }
        if (!TextUtils.isEmpty(password)) {
            editor.putString(USERPASSWORD, password);
        }
        editor.commit();
    }

    public static String getUserPassword(Context context, String username) {
        SharedPreferences sp = context.getSharedPreferences(USERPREFERENCES, Context.MODE_PRIVATE);
        String password = sp.getString(USERPASSWORD, "");
        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(username) && username.equals(sp.getString(USERNAME, ""))) {
            return password;
        }
        return ""; 
        
    }
    
    
}
