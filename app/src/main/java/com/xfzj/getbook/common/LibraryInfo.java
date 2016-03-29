package com.xfzj.getbook.common;

/**
 * Created by zj on 2016/3/28.
 */
public class LibraryInfo {
    private String account;
    private String password;
    private String cookie;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public LibraryInfo() {
    }

    public LibraryInfo(String account, String password, String cookie) {
        this.account = account;
        this.password = password;
        this.cookie = cookie;
    }

    @Override
    public String toString() {
        return "LibraryInfo{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", cookie='" + cookie + '\'' +
                '}';
    }
}
