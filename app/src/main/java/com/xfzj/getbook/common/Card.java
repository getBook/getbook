package com.xfzj.getbook.common;

import com.google.gson.annotations.Expose;

/**
 * Created by zj on 2016/3/14.
 */
public class Card {
    @Expose
    private String bankno;
    @Expose
    private String cardbalance;
    @Expose
    private String pretmpbalance;
    @Expose
    private String cardstatus;

    public Card() {
    }

    public Card(String bankno, String cardbalance, String pretmpbalance, String cardstatus) {
        this.bankno = bankno;
        this.cardbalance = cardbalance;
        this.pretmpbalance = pretmpbalance;
        this.cardstatus = cardstatus;
    }

    public String getBankno() {
        return bankno;
    }

    public void setBankno(String bankno) {
        this.bankno = bankno;
    }

    public String getCardbalance() {
        return cardbalance;
    }

    public void setCardbalance(String cardbalance) {
        this.cardbalance = cardbalance;
    }

    public String getPretmpbalance() {
        return pretmpbalance;
    }

    public void setPretmpbalance(String pretmpbalance) {
        this.pretmpbalance = pretmpbalance;
    }

    public String getCardstatus() {
        return cardstatus;
    }

    public void setCardstatus(String cardstatus) {
        this.cardstatus = cardstatus;
    }

    @Override
    public String toString() {
        return "Card{" +
                "bankno='" + bankno + '\'' +
                ", cardbalance='" + cardbalance + '\'' +
                ", pretmpbalance='" + pretmpbalance + '\'' +
                ", cardstatus='" + cardstatus + '\'' +
                '}';
    }
}
