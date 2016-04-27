package com.xfzj.getbook.common;

import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by zj on 2016/4/16.
 */
public class Post extends BmobObject {
    private String content;
    private String[] topic;

    private BmobRelation likes;

    private String[] pics;
    private List<BmobFile> files;


    private Integer likeCount, commentCount;

    public Post() {
    }

    public void setFiles(List<BmobFile> files) {
        this.files = files;
    }

    public Post(String content, String[] topic, BmobRelation likes, String[] pics) {
        this.content = content;
        this.topic = topic;
        this.likes = likes;
        this.pics = pics;
        this.likeCount=0;
        this.commentCount=0;
    }

    public Post(String content, String[] topic, BmobRelation likes, String[] pics, List<BmobFile> files, Integer likeCount, Integer commentCount) {
        this.content = content;
        this.topic = topic;
        this.likes = likes;
        this.pics = pics;
        this.files = files;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public List<BmobFile> getFiles() {
        return files;
    }

    public String[] getPics() {
        return pics;
    }

    public void setPics(String[] pics) {
        this.pics = pics;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getTopic() {
        return topic;
    }

    public void setTopic(String[] topic) {
        this.topic = topic;
    }

    public BmobRelation getLikes() {
        return likes;
    }

    public void setLikes(BmobRelation likes) {
        this.likes = likes;
    }


    @Override
    public String toString() {
        return "Post{" +
                "content='" + content + '\'' +
                ", topic=" + Arrays.toString(topic) +
                ", likes=" + likes +
                ", pics=" + Arrays.toString(pics) +
                ", files=" + files +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Post) {
            if (((Post) o).getObjectId().equals(this.getObjectId())) {
                return true;
            }

        } else {
            return false;
        }
        return false;
    }

}
