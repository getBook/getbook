package com.xfzj.getbook.newnet;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zj on 2016/6/25.
 */
public class NetRxWrap {
    public static <T> Observable<T> wrap(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).throttleFirst(1, TimeUnit.SECONDS) ;
    }
}
