package com.xfzj.getbook.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zj on 2016/1/28.
 */
public class MyToast {
    public static void show(Context context,String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
