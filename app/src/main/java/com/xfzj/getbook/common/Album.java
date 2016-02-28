package com.xfzj.getbook.common;

/**
 * Created by zj on 2016/2/26.
 */
public class Album {
    private String name;
    private int count;

    public Album(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}