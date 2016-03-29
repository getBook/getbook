package com.xfzj.getbook.async;

import android.content.Context;
import android.text.TextUtils;

import com.xfzj.getbook.net.HttpHelper;
import com.xfzj.getbook.net.LibraryHttpImp;
import com.xfzj.getbook.utils.SharedPreferencesUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by zj on 2016/3/28.
 */
public abstract class BaseGetLibraryInfoAsyc<T> extends BaseAsyncTask<String, Void, T> {
    protected HttpHelper httpHelper;
    protected Document document;
    protected LibraryHttpImp libraryHttpImp;
    protected String cookie;
    public BaseGetLibraryInfoAsyc(Context context) {
        super(context);
    }

    @Override
    protected T doExcute(String[] params) {
        try {
            if (null == params) {
                return null;
            }
            if (TextUtils.isEmpty(params[0])) {
                return null;
            }
            cookie = SharedPreferencesUtils.getLibraryCookie(context);
            if (TextUtils.isEmpty(cookie)) {
                return null;
            }
            libraryHttpImp = new LibraryHttpImp();
            httpHelper = new HttpHelper(libraryHttpImp);
            byte[] bytes = httpHelper.DoConnection(params[0], cookie);
            String result = new String(bytes, "utf-8");
            document = Jsoup.parse(result);
            return parse(params,result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    protected abstract T parse(String[] params,String result);

    @Override
    protected void onPost(T t) {
        if (null != t) {
            if (null != onTaskListener) {
                onTaskListener.onSuccess(t);
            }
        }else {
            if (null != onTaskListener) {
                onTaskListener.onFail();
            }
        }
    }
}
