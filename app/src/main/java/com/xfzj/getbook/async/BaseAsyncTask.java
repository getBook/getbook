package com.xfzj.getbook.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by zj on 2016/1/29.
 */
public abstract class BaseAsyncTask<K, V, T> extends AsyncTask<K, V, T> {
    private ProgressDialog pd;
    private Context context;

    public BaseAsyncTask(Context context) {
        this.context = context;
    }
    public void setProgressDialog(String title,String message) {
        setProgressDialog(title, message, false);
    }
    public void setProgressDialog(String title,String message,boolean isCanceled) {
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setTitle(title);
        pd.setMessage(message);
        pd.setCancelable(isCanceled);
        pd.setCanceledOnTouchOutside(isCanceled);
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (null != pd && !pd.isShowing()) {
            pd.show();
        }
    }

    @Override
    protected void onPostExecute(T t) {
        super.onPostExecute(t);
        if (null != pd && pd.isShowing()) {
            pd.dismiss();
        }
        onPost(t);
        
    }

    /**
     * 更新UI操作
     * @param t
     */
    protected abstract void onPost(T t);

    @Override
    protected void onProgressUpdate(V... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(T t) {
        super.onCancelled(t);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected T doInBackground(K... params) {
        return doExcute(params);
    }
    
    /**
     * 真正执行耗时操作的方法
     * @param params
     */
    protected abstract T doExcute(K[] params);

}
