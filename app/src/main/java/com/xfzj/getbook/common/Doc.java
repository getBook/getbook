package com.xfzj.getbook.common;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by zj on 2016/6/7.
 */
public class Doc {
    @Expose
    private int total;
    @Expose
    private List<Paper> docs;

    public Doc(int total, List<Paper> docs) {
        this.total = total;
        this.docs = docs;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Paper> getDocs() {
        return docs;
    }

    public void setDocs(List<Paper> docs) {
        this.docs = docs;
    }
}
