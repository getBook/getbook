package com.xfzj.getbook.newnet;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by zj on 2016/6/17.
 */
public class ResponseConverterFactory extends Converter.Factory {
    protected static Gson gson;

    static {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public static final ResponseConverterFactory INSTANCE = new ResponseConverterFactory();

    public static ResponseConverterFactory create() {
        return INSTANCE;
    }
    @Override
    public Converter<okhttp3.ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            return new CardConverter<>(gson,type);
    }
}
