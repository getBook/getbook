package com.xfzj.getbook.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

import cn.bmob.v3.BmobObject;

/**
 * Created by zj on 2016/2/24.
 */
public class BookInfo extends BmobObject{
    
    private String isbn;
    @Expose 
    @SerializedName("title")
    private String bookName;
    @Expose
    @SerializedName("publisher")
    private String publish;
    @Expose
    @SerializedName("price")
    private String originPrice;
    @Expose
    private String image;

    public String[] getAuthor() {
        return author;
    }

    public void setAuthor(String[] author) {
        this.author = author;
    }

    @Expose
    @SerializedName("author")
    private String[] author;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public String getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(String originPrice) {
        this.originPrice = originPrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "BookInfo{" +
                "isbn='" + isbn + '\'' +
                ", bookName='" + bookName + '\'' +
                ", publish='" + publish + '\'' +
                ", originPrice='" + originPrice + '\'' +
                ", image='" + image + '\'' +
                ", author=" + Arrays.toString(author) +
                '}';
    }
}
