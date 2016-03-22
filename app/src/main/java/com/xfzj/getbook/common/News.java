package com.xfzj.getbook.common;

import java.io.Serializable;

/**
 * Created by zj on 2016/3/16.
 */
public class News implements Serializable {
    private String title, date, href;

    public News() {
    }

    public News(String title, String date, String href) {
        this.title = title;
        this.date = date;
        this.href = href;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", href='" + href + '\'' +
                '}';
    }
}
