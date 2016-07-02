package com.xfzj.getbook.common;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by zj on 2016/1/28.
 */
public class User extends BmobUser {
    /**
     * 姓名
     */
    @SerializedName("ecardName")
    private String name;
    /**
     * 性别，是否男生
     */
    private boolean gender;
    @SerializedName("nickname")
    private String huaName;

    private BmobFile bmobHeader;
    /**
     * 学号
     */
    @SerializedName("sno")
    private String sno;

    /**
     * 卡号
     */
    @SerializedName("ecardNo")
    private String cardno;
    /**
     * 访问一卡通业务必需
     */
    private String msg;
    /**
     * 暂时不知道有什么用的id
     */
    @SerializedName("ecardId")
    private int id;


    private String avator;

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public BmobFile getBmobHeader() {
        return bmobHeader;
    }

    public String getHuaName() {
        return huaName;
    }

    public void setHuaName(String huaName) {
        this.huaName = huaName;
    }

    public void setBmobHeader(BmobFile bmobHeader) {
        this.bmobHeader = bmobHeader;
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
            if (!TextUtils.isEmpty(sno) && this.sno.equals(oo.getSno())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String s = null;
        if (null != bmobHeader) {
            s = bmobHeader.getUrl();
        }
        return "User{" +
                "name='" + name + '\'' +
                ", gender=" + gender +
                ", huaName='" + huaName + '\'' +
                ", bmobHeader='" + s + '\'' +
                ", sno='" + sno + '\'' +
                ", cardno='" + cardno + '\'' +
                ", msg='" + msg + '\'' +
                ", id=" + id +
                '}';
    }

    public User() {

    }

    public User(String name, boolean gender, String huaName, String sno, String cardno, String msg, int id) {
        this.name = name;
        this.gender = gender;
        this.huaName = huaName;
        this.sno = sno;
        this.cardno = cardno;
        this.msg = msg;
        this.id = id;

    }

    public User(String name, boolean gender, String huaName, String sno, String cardno, String msg, int id, String objectId, String email) {

        this.name = name;
        this.gender = gender;
        this.huaName = huaName;
        this.sno = sno;
        this.cardno = cardno;
        this.msg = msg;
        this.id = id;
        setObjectId(objectId);
        setEmail(email);
    }
}
