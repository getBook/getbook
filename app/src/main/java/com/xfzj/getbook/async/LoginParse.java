package com.xfzj.getbook.async;

import android.content.Context;
import android.text.TextUtils;

import com.xfzj.getbook.common.LibraryInfo;
import com.xfzj.getbook.common.LibraryUserInfo;
import com.xfzj.getbook.utils.SharedPreferencesUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by zj on 2016/3/29.
 */

public  class LoginParse {

    public static LibraryUserInfo parse(Context context, String result, String account, String passwd, String cookie) {
        SharedPreferencesUtils.saveLibraryLoginInfo(context, new LibraryInfo(account, passwd, cookie));
        Document document = Jsoup.parse(result);
        Element e = document.getElementsByClass("mylib_msg").first();
        Element element = document.getElementById("mylib_info");
        Elements elements = element.getElementsByTag("TD");
        LibraryUserInfo libraryUserInfo = new LibraryUserInfo();
        String s = e.text();
        s = s.replaceAll("，点击继续荐购", "");
        libraryUserInfo.setBookInfo(s);
        for (int i = 0; i < elements.size(); i++) {
            Element element1 = elements.get(i);
            String str = element1.text();
            if (TextUtils.isEmpty(str)) {
                continue;
            }
            String temp = element1.toString();
            temp = temp.substring(temp.lastIndexOf("</span>") + 7, temp.lastIndexOf("</td>"));
            if (str.contains("累计借书")) {
                libraryUserInfo.setBorrowCount(temp);
            } else if (str.contains("违章次数")) {
                libraryUserInfo.setIllegalCount(temp);
            } else if (str.contains("欠款金额")) {
                libraryUserInfo.setOwnMoney(temp);
            } else if (str.contains("最大可借图书")) {
                libraryUserInfo.setMaxBorrow(temp);
            }
        }
        return libraryUserInfo;
    }
}