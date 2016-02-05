package com.xfzj.getbook.net;

import android.text.TextUtils;

import java.util.Map;

/**
 * Created by zj on 2016/1/28.
 */
public class HttpHelper implements IHttpHelper {
    private IHttpHelper iHttpHelper;
    private static HttpHelper httpHelper;

    private HttpHelper() {
        iHttpHelper = new HttpHelperImp();
    }

    public static HttpHelper getInstance() {
        if (null == httpHelper) {
            httpHelper = new HttpHelper();
        }
        return httpHelper;
    }

    @Override

    public byte[] DoConnection(String url) {
        if (TextUtils.isEmpty(url)) {
            return iHttpHelper.NET_ERROR;
        }
        return iHttpHelper.DoConnection(url);
    }

    @Override
    public byte[] DoConnection(String url, int requestType, Map<String, String> params) {
        if (TextUtils.isEmpty(url)) {
            return iHttpHelper.NET_ERROR;
        }
        return iHttpHelper.DoConnection(url, requestType, params);
    }


}
