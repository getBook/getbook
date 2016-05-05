package com.xfzj.getbook.common;

import cn.bmob.v3.BmobObject;

/**
 * Created by zj on 2016/5/5.
 */
public class BillShare extends BmobObject {
    private String detail;
    
    private String total;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public BillShare(String detail, String total) {
        this.detail = detail;
        this.total = total;
    }

    public BillShare() {
    }

    @Override
    public String toString() {
        return "BillShare{" +
                "detail='" + detail + '\'' +
                ", total='" + total + '\'' +
                '}';
    }
}
