package com.xfzj.getbook.newnet;

import android.content.Context;

import com.xfzj.getbook.utils.SharedPreferencesUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;

/**
 * Created by zj on 2016/6/16.
 */
public class CookiesInterceptor implements Interceptor {
    private Context context;

    public CookiesInterceptor(Context context) {
        this.context = context;
    }

    //重写拦截方法，处理自定义的Cookies信息
    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        okhttp3.Request request = chain.request();
        okhttp3.Request compressedRequest = request.newBuilder()
                .header("cookie", SharedPreferencesUtils.getGetFunCookie(context)
                )
                .build();
        okhttp3.Response response = chain.proceed(compressedRequest);
        List<String> cookies = response.headers().values("Set-Cookie");
        if (null != cookies && cookies.size() > 1) {
            SharedPreferencesUtils.saveGetFunCookie(context, cookies.get(0)+"; "+cookies.get(1));
        }
        return response;
    }
}