package com.xfzj.getbook.common;

import com.bmob.BmobProFile;

import cn.bmob.v3.BmobObject;

/**
 * Created by zj on 2016/2/5.
 */
public class SecondBook extends BmobObject {
    private User user;
    private String isbn;
    private String bookName;
    private String publish;
    private String originPrice;
    private String discount;
    private String newold;
    private Integer count;
    private BmobProFile pictures;
    private String tips;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public String getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(String originPrice) {
        this.originPrice = originPrice;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getNewold() {
        return newold;
    }

    public void setNewold(String newold) {
        this.newold = newold;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BmobProFile getPictures() {
        return pictures;
    }

    public void setPictures(BmobProFile pictures) {
        this.pictures = pictures;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
