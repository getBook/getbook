package com.xfzj.getbook.newnet;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by zj on 2016/6/17.
 */
public class CardConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

   public  CardConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }
    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
            ResultResponse resultResponse = gson.fromJson(response, ResultResponse.class);
            if (resultResponse.isSuccess()) {
                return gson.fromJson(resultResponse.getObj(), type);
            } else {
                throw new ResultException(resultResponse.getMsg());
            }
    }
}
