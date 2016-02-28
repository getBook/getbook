package com.xfzj.getbook.common;

import java.io.Serializable;

/**
 * Created by zj on 2016/2/28.
 */
public class PicPath implements Serializable {
    public static final int FLAG_CAPTURE = 1;
    public static final int FLAG_ALBUM = 0;
    private int flag;
    private String path;

    public PicPath(int flag, String path) {
        this.flag = flag;
        this.path = path;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
