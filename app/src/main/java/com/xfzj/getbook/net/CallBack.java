package com.xfzj.getbook.net;

/**
 * Created by zj on 2016/1/28.
 */
public interface CallBack {
    void onSuccess(String data);

    void onFail(int error);
}
