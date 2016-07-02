package com.xfzj.getbook.newnet;

/**
 * Created by zj on 2016/6/23.
 */
public class ApiException extends Exception {

    private  int code;
    private String displayMessage;

    public static final int UNKNOWN = 1000;
    public static final int PARSE_ERROR = 1001;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }
    public ApiException(Throwable throwable, String msg) {
        super(throwable);
        this.displayMessage=msg;
    }
    public int getCode() {
        return code;
    }
    public String getDisplayMessage() {
        return displayMessage;
    }
    public void setDisplayMessage(String msg) {
        this.displayMessage = msg ;
    }
}
