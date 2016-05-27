package com.xfzj.getbook.common;

import cn.bmob.v3.BmobObject;

/**
 * Created by zj on 2016/5/11.
 */
public class CommonShareSetting extends BmobObject {
    /**
     * 账单带我穿越的分享功能是否打开
     */
    private boolean isOpen;

    public boolean isOpen() {
        return isOpen;
    }
}
