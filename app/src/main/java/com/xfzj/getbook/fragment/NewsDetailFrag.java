package com.xfzj.getbook.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.xfzj.getbook.DownLoadSevice;
import com.xfzj.getbook.R;
import com.xfzj.getbook.async.BaseAsyncTask;
import com.xfzj.getbook.db.DownLoadFileManager;
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.net.HttpHelper;
import com.xfzj.getbook.utils.MyLog;
import com.xfzj.getbook.utils.MyToast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by zj on 2016/3/21.
 */
public class NewsDetailFrag extends Fragment implements View.OnClickListener {

    public static final String PARAM = "NewsDetailFrag.class";
    private static final String TEXTVIEWCONTENT = "textviewcontetn";
    private String param;


    private TextView tvContent;
    private LinearLayout llError;
    private Button btn;
    private String content;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_detail, null);
        tvContent = (TextView) view.findViewById(R.id.tvContent);
        llError = (LinearLayout) view.findViewById(R.id.llError);
        btn = (Button) view.findViewById(R.id.btn);
        btn.setOnClickListener(this);
        if (null != savedInstanceState) {
            String cs = savedInstanceState.getString(TEXTVIEWCONTENT);
            if (TextUtils.isEmpty(cs)) {
                getActivity().finish();
            } else {
                setContent(Html.fromHtml(cs));
            }
        } else {
            getNews();
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        getNews();


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (tvContent.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(content)) {
            outState.putString(TEXTVIEWCONTENT, content);
        }
        super.onSaveInstanceState(outState);
    }

    private void getNews() {
        GetNewsDetailAsync getNewsDetailAsync = new GetNewsDetailAsync(getActivity());
        getNewsDetailAsync.execute(param);
    }

    private class GetNewsDetailAsync extends BaseAsyncTask<String, Void, Spanned> {
        public GetNewsDetailAsync(Context context) {
            super(context);
            setProgressDialog(null, getString(R.string.loading),true);
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
                MyLog.print("qwe", url);
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

                    DownLoadFileManager dm = new DownLoadFileManager(getActivity());
                    if (dm.find(sb.toString())) {
                        MyToast.show(getActivity(), sb.toString() + getActivity().getString(R.string.has_download));
                    } else {
                        Intent intent = new Intent(getActivity(), DownLoadSevice.class);
                        intent.putExtra(DownLoadSevice.DOWNLOADURL, link);
                        intent.putExtra(DownLoadSevice.DOWNLOADFILENAME, sb.toString());
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

}
