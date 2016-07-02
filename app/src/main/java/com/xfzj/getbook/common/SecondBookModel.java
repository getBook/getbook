package com.xfzj.getbook.common;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zj on 2016/6/24.
 */
public class SecondBookModel {
    private int total;
    @SerializedName("books")
    private List<SecondBook> secondBooks;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<SecondBook> getSecondBooks() {
        return secondBooks;
    }

    public void setSecondBooks(List<SecondBook> secondBooks) {
        this.secondBooks = secondBooks;
    }
}
