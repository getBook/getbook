package com.xfzj.getbook.common;

import java.util.List;

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
    /**
     * 館藏
     */
    private String guancang;

    private List<LibraryBookPosition> libraryBookPositions;

    public List<LibraryBookPosition> getLibraryBookPositions() {
        return libraryBookPositions;
    }

    public void setLibraryBookPositions(List<LibraryBookPosition> libraryBookPositions) {
        this.libraryBookPositions = libraryBookPositions;
    }

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

    public String getGuancang() {
        return guancang;
    }

    public void setGuancang(String guancang) {
        this.guancang = guancang;
    }

    @Override
    public String toString() {
        return "LibraryBook{" +
                "libraryType='" + libraryType + '\'' +
                ", title='" + title + '\'' +
                ", count='" + count + '\'' +
                ", borrowCount='" + borrowCount + '\'' +
                ", ap='" + ap + '\'' +
                ", position='" + position + '\'' +
                ", guancang='" + guancang + '\'' +
                '}';
    }
}
