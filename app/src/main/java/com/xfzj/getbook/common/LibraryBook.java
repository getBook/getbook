package com.xfzj.getbook.common;

/**
 * Created by zj on 2016/4/2.
 */
public class LibraryBook {
    /**
     * 图书类型：西文图书，中文图书
     */
    private String libraryType;
    /**
     * 图书名称
     */
    private String title;
    /**
     * 馆藏复本
     */
    private String count;
    /**
     * 可借复本
     */
    private String borrowCount;

    /**
     * 作者、出版社和年份
     */
    private String ap;
  
    /**
     * 图书位置
     */
    private String position;
    
    public String getLibraryType() {
        return libraryType;
    }

    public void setLibraryType(String libraryType) {
        this.libraryType = libraryType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getBorrowCount() {
        return borrowCount;
    }

    public void setBorrowCount(String borrowCount) {
        this.borrowCount = borrowCount;
    }

  

    public LibraryBook() {
    }

    public String getAp() {
        return ap;
    }

    public void setAp(String ap) {
        this.ap = ap;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LibraryBook(String libraryType, String title, String count, String borrowCount, String ap, String position) {
        this.libraryType = libraryType;
        this.title = title;
        this.count = count;
        this.borrowCount = borrowCount;
        this.ap = ap;
        this.position = position;
    }
}
