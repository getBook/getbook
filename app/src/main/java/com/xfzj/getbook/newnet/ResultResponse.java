package com.xfzj.getbook.newnet;

import com.google.gson.JsonElement;

/**
 * Created by zj on 2016/6/17.
 */
public class ResultResponse {
    private boolean success;
    
    private String msg;
    
    private JsonElement obj;
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JsonElement getObj() {
        return obj;
    }

    public void setObj(JsonElement obj) {
        this.obj = obj;
    }
}
