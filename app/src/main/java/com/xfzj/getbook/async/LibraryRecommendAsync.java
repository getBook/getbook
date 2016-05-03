package com.xfzj.getbook.async;

import android.content.Context;

/**获取图书馆荐购信息
 * Created by zj on 2016/5/1.
 */
public class LibraryRecommendAsync extends BaseGetLibraryInfoAsyc<String> {
    public LibraryRecommendAsync(Context context) {
        super(context);
    }

    @Override
    protected boolean needCookie() {
        return true;
    }

    @Override
    protected String parse(String[] params, String result) {
        if (result.contains("荐购成功")) {
            return "荐购成功";
        }
        return null;
    }

}
