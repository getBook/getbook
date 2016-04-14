package com.xfzj.getbook.net;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zj on 2016/3/29.
 */
public class BmobHttpImp extends HttpHelperImp {

    public static final String Get_THUMBNAIL = "https://api.bmob.cn/1/images/thumbnail";

    @Override
    protected HttpURLConnection getHttpURLConnection(URL url) throws Exception {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("X-Bmob-Application-Id", "953b4c2054c0d44e168d6725f8df4ff7");
        urlConnection.setRequestProperty("X-Bmob-REST-API-Key", "21e2564646604f5c5eec7cc27c995508");
        urlConnection.setRequestProperty("Content-Type", "application/json");
        return urlConnection;
    }
}
