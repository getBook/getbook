package com.xfzj.getbook.common;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by zj on 2016/3/5.
 */
public class Debris extends BmobObject {
    @SerializedName("_id")
    private String id;
    
    private User user;
    private String title;
    @SerializedName("description")
    private String tips;


    private String[] pics;

    private List<BmobFile> files;
    private String discount;

    private String originPrice;

    private int count;

    private String newold;
    @SerializedName("telePhone")
    private String tele;

//    private String createdAt;

   
    private String picture;

    private boolean overtime;
    
    public Debris() {

    }

    public Debris(User user, String title, String tips, String[] pics, String discount, String originPrice, int count, String newold, String tele) {
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

//    @Override
//    public String getCreatedAt() {
//        return createdAt;
//    }
//
//    @Override
//    public void setCreatedAt(String createdAt) {
//        this.createdAt = createdAt;
//    }


    public boolean isOvertime() {
        return overtime;
    }

    public void setOvertime(boolean overtime) {
        this.overtime = overtime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<BmobFile> getFiles() {
        return files;
    }

    public void setFiles(List<BmobFile> files) {
        this.files = files;
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
