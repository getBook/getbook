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


    public static final int UNKNOWNLIKESTATE = -1;

    public static final int NOLIKESTATE = 0;
    public static final int LIKEDSTATE = 1;
    private String content;
    private String[] topic;

    private BmobRelation likes;

    private String[] pics;
    private List<BmobFile> files;

    private int likeCount=-1,CommentCount=-1;

    private BmobRelation focus;
    /**
     * -1 点赞状态未知
     * 0 未点赞
     * 1 已点赞
     */
    private int likeState=-1;

    public Post(String str) {
        setObjectId(str);
    }

    public int getLikeState() {
        return likeState;
    }

    public void setLikeState(int likeState) {
        this.likeState = likeState;
    }

    public Post() {
    }

    public BmobRelation getFocus() {
        return focus;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return CommentCount;
    }

    public void setCommentCount(int commentCount) {
        CommentCount = commentCount;
    }

    public void setFiles(List<BmobFile> files) {
        this.files = files;
    }

    public Post(String content, String[] topic, BmobRelation likes, String[] pics) {
        this.content = content;
        this.topic = topic;
        this.likes = likes;
        this.pics = pics;
    }

    public Post(String content, String[] topic, BmobRelation likes, String[] pics, List<BmobFile> files) {
        this.content = content;
        this.topic = topic;
        this.likes = likes;
        this.pics = pics;
        this.files = files;
    }

    public void setFocus(BmobRelation focus) {
        this.focus = focus;
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
    public int hashCode() {
        return getObjectId().hashCode();
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
