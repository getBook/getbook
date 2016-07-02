package com.xfzj.getbook.newnet;

import android.content.Context;

/**
 * Created by zj on 2016/6/24.
 */
public abstract class NormalSubscriber<T> extends BaseSubscriber<T> {
    @Override
    protected void onError(ApiException ex) {
        onFail(ex);
    }

    @Override
    protected void onPermissionError(ApiException ex) {
        onFail(ex);
    }

    @Override
    protected void onResultError(ApiException ex) {
        onFail(ex);
    }


    protected abstract void onFail(ApiException ex);

    public NormalSubscriber(Context context, boolean showPd, String networkMsg, String parseMsg, String unknownMsg) {
        super(context, showPd, networkMsg, parseMsg, unknownMsg);
    }

    public NormalSubscriber(Context context, String title, String msg, boolean cancelable) {
        super(context, title, msg, cancelable);
    }

    public NormalSubscriber(Context context, boolean showPd, String title, String msg, boolean cancelable) {
        super(context, showPd, title, msg, cancelable);
    }

    public NormalSubscriber() {
    }
}
