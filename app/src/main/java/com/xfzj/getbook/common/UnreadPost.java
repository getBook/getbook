package com.xfzj.getbook.common;

import com.xfzj.getbook.utils.MyLog;

/**
 * Created by zj on 2016/5/17.
 */
public class UnreadPost {
    /**
     * 未读帖子
     */
    private Post post;
    /**
     * 未读的数量
     */
    private int unReadCount;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public UnreadPost(Post post, int unReadCount) {
        this.post = post;
        this.unReadCount = unReadCount;
    }

    @Override
    public int hashCode() {
        if (getPost() == null) {
            return 0;
        }
        return getPost().getObjectId().hashCode();
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            MyLog.print("equal", "false1");
            return false;
        }
        if (o instanceof UnreadPost) {
            MyLog.print("equal", ((UnreadPost) o).getPost().equals(getPost()) + "");
            return ((UnreadPost) o).getPost().equals(getPost());
        } else {
            MyLog.print("equal", "false2");
            return false;
        }
    }
    
}
