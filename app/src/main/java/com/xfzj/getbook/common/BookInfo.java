package com.xfzj.getbook.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

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

    private String coverImage;
    
    private BmobFile bmobImage;
    @Expose
    private String pubdate;
    public String[] getAuthor() {
        return author;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public void setAuthor(String[] author) {
        this.author = author;
    }

    @Expose
    @SerializedName("author")
    private String[] author;

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

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

    public BmobFile getBmobImage() {
        return bmobImage;
    }

    public void setBmobImage(BmobFile bmobImage) {
        this.bmobImage = bmobImage;
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
