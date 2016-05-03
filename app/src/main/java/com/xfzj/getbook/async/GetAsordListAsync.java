package com.xfzj.getbook.async;

import android.content.Context;

import com.xfzj.getbook.common.AsordBook;
import com.xfzj.getbook.utils.MyLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**获取图书馆荐购信息
 * Created by zj on 2016/5/1.
 */
public class GetAsordListAsync extends BaseGetLibraryInfoAsyc<List<AsordBook>> {
    public GetAsordListAsync(Context context) {
        super(context);
    }

    @Override
    protected boolean needCookie() {
        return true;
    }

    @Override
    protected List<AsordBook> parse(String[] params, String result) {
        Document document = Jsoup.parse(result);
        Element myLibContent = document.getElementById("mylib_content");
        List<AsordBook> asordBooks = new ArrayList<>();
        if (null == myLibContent) {
            return null;
        }
        Elements no = myLibContent.getElementsByClass("iconerr");
        if (null != no && no.size() != 0) {
            MyLog.print("no", "" + no.size());
            return asordBooks;
        }
        Elements books = myLibContent.select("table").get(0).select("tr");
        if (null == books || books.size() == 0) {
            return null;
        }

        for (int i = 1; i < books.size(); i++) {
            Element book = books.get(i);
            Elements eles = book.select("td");
            String name = eles.get(0).text();
            String author = eles.get(1).text();
            String publisher = eles.get(2).text();
            String date = eles.get(3).text();
            String state = eles.get(4).text();

            AsordBook asordBook = new AsordBook(name, author, publisher, date, state);
            asordBooks.add(asordBook);
        }
        return asordBooks;
    }

}
