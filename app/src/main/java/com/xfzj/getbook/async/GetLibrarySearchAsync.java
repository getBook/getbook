package com.xfzj.getbook.async;

import android.content.Context;
import android.text.TextUtils;

import com.xfzj.getbook.common.LibraryBook;
import com.xfzj.getbook.utils.MyLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zj on 2016/4/2.
 */
public class GetLibrarySearchAsync extends BaseGetLibraryInfoAsyc<List<LibraryBook>> {

    public GetLibrarySearchAsync(Context context) {
        super(context);
    }

    @Override
    protected boolean needCookie() {
        return false;
    }

    @Override
    protected List<LibraryBook> parse(String[] params, String result) {
        Document document = Jsoup.parse(result);
        Elements elements = document.getElementsByClass("book_list_info");
        if(null==elements||elements.size()==0){
            return null;
        }
        List<LibraryBook> libraryBooks = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
            LibraryBook libraryBook = new LibraryBook();
            Element element = elements.get(i);
            String ap=element.html();
            ap=ap.substring(ap.lastIndexOf("</span>") + 7, ap.lastIndexOf("<br>"));
            ap = ap.replaceAll("<br>", "");
            ap = ap.replaceAll("&nbsp;", "");
            ap=ap.replaceAll("\\s{2}","\n");
            libraryBook.setAp(ap);
            Elements elements2 = element.getElementsByTag("span");
            for (int j = 0; j < elements2.size(); j++) {
                String str = elements2.get(j).text();
                if (TextUtils.isEmpty(str)) {
                    continue;
                }
                if(j==0) {
                    libraryBook.setLibraryType(str);
                }else {
                    String[] canBorrow = str.split("可");
                    libraryBook.setCount(canBorrow[0]);
                    libraryBook.setBorrowCount("可"+canBorrow[1]);
                }
            }
            String name = element.getElementsByTag("a").get(0).text();
            name=name.substring(name.indexOf(".")+1);
            libraryBook.setTitle(name);
            String position = element.getElementsByTag("h3").get(0).ownText();
            libraryBook.setPosition(position);
            String guancang=element.getElementsByClass("tooltip").get(0).attr("href");
            libraryBook.setGuancang(guancang);
            libraryBooks.add(libraryBook);
        }
        return libraryBooks;
    }
}
