package com.xfzj.getbook.net;

import java.util.Map;

/**
 * Created by zj on 2016/1/28.
 */
public interface IHttpHelper {
    int METHOD_POST = 0;
    int METHOD_GET = 1;
    byte[] NET_ERROR = "网络错误，请稍后再试".getBytes();//网络错误
    byte[] SYS_ERROR = "系统异常，请重试".getBytes();//系统异常错误
    byte[] DoConnection(String url) throws NetException, Exception;

    byte[] DoConnection(String url, String cookie)throws NetException, Exception;

    void setCookie(String cookie);

    byte[] DoConnection(String url,int requestType, Map<String, String> params) throws NetException, Exception;
    byte[] DoConnectionJson(String url,int requestType, String json) throws NetException, Exception;
    String getCookie();
    
}
