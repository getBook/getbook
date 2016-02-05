package com.xfzj.getbook;

import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.net.HttpHelper;
import com.xfzj.getbook.net.IHttpHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zj on 2016/1/28.
 */
public class HttpDemo {
    public static byte[] excute() {


        BaseHttp baseHttp = new BaseHttp();
        String url = baseHttp.SignInAndGetUserPlus;
        HttpHelper httpHelper = HttpHelper.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put("account", "20121314024");
        map.put("schoolCode", "nuist");
        map.put("signType", "SynSno");
        try {
            String str =null;
//            = AryConversion.binary2Hex(Des.encrypt(URLEncoder.encode("064012", "utf-8"), "synjones")).toUpperCase();
            map.put("password", str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return httpHelper.DoConnection(url, IHttpHelper.METHOD_POST, map);
    }
}

