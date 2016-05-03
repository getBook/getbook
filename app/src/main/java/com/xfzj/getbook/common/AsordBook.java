package com.xfzj.getbook.common;

/**图书馆个人荐购图书信息
 * Created by zj on 2016/5/2.
 */
public class AsordBook   {
    private String name;
    private String author;
    private String publisher;
    private String date;
    private String state;

    public AsordBook() {
    }

    public AsordBook(String name, String author, String publisher, String date, String state) {
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.date = date;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "AsordBook{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", date='" + date + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
