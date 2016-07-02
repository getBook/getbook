package com.xfzj.getbook.newnet;

/**
 * Created by zj on 2016/6/17.
 */
public class ResultException extends RuntimeException {

    private int errCode = 0;

    public ResultException(int errCode, String msg) {
        super(msg);
        this.errCode = errCode;
    }
    public ResultException(String msg) {
        super(msg);
    }
    public int getErrCode() {
        return errCode;
    }
}
