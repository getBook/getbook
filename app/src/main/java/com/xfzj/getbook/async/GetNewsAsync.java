package com.xfzj.getbook.async;

import android.content.Context;

import com.xfzj.getbook.common.News;
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.net.HttpHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zj on 2016/3/16.
 */
public class GetNewsAsync extends BaseAsyncTask<Integer, Void, List<News>> {

    public GetNewsAsync(Context context) {
        super(context);
    }

    @Override
    protected void onPost(List<News> newses) {
        if (null == newses || newses.size() == 0) {
            if (null != onTaskListener) {
                onTaskListener.onFail();
            }
        }else {
            if (null != onTaskListener) {
                onTaskListener.onSuccess(newses);
            }
        }
    }

    @Override
    protected List<News> doExcute(Integer[] params) {

        try {
         
            String url = BaseHttp.GETNEWS + params[0];
            byte[] bytes = new HttpHelper().DoConnection(url);
            String result = new String(bytes, "utf-8");
            return parseNews(result);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    /**
     * 解析新闻列表
     *
     * @param result
     */
    private List<News> parseNews(String result) {
        List<News> news = new ArrayList<>();
        try {
            Document document = Jsoup.parse(result);
            Elements elements = document.getElementsByClass("gridline");
            for (int i = 0; i < elements.size(); i++) {
                Element element = elements.get(i);
                Element e = element.getElementsByClass("Title").get(0);
                String href = e.attr("href");
                href = href.substring(5);
                String title = e.text();
                Element ele = element.getElementsByTag("td").get(2);
                news.add(new News(title, ele.text(), href));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return news;
    }
}
