package com.xfzj.getbook.common;

/**
 * Created by zj on 2016/5/6.
 */
public class LocalPost extends Post {
    
    private boolean isLiked;

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
