package com.xfzj.getbook.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zj on 2016/3/25.
 */
public class HistoryTrjn {

    @Expose
    @SerializedName("JnDateTime")
    private String date;
   
    @Expose
    private String MercName;


    @Expose
    private String TranName;
    @Expose
    @SerializedName("TranAmt")
    private String customMoney;
    @Expose
    private String CardBalance;

    public HistoryTrjn() {
    }

    public HistoryTrjn(String date,String mercName, String tranName, String customMoney, String cardBalance) {
        this.date = date;
  
        MercName = mercName;
        TranName = tranName;
        this.customMoney = customMoney;
        CardBalance = cardBalance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public String getMercName() {
        return MercName;
    }

    public void setMercName(String mercName) {
        MercName = mercName;
    }

    public String getTranName() {
        return TranName;
    }

    public void setTranName(String tranName) {
        TranName = tranName;
    }

    public String getCustomMoney() {
        return customMoney;
    }

    public void setCustomMoney(String customMoney) {
        this.customMoney = customMoney;
    }

    public String getCardBalance() {
        return CardBalance;
    }

    public void setCardBalance(String cardBalance) {
        CardBalance = cardBalance;
    }

    @Override
    public String toString() {
        return "HistoryTrjn{" +
                "date='" + date + '\'' +
                ", MercName='" + MercName + '\'' +
                ", TranName='" + TranName + '\'' +
                ", customMoney='" + customMoney + '\'' +
                ", CardBalance='" + CardBalance + '\'' +
                '}';
    }
}
