package com.xfzj.getbook.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by zj on 2016/1/29.
 */
public abstract class BaseAsyncTask<K, V, T> extends AsyncTask<K, V, T> {
    private ProgressDialog pd;
    protected Context context;
    protected onTaskListener<T> onTaskListener;
    protected Gson gson;
    public BaseAsyncTask(Context context) {
        this.context = context;
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    public BaseAsyncTask() {
        
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

    public void setOnTaskListener(BaseAsyncTask.onTaskListener<T> onTaskListener) {
        this.onTaskListener = onTaskListener;
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

    
    public interface  onTaskListener<T>{
        void onSuccess(T t);

        void onFail();
    }
}
