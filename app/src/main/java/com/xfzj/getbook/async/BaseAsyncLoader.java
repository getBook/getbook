package com.xfzj.getbook.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.google.gson.Gson;

/**
 * Created by zj on 2016/3/21.
 */
public  abstract   class BaseAsyncLoader<T> extends AsyncTaskLoader<T> {
   
    private ProgressDialog pd;
    protected Context context;
    protected Gson gson;

    public BaseAsyncLoader(Context context) {
        super(context);
        this.context=context;


    }

    public void setProgressDialog(String title, String message) {
        setProgressDialog(title, message, false);
    }

    public void setProgressDialog(String title, String message, boolean isCanceled) {
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setTitle(title);
        pd.setMessage(message);
        pd.setCancelable(isCanceled);
        pd.setCanceledOnTouchOutside(isCanceled);
    }

    @Override
    public T loadInBackground() {
        return doInBackground();
    }

    protected abstract T doInBackground();

    @Override
    public void deliverResult(T data) {
        super.deliverResult(data);
        if (null != pd && pd.isShowing()) {
            pd.dismiss();
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (null != pd && !pd.isShowing()) {
            pd.show();
        }
//        forceLoad();
    }
}
