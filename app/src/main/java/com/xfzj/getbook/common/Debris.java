package com.xfzj.getbook.common;

import java.util.Arrays;

import cn.bmob.v3.BmobObject;

/**
 * Created by zj on 2016/3/5.
 */
public class Debris extends BmobObject {
    private User user;
    private String title;

    private String tips;


    private String[] pics;

    private String discount;

    private String originPrice;

    private int count;

    private String newold;

    private String tele;

    public Debris(User user,String title, String tips, String[] pics, String discount, String originPrice, int count, String newold, String tele) {
        this.user = user;
        this.title = title;
        this.tips = tips;
        this.pics = pics;
        this.discount = discount;
        this.originPrice = originPrice;
        this.count = count;
        this.newold = newold;
        this.tele = tele;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String[] getPics() {
        return pics;
    }

    public void setPics(String[] pics) {
        this.pics = pics;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(String originPrice) {
        this.originPrice = originPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNewold() {
        return newold;
    }

    public void setNewold(String newold) {
        this.newold = newold;
    }

    public String getTele() {
        return tele;
    }

    public void setTele(String tele) {
        this.tele = tele;
    }

    @Override
    public String toString() {
        return "Debris{" +
                "user=" + user +
                ", title='" + title + '\'' +
                ", tips='" + tips + '\'' +
                ", pics=" + Arrays.toString(pics) +
                ", discount='" + discount + '\'' +
                ", originPrice='" + originPrice + '\'' +
                ", count=" + count +
                ", newold='" + newold + '\'' +
                ", tele='" + tele + '\'' +
                '}';
    }
}
