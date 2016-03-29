package com.xfzj.getbook.common;

import com.google.gson.annotations.Expose;

/**
 * Created by zj on 2016/3/27.
 */
public class Bill {
    @Expose
    private String K;
    @Expose
    private String V;


    public Bill() {
    }

    public Bill(String k, String v) {
        K = k;
        V = v;
    }

    public String getK() {
        return K;
    }

    public void setK(String k) {
        K = k;
    }

    public String getV() {
        return V;
    }

    public void setV(String v) {
        V = v;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "K='" + K + '\'' +
                ", V='" + V + '\'' +
                '}';
    }
}
