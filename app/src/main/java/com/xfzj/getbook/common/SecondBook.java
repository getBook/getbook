package com.xfzj.getbook.common;

import cn.bmob.v3.BmobObject;

/**
 * Created by zj on 2016/2/5.
 */
public class SecondBook extends BmobObject {
    private User user;
    private String isbn;
    private String discount;
    private String newold;
    private Integer count;
    private String[] pictures;
    private String tips;
    private String telePhone;

    public SecondBook(User user, String isbn, String discount, String newold, Integer count, String[] pictures, String tips, String telePhone) {
        this.user = user;
        this.isbn = isbn;
        this.discount = discount;
        this.newold = newold;
        this.count = count;
        this.pictures = pictures;
        this.tips = tips;
        this.telePhone = telePhone;
    }

    public String getTelePhone() {
        return telePhone;
    }

    public void setTelePhone(String telePhone) {
        this.telePhone = telePhone;
    }

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

    @Override
    public String toString() {
        return "SecondBook{" +
                "user=" + user +
                ", isbn='" + isbn + '\'' +
                ", discount='" + discount + '\'' +
                ", newold='" + newold + '\'' +
                ", count=" + count +
                ", pictures=" + pictures +
                ", tips='" + tips + '\'' +
                ", telePhone='" + telePhone + '\'' +
                '}';
    }
}
