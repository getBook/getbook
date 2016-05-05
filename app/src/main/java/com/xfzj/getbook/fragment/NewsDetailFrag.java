package com.xfzj.getbook.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.xfzj.getbook.DownLoadSevice;
import com.xfzj.getbook.R;
import com.xfzj.getbook.async.BaseAsyncTask;
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.net.HttpHelper;
import com.xfzj.getbook.utils.AppAnalytics;
import com.xfzj.getbook.utils.FileUtils;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.utils.ShareUtils;
import com.xfzj.getbook.views.view.BaseScrollView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by zj on 2016/3/21.
 */
public class NewsDetailFrag extends BaseFragment implements View.OnClickListener, BaseScrollView.OnScrollCallBack {

    public static final String PARAM = "NewsDetailFrag.class";
    private static final String TEXTVIEWCONTENT = "textviewcontetn";
    private String param;


    private TextView tvContent;
    private LinearLayout llError;
    private Button btn;
    private String content;
    private String href;

    private FloatingActionButton fab;
    private BaseScrollView scrollView;
    /**
     * 通知栏的id
     */
    private int i=0;
    private String title;

    public NewsDetailFrag() {

    }


    public static NewsDetailFrag newInstance(String param) {
        NewsDetailFrag newsDetailFrag = new NewsDetailFrag();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM, param);
        newsDetailFrag.setArguments(bundle);
        return newsDetailFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            param = getArguments().getString(PARAM);
        }


    }

    public void setFab(FloatingActionButton fab) {
        this.fab = fab;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_detail, container, false);
        scrollView = (BaseScrollView) view.findViewById(R.id.scrollView);
        scrollView.setOnScrollCallBack(this);

        tvContent = (TextView) view.findViewById(R.id.tvContent);
        llError = (LinearLayout) view.findViewById(R.id.llError);
        btn = (Button) view.findViewById(R.id.btn);
        btn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void onClick(View v) {
        getNews();
    }


    public void setHref(String href) {
        this.href = href;
        getNews();
    }

    public void getNews() {
        GetNewsDetailAsync getNewsDetailAsync = new GetNewsDetailAsync(getActivity());
        getNewsDetailAsync.execute(href);
    }

    @Override
    public void onScroll(boolean b) {
        if (null == fab) {
            return;
        }
        if (b) {
            fab.setVisibility(View.GONE);
        } else {
            fab.setVisibility(View.VISIBLE);
        }
    }

    public void shareNews() {
        String url = BaseHttp.GETNEWSITEM + href;
        String text = getActivity().getString(R.string.use_getfun_read, title);
        ShareUtils.share(getActivity(), text, title, url, R.mipmap.nuist);
    }

    public void setTitle(String title) {
        this.title = title;
    }


    private class GetNewsDetailAsync extends BaseAsyncTask<String, Void, Spanned> {
        public GetNewsDetailAsync(Context context) {
            super(context);
            setProgressDialog(null, getString(R.string.loading), true);
        }

        @Override
        protected void onPost(Spanned s) {
            if (null == s) {
                tvContent.setVisibility(View.GONE);
                llError.setVisibility(View.VISIBLE);


            } else {
                tvContent.setVisibility(View.VISIBLE);
                llError.setVisibility(View.GONE);
                setContent(s);
            }


        }


        @Override
        protected Spanned doExcute(String[] params) {
            try {
                String url = BaseHttp.GETNEWSITEM + params[0];
                byte[] bytes = new HttpHelper().DoConnection(url);
                String result = new String(bytes, "utf-8");
                Document document = Jsoup.parse(result);
                NewsDetailFrag.this.content = document.getElementById("xw").toString();
                return Html.fromHtml(NewsDetailFrag.this.content);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }
    }

    private void setContent(Spanned s) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(s);
        URLSpan[] urlSpans = spannableStringBuilder.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan urlSpan : urlSpans) {
            int start = spannableStringBuilder.getSpanStart(urlSpan);
            int end = spannableStringBuilder.getSpanEnd(urlSpan);
            int flag = spannableStringBuilder.getSpanFlags(urlSpan);
            final String link = urlSpan.getURL();
            final CharacterStyle cs = urlSpan.getUnderlying();
            final String sb = spannableStringBuilder.subSequence(start, end).toString();
            spannableStringBuilder.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    AppAnalytics.onEvent(getActivity(), AppAnalytics.C_SA_DOWN);
                    if (isDownloaded(sb.toString())) {
                        MyToast.show(getActivity(), sb.toString() + getActivity().getString(R.string.has_download));
                    } else {
                        Intent intent = new Intent(getActivity(), DownLoadSevice.class);
                        intent.putExtra(DownLoadSevice.DOWNLOADURL, link);
                        intent.putExtra(DownLoadSevice.DOWNLOADFILENAME, sb.toString());
                        intent.putExtra(DownLoadSevice.NOTIFY, i++);
                        getActivity().startService(intent);

                    }
                }
            }, start, end, flag);
            spannableStringBuilder.removeSpan(urlSpan);

        }
        tvContent.setLinksClickable(true);
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        tvContent.setText(spannableStringBuilder);
    }

    private boolean isDownloaded(final String s) {
        File f = FileUtils.getDownloadLibrary(getActivity());
        if (!f.exists() || TextUtils.isEmpty(s)) {
            return false;
        }
        File[] fs = f.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.getName().contains(s)) {
                    return true;
                }
                return false;
            }
        });
        if (null == fs || fs.length == 0) {
            return false;
        }
        return true;


    }


    public void setFloatingBUtton(FloatingActionButton fab) {
        this.fab = fab;
    }

}
