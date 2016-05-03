package com.xfzj.getbook.async;

import android.content.Context;

/**
 * Created by zj on 2016/5/1.
 */
public class RenewBookAsync extends BaseGetLibraryInfoAsyc<String> {
    public RenewBookAsync(Context context) {
        super(context);
    }

    @Override
    protected boolean needCookie() {
        return true;
    }

    @Override
    protected String parse(String[] params, String result) {
        return result;
    }

}
