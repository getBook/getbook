package com.xfzj.getbook.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by zj on 2016/3/29.
 */
public class InputMethodManagerUtils {
    public static void hide(Context context,View v) {
        InputMethodManager ipm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        ipm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
}
