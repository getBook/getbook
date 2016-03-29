package com.xfzj.getbook.net;

import android.text.TextUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by zj on 2016/3/29.
 */
public class LibraryHttpImp extends HttpHelperImp {
    public static final String INDEX = "http://lib.nuist.edu.cn/portal/index.aspx";
    public static final String LOGIN = "http://lib2.nuist.edu.cn/reader/login.php";
 private Map<String,String> map;
    @Override
    protected HttpURLConnection getHttpURLConnection(URL url) throws Exception {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Host", " lib2.nuist.edu.cn");
        urlConnection.setRequestProperty("Accept", "text / html, application / xhtml + xml, application / xml;q = 0.9, image / webp,*/*;q=0.8");
        urlConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.108 Safari/537.36");
        urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
        urlConnection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
        if (!TextUtils.isEmpty(cookie)) {
            urlConnection.setRequestProperty("Cookie", cookie);
        }
        if(null!=map) {
            Set<Map.Entry<String, String>> set = map.entrySet();
            for (Map.Entry<String, String> s : set) {
                urlConnection.setRequestProperty(s.getKey(), s.getValue());
            }
        }
        
        return urlConnection;
    }

  

    public void setProperty(String k, String v) {
        if (null == map) {
            map = new HashMap<>();
        }
        if (!map.containsKey(k)) {
            map.put(k, v);
        }
        

    }
}
