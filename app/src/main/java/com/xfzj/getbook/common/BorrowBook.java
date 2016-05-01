package com.xfzj.getbook.common;

/**
 * Created by zj on 2016/5/1.
 */
public class BorrowBook {
    private String bookName;
    private String code;
    private String check;

    public BorrowBook() {
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public BorrowBook(String bookName, String code, String check) {
        this.bookName = bookName;
        this.code = code;
        this.check = check;
    }

    @Override
    public String toString() {
        return "BorrowBook{" +
                "bookName='" + bookName + '\'' +
                ", code='" + code + '\'' +
                ", check='" + check + '\'' +
                '}';
    }
}
