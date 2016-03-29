package com.xfzj.getbook.common;

/**
 * Created by zj on 2016/3/29.
 */
public class LibraryUserInfo {
    private String bookInfo;

    private String maxBorrow;

    private String illegalCount;

    private String ownMoney;

    private String borrowCount;

    public LibraryUserInfo(String bookInfo, String maxBorrow, String illegalCount, String ownMoney, String borrowCount) {
        this.bookInfo = bookInfo;
        this.maxBorrow = maxBorrow;
        this.illegalCount = illegalCount;
        this.ownMoney = ownMoney;
        this.borrowCount = borrowCount;
    }

    public LibraryUserInfo() {
    }

    public String getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(String bookInfo) {
        this.bookInfo = bookInfo;
    }

    public String getMaxBorrow() {
        return maxBorrow;
    }

    public void setMaxBorrow(String maxBorrow) {
        this.maxBorrow = maxBorrow;
    }

    public String getIllegalCount() {
        return illegalCount;
    }

    public void setIllegalCount(String illegalCount) {
        this.illegalCount = illegalCount;
    }

    public String getOwnMoney() {
        return ownMoney;
    }

    public void setOwnMoney(String ownMoney) {
        this.ownMoney = ownMoney;
    }

    public String getBorrowCount() {
        return borrowCount;
    }

    public void setBorrowCount(String borrowCount) {
        this.borrowCount = borrowCount;
    }

    @Override
    public String toString() {
        return "LibraryUserInfo{" +
                "bookInfo='" + bookInfo + '\'' +
                ", maxBorrow='" + maxBorrow + '\'' +
                ", illegalCount='" + illegalCount + '\'' +
                ", ownMoney='" + ownMoney + '\'' +
                ", borrowCount='" + borrowCount + '\'' +
                '}';
    }
}
