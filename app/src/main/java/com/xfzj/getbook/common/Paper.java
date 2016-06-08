package com.xfzj.getbook.common;

import com.google.gson.annotations.Expose;

/**
 * Created by zj on 2016/6/7.
 */
public class Paper {
    @Expose
    private int downloads;
    @Expose
    private String link;
    @Expose
    private String title;
    @Expose
    private String type;


    public static final String GENERAL = "general";
    public static final String PROFESSIONAL = "professional";
    public Paper(int downloads, String link, String title, String type) {
        this.downloads = downloads;
        this.link = link;
        this.title = title;
        this.type = type;
    }

    public Paper() {
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Paper{" +
                "downloads=" + downloads +
                ", link='" + link + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
