package com.xfzj.getbook.net;

import android.text.TextUtils;

import java.util.Map;

/**
 * Created by zj on 2016/1/28.
 */
public class HttpHelper implements IHttpHelper {
    private IHttpHelper iHttpHelper;

    public HttpHelper() {
        iHttpHelper = new HttpHelperImp();
    }


    public HttpHelper(HttpHelperImp httpHelperImp) {
        iHttpHelper = httpHelperImp;
    }

    @Override

    public byte[] DoConnection(String url) throws Exception {
        if (TextUtils.isEmpty(url)) {
            return iHttpHelper.NET_ERROR;
        }
        return iHttpHelper.DoConnection(url);
    }

    @Override
    public byte[] DoConnection(String url, String cookie) throws NetException, Exception {
        if (TextUtils.isEmpty(url)) {
            return iHttpHelper.NET_ERROR;
        }
        return iHttpHelper.DoConnection(url,cookie);
    }

    @Override
    public void setCookie(String cookie) {
        iHttpHelper.setCookie(cookie);
    }

    @Override
    public byte[] DoConnection(String url, int requestType, Map<String, String> params) throws Exception {
        if (TextUtils.isEmpty(url)) {
            return iHttpHelper.NET_ERROR;
        }
        return iHttpHelper.DoConnection(url, requestType, params);
    }

    @Override
    public byte[] DoConnectionJson(String url, int requestType, String json) throws NetException, Exception {
        if (TextUtils.isEmpty(url)) {
            return iHttpHelper.NET_ERROR;
        }
        return iHttpHelper.DoConnectionJson(url, requestType, json);
    }

    @Override
    public String getCookie() {
        return iHttpHelper.getCookie();
    }
    
}
