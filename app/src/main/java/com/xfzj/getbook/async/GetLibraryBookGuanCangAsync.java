package com.xfzj.getbook.async;

import android.content.Context;

import com.xfzj.getbook.common.LibraryBookPosition;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zj on 2016/4/2.
 */
public class GetLibraryBookGuanCangAsync extends BaseGetLibraryInfoAsyc<List<LibraryBookPosition>> {

    public GetLibraryBookGuanCangAsync(Context context) {
        super(context);
    }

    @Override
    protected boolean needCookie() {
        return false;
    }

    @Override
    protected List<LibraryBookPosition> parse(String[] params, String result) {
        Document document = Jsoup.parse(result);
        Elements elements = document.getElementsByClass("whitetext");
        if (null == elements || elements.size() == 0) {
            return null;
        }
        List<LibraryBookPosition> libraryBookPositions = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
            LibraryBookPosition libraryBookPosition = new LibraryBookPosition();
            Element element = elements.get(i);
            Elements tds = element.select("td");
            libraryBookPosition.setPosition(tds.get(3).text());
            libraryBookPosition.setState(tds.get(4).text());
            libraryBookPositions.add(libraryBookPosition);
        }
        return libraryBookPositions;
    }
}
