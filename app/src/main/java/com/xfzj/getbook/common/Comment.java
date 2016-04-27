package com.xfzj.getbook.common;

import cn.bmob.v3.BmobObject;

/**
 * Created by zj on 2016/4/16.
 */
public class Comment extends BmobObject {
    /**
     * 评论内容
     */
    private String content;
    /**
     * 帖子
     */
    private Post post;

    public Comment() {
    }

    public Comment(String content, Post post) {
        this.content = content;
        this.post = post;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "content='" + content + '\'' +
                ", post=" + post +
                '}';
    }
}
