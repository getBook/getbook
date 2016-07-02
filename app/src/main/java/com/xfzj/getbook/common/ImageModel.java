package com.xfzj.getbook.common;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zj on 2016/6/25.
 * "__v": 0,
 * "qiniu_url": "http://luxiaojian.qiniucdn.com/-781373464.jpg",
 * "local_url": "http://getfun-api.alpha.duohuo.org/-781373464.jpg",
 * "image_src": "http://getfun-api.alpha.duohuo.org/-781373464.jpg",
 * "hash": "Fm6NQNo6EEsOKo5NAwcuPI8yPU6x",
 * "_id": "576e47dc47198e1c6dedcc09",
 * "createdAt": "2016-06-25T08:59:08.829Z"
 */
public class ImageModel {
    @SerializedName("qiniu_url")
    private String qiniuUrl;
    @SerializedName("local_url")
    private String localUrl;
    @SerializedName("image_src")
    private String imageUrl;
    private String hash;
    @SerializedName("_id")
    private String id;
    private String createdAt;

    public String getQiniuUrl() {
        return qiniuUrl;
    }

    public void setQiniuUrl(String qiniuUrl) {
        this.qiniuUrl = qiniuUrl;
    }

    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
