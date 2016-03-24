package com.xfzj.getbook.async;

import android.content.Context;
import android.text.TextUtils;

import com.xfzj.getbook.common.News;
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.net.HttpHelper;
import com.xfzj.getbook.net.IHttpHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zj on 2016/3/21.
 */
public class GetNewsListLoader extends BaseAsyncLoader<List<News>> {
    private String __EVENTTARGET, __EVENTARGUMENT = "Page$", __VIEWSTATEGENERATOR;
    private int page = 1;

    public GetNewsListLoader(Context context) {
        super(context);
//        setProgressDialog(null, context.getString(R.string.loading));
     
    }

    @Override
    protected List<News> doInBackground() {
        try {
            String url = BaseHttp.GETNEWS;
            byte[] bytes = null;
            if ( TextUtils.isEmpty(__EVENTTARGET) && TextUtils.isEmpty(__VIEWSTATEGENERATOR)) {
                bytes = new HttpHelper().DoConnection(url);
            } else if ("Page$First".equals(__EVENTARGUMENT)) {
                return null;
            } else {
                Map<String, String> map = new HashMap<>();
                map.put("__EVENTARGUMENT", __EVENTARGUMENT+page);
                map.put("__EVENTTARGET", __EVENTTARGET);
                map.put("__VIEWSTATEGENERATOR", __VIEWSTATEGENERATOR);
                map.put("action", "search");
                bytes = new HttpHelper().DoConnection(url, IHttpHelper.METHOD_POST, map);
            }
            String result = new String(bytes, "utf-8");
            return parseNews(result);
        } catch (Exception e)

        {
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
        if (TextUtils.isEmpty(result)) {
            return news;
        }
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
//                MyLog.print("new",title);
                news.add(new News(title, ele.text(), href));
            }

            __VIEWSTATEGENERATOR = document.getElementById("__VIEWSTATEGENERATOR").attr("value");
            Element element = document.getElementsByClass("pagecode").get(0);
            Element aa = element.getElementsByTag("a").get(0);
            String aaa = aa.attr("href");
            Pattern pattern = Pattern.compile("'\\S+'");
            Matcher matcher = pattern.matcher(aaa);
            if (matcher.find()) {
                aaa = matcher.group();
                String[] str = aaa.split(",");
                __EVENTTARGET = str[0].substring(1, str[0].length() - 1);
                page++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return news;
    }

    @Override
    protected void onReset() {
        super.onReset();
        __EVENTTARGET=null;
        __VIEWSTATEGENERATOR=null;
    }
}
