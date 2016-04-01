package com.xfzj.getbook.utils;

import android.util.Log;

/**
 * Created by zj on 2016/1/28.
 */
public class MyLog {
    public static final boolean ISDEBUG = true;

    public static void print(String tag, String message) {
        if (ISDEBUG) {
            Log.i("aaa", tag + " " + message);
        }
    }
}
