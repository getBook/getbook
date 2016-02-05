package com.xfzj.getbook.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by zj on 2016/1/28.
 */
public class BaseAction {
    protected static Gson gson;

    static {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }
    
}
