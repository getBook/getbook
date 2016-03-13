package com.xfzj.getbook.common;

import com.google.gson.annotations.SerializedName;

import cn.bmob.v3.BmobUser;

/**
 * Created by zj on 2016/1/28.
 */
public class User extends BmobUser {
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别，是否男生
     */
    private boolean gender;

    private String huaName;
    private String header;
    /**
     * 学号
     */
    @SerializedName("sno")
    private String sno;

    /**
     * 卡号
     */
    private String cardno;
    /**
     * 暂时不知道什么鬼的msg
     */
    private String msg;
    /**
     * 暂时不知道有什么用的id
     */
    private int id;

    public String getHeader() {
        return header;
    }

    public String getHuaName() {
        return huaName;
    }

    public void setHuaName(String huaName) {
        this.huaName = huaName;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }


    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    @Override
    public boolean equals(Object o) {
        if (null == o) {
            return false;
        }
        if (o instanceof User) {
            User oo = (User) o;
            if (this.sno.equals(oo.getSno())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", gender=" + gender +
                ", huaName='" + huaName + '\'' +
                ", header='" + header + '\'' +
                ", sno='" + sno + '\'' +
                ", cardno='" + cardno + '\'' +
                ", msg='" + msg + '\'' +
                ", id=" + id +
                '}';
    }

    public User() {

    }

    public User(String name, boolean gender, String huaName, String sno, String cardno, String msg,int id) {
        this.name = name;
        this.gender = gender;
        this.huaName = huaName;
        this.sno = sno;
        this.cardno = cardno;
        this.msg = msg;
        this.id = id;
                
    }
}
