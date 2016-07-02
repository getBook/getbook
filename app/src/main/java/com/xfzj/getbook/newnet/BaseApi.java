package com.xfzj.getbook.newnet;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xfzj.getbook.BaseApplication;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by zj on 2016/6/16.
 */
public abstract class BaseApi {
    public static final String CARD_API_SERVER = "http://ucard.nuist.edu.cn:8070/Api";
    public static final String GETFUN_API_SERVER = "http://getfun-api.alpha.duohuo.org";
    private static OkHttpClient mOkHttpClient;
    private static Retrofit mRetrofit;
    protected static Gson gson ;
    protected static Retrofit getRetrofit(String apiService) {
        gson = new GsonBuilder().setPrettyPrinting().create();
        if (mRetrofit == null) {
            Context context = BaseApplication.newInstance();
            //设定30秒超时
         
         OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    //设置拦截器，以用于自定义Cookies的设置
//                    .addNetworkInterceptor(new CookiesInterceptor(context))
                    //设置缓存目录
                    .cache(new Cache(new File(context.getCacheDir()
                            .getAbsolutePath(), "HttpCache"), 20 * 1024 * 1024)).connectTimeout(30, TimeUnit.SECONDS);
            if (GETFUN_API_SERVER.equals(apiService)) {
                builder.addInterceptor(new CookiesInterceptor(context));
            }
            mOkHttpClient = builder.build();
            //构建Retrofit
            mRetrofit = new Retrofit.Builder()
                    //配置服务器路径
                    .baseUrl(apiService + "/")
                    //设置日期解析格式，这样可以直接解析Date类型
//                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    //配置转化库，默认是Gson
                    .addConverterFactory(ResponseConverterFactory.create())
                    //配置回调库，采用RxJava
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    //设置OKHttpClient为网络客户端
                    .client(mOkHttpClient)
                    .build();

        }
        return mRetrofit;
    }
}
