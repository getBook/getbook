package com.xfzj.getbook.common;

/**
 * Created by zj on 2016/4/30.
 */
public class LibraryBookPosition {
    /**
     * 馆藏地
     */
    private String position;
    /**
     * 借阅状态
     */
    private String state;

    public LibraryBookPosition() {
    }

    public LibraryBookPosition(String position, String state) {
        this.position = position;
        this.state = state;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "LibraryBookPosition{" +
                "position='" + position + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
