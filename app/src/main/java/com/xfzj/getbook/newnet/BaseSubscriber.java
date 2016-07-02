package com.xfzj.getbook.newnet;

import android.content.Context;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.text.ParseException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by zj on 2016/6/23.
 */
public abstract class BaseSubscriber<T> extends Subscriber<T> implements ProgressBar.ProgressCancelListener {

    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;
    private boolean cancelable;
    private Context context;
    //出错提示
    private String networkMsg = "网络错误";
    private String parseMsg = "解析错误";
    private String unknownMsg = "未知错误";
    private boolean showPd;
    private ProgressBar pd;
    private String title;
    private String msg;


    protected BaseSubscriber(Context context, boolean showPd, String networkMsg, String parseMsg, String unknownMsg) {
        this.networkMsg = networkMsg;
        this.parseMsg = parseMsg;
        this.unknownMsg = unknownMsg;
        this.showPd = showPd;
        this.context = context;
    }

    public BaseSubscriber(Context context, String title, String msg, boolean cancelable) {
        this.showPd = true;
        this.context = context;
        this.title = title;
        this.msg = msg;
        this.cancelable = cancelable;
    }
    public BaseSubscriber(Context context,  boolean showPd,String title, String msg, boolean cancelable) {
        this.showPd = showPd;
        this.context = context;
        this.title = title;
        this.msg = msg;
        this.cancelable = cancelable;
    }
    public BaseSubscriber() {
        this.showPd = false;
    }

    @Override
    public void onStart() {
        super.onStart();
        showProgressDialog();
    }

    private void showProgressDialog() {
        if (showPd) {
            if (null == pd) {
                pd = new ProgressBar(context, this, title, msg, cancelable);
                pd.obtainMessage(ProgressBar.SHOW_PROGRESS).sendToTarget();
            }
        }
    }

    public void disMissProgressDialog() {
        if (pd != null) {
            pd.obtainMessage(ProgressBar.DISMISS_PROGRESS).sendToTarget();
            pd = null;
        }
    }

    @Override
    public void onProgressCanceled() {//取消请求
        if (!this.isUnsubscribed()) {
            unsubscribe();
        }
    }

    @Override
    public void onError(Throwable e) {
        disMissProgressDialog();
        Throwable throwable = e;
        e.printStackTrace();
        //获取最根源的异常
        while (throwable.getCause() != null) {
            e = throwable;
            throwable = throwable.getCause();
        }

        ApiException ex;
        if (e instanceof HttpException) {             //HTTP错误
            HttpException httpException = (HttpException) e;
            ex = new ApiException(e, httpException.code());
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                    onPermissionError(ex);          //权限错误，需要实现
                    break;
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex.setDisplayMessage(networkMsg);  //均视为网络错误
                    onError(ex);
                    break;
            }
        } else if (e instanceof ResultException) {    //服务器返回的错误
            ResultException resultException = (ResultException) e;
            ex = new ApiException(resultException, resultException.getMessage());
            onResultError(ex);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ApiException(e, ApiException.PARSE_ERROR);
            ex.setDisplayMessage(parseMsg);            //均视为解析错误
            onError(ex);
        } else {
            ex = new ApiException(e, ApiException.UNKNOWN);
            ex.setDisplayMessage(unknownMsg);          //未知错误
            onError(ex);
        }
    }

    @Override
    public void onNext(T t) {
        disMissProgressDialog();
        onNextResult(t);
    }

    /**
     * onNext的回调
     *
     * @param t
     */
    protected abstract void onNextResult(T t);


    /**
     * 错误回调
     */
    protected abstract void onError(ApiException ex);

    /**
     * 权限错误，需要实现重新登录操作
     */
    protected abstract void onPermissionError(ApiException ex);

    /**
     * 服务器返回的错误
     */
    protected abstract void onResultError(ApiException ex);

    @Override
    public void onCompleted() {
        disMissProgressDialog();
    }

}
