package com.xfzj.getbook.common;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zj on 2016/6/24.
 */
public class DebrisModel {
    private int total;
    @SerializedName("goods")
    private List<Debris> debrises;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Debris> getDebrises() {
        return debrises;
    }

    public void setDebrises(List<Debris> debrises) {
        this.debrises = debrises;
    }
}
