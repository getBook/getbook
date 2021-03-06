package com.xfzj.getbook.async;

import android.content.Context;

import com.xfzj.getbook.common.BorrowBook;
import com.xfzj.getbook.utils.MyLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**获取图书馆借阅信息
 * Created by zj on 2016/5/1.
 */
public class GetBookListAsync extends BaseGetLibraryInfoAsyc<List<BorrowBook>> {
    public GetBookListAsync(Context context) {
        super(context);
    }

    @Override
    protected boolean needCookie() {
        return true;
    }

    @Override
    protected List<BorrowBook> parse(String[] params, String result) {
        Document document = Jsoup.parse(result);
        Element myLibContent = document.getElementById("mylib_content");
        List<BorrowBook> borrowBooks = new ArrayList<>();
        if (null == myLibContent) {
            return null;
        }
        Elements no = myLibContent.getElementsByClass("iconerr");
        if (null != no && no.size() != 0) {
            MyLog.print("no", "" + no.size());
            return borrowBooks;
        }
        Elements books = myLibContent.select("table").get(0).select("tr");
        if (null == books || books.size() == 0) {
            return null;
        }

        for (int i = 1; i < books.size(); i++) {
            Element book = books.get(i);
            Elements eles = book.getElementsByClass("whitetext");
            if (null == eles||eles.size()==0) {
                continue;
            }
            String bookName = eles.get(1).text();
            String borrowDate = eles.get(2).text();
            String returnDate = eles.get(3).text();
            String click = book.getElementById("" + i).getElementsByTag("input").get(0).attr("onclick");
            Pattern pattern = Pattern.compile("'+\\S+?'");
            Matcher matcher = pattern.matcher(click);
            int j = 0;
            String code = null, check = null;
            while (matcher.find()) {
                String group = matcher.group().substring(1, matcher.group().length()-1);
                if (j == 0) {
                    code = group;
                }else if(j==1){
                    check = group;
                }
                j++;
            }
            BorrowBook borrowBook = new BorrowBook(bookName, code, check, borrowDate, returnDate);
            borrowBooks.add(borrowBook);
        }
        return borrowBooks;
    }

}
