package com.xfzj.getbook.common;

import java.util.Arrays;

import cn.bmob.v3.BmobObject;

/**
 * Created by zj on 2016/2/5.
 */
public class SecondBook extends BmobObject {
    private User user;
    private BookInfo bookInfo;
    private String discount;
    private String newold;
    private Integer count;
    private String[] pictures;
    private String tips;
    private String telePhone;


    public SecondBook() {
        
    }
    public SecondBook(User user, BookInfo bookInfo, String discount, String newold, Integer count, String[] pictures, String tips, String telePhone) {
        this.user = user;
        this.bookInfo = bookInfo;
        this.discount = discount;
        this.newold = newold;
        this.count = count;
        this.pictures = pictures;
        this.tips = tips;
        this.telePhone = telePhone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
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

    public String[] getPictures() {
        return pictures;
    }

    public void setPictures(String[] pictures) {
        this.pictures = pictures;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getTelePhone() {
        return telePhone;
    }

    public void setTelePhone(String telePhone) {
        this.telePhone = telePhone;
    }

    @Override
    public String toString() {
        return "SecondBook{" +
                "user=" + user +
                ", bookInfo=" + bookInfo +
                ", discount='" + discount + '\'' +
                ", newold='" + newold + '\'' +
                ", count=" + count +
                ", pictures=" + Arrays.toString(pictures) +
                ", tips='" + tips + '\'' +
                ", telePhone='" + telePhone + '\'' +
                '}';
    }
}
