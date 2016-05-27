package com.xfzj.getbook.common;

import android.text.TextUtils;

import com.xfzj.getbook.utils.MyLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobObject;

/**
 * Created by zj on 2016/4/16.
 */
public class Comment extends BmobObject implements Comparable<Comment> {
    /**
     * 评论内容
     */
    private String content;
    /**
     * 帖子
     */
    private Post post;
    /**
     * 回复的某条评论
     */
    private Comment comment;

    /**
     * 楼层
     */
    private Integer floor;

    public Integer getFloor() {
        if (null == floor) {
            return 0;
        }
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Comment() {
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Comment(String content, Post post, Comment comment) {
        this.content = content;
        this.post = post;
        this.comment = comment;
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
    public boolean equals(Object o) {
        if (null == o) {
            return false;
        }
        if (o instanceof Comment) {
            if (!TextUtils.isEmpty(((Comment) o).getObjectId())) {
                return getObjectId().equals(((Comment) o).getObjectId());
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getObjectId().hashCode();
    }
    
    @Override
    public String toString() {
        if (null != comment) {
            return "Comment{" + "objectId=" + getObjectId() + ", " +
                    "content='" + content + '\'' +
                    ", comment=" + comment.toString() + ",floor=" + floor +
                    '}';
        } else {
            return "Comment{" + "objectId=" + getObjectId() + ", " +
                    "content='" + content + '\'' +
                    ", floor=" + floor +
                    '}';
        }
    }

    @Override
    public int compareTo(Comment o) {
        MyLog.print(getObjectId(), o.getObjectId());
        int b = o.getObjectId().trim().compareTo(getObjectId().trim());
        if (b == 0) {
            return 0;
        } else {
            String lhsT = getCreatedAt();
            String rhsT = o.getCreatedAt();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date l = simpleDateFormat.parse(lhsT);
                Date r = simpleDateFormat.parse(rhsT);
                if (l.getTime() > r.getTime()) {
                    return 1;
                } else {
                    return -1;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return -1;
        }

    }
}
