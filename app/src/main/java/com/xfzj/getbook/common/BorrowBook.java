package com.xfzj.getbook.common;

/**
 * Created by zj on 2016/5/1.
 */
public class BorrowBook {
    private String bookName;
    private String code;
    private String check;
    private String borrowDate;
    private String returnDate;

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

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public BorrowBook(String bookName, String code, String check, String borrowDate, String returnDate) {
        this.bookName = bookName;
        this.code = code;
        this.check = check;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return "BorrowBook{" +
                "bookName='" + bookName + '\'' +
                ", code='" + code + '\'' +
                ", check='" + check + '\'' +
                ", borrowDate='" + borrowDate + '\'' +
                ", returnDate='" + returnDate + '\'' +
                '}';
    }
}
