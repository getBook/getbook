package com.xfzj.getbook.common;

import com.google.gson.annotations.Expose;

/**
 * Created by zj on 2016/3/14.
 */
public class Score {
    @Expose
    private String XN;
    @Expose
    private String XQ;
    @Expose
    private String XH;
    @Expose
    private String KCMC;
    @Expose
    private String XF;
    @Expose
    private String CJ;

    public Score() {
    }

    public Score(String XN, String XQ, String XH, String KCMC, String XF, String CJ) {
        this.XN = XN;
        this.XQ = XQ;
        this.XH = XH;
        this.KCMC = KCMC;
        this.XF = XF;
        this.CJ = CJ;
    }


    public String getXN() {
        return XN;
    }

    public void setXN(String XN) {
        this.XN = XN;
    }

    public String getXQ() {
        return XQ;
    }

    public void setXQ(String XQ) {
        this.XQ = XQ;
    }

    public String getXH() {
        return XH;
    }

    public void setXH(String XH) {
        this.XH = XH;
    }

    public String getKCMC() {
        return KCMC;
    }

    public void setKCMC(String KCMC) {
        this.KCMC = KCMC;
    }

    public String getXF() {
        return XF;
    }

    public void setXF(String XF) {
        this.XF = XF;
    }

    public String getCJ() {
        return CJ;
    }

    public void setCJ(String CJ) {
        this.CJ = CJ;
    }


    @Override
    public String toString() {
        return "Score{" +
                "XN='" + XN + '\'' +
                ", XQ='" + XQ + '\'' +
                ", XH='" + XH + '\'' +
                ", KCMC='" + KCMC + '\'' +
                ", XF='" + XF + '\'' +
                ", CJ='" + CJ + '\'' +
                '}';
    }
}
