package com.xfzj.getbook.common;

import com.google.gson.annotations.Expose;

/**
 * Created by zj on 2016/3/28.
 */
public class SubsidyTrjn {
    @Expose
    private String TranName;
    @Expose
    private String SubAmt;
    @Expose
    private String JndateTime;
    @Expose
    private String Subjnstatus;


    public SubsidyTrjn() {
    }

    public SubsidyTrjn(String tranName, String subAmt, String jndateTime, String subjnstatus) {
        TranName = tranName;
        SubAmt = subAmt;
        JndateTime = jndateTime;
        Subjnstatus = subjnstatus;
    }

    public String getTranName() {
        return TranName;
    }

    public void setTranName(String tranName) {
        TranName = tranName;
    }

    public String getSubAmt() {
        return SubAmt;
    }

    public void setSubAmt(String subAmt) {
        SubAmt = subAmt;
    }

    public String getJndateTime() {
        return JndateTime;
    }

    public void setJndateTime(String jndateTime) {
        JndateTime = jndateTime;
    }

    public String getSubjnstatus() {
        return Subjnstatus;
    }

    public void setSubjnstatus(String subjnstatus) {
        Subjnstatus = subjnstatus;
    }

    @Override
    public String toString() {
        return "SubsidyTrjn{" +
                "TranName='" + TranName + '\'' +
                ", SubAmt='" + SubAmt + '\'' +
                ", JndateTime='" + JndateTime + '\'' +
                ", Subjnstatus='" + Subjnstatus + '\'' +
                '}';
    }
}
