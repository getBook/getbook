package com.xfzj.getbook.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import com.xfzj.getbook.R;
import com.xfzj.getbook.views.view.BaseToolBar;

import butterknife.Bind;

/**
 * Created by zj on 2016/6/8.
 */
public class WebViewAty extends AppActivity {
    public static final String KEY = "key";
    @Bind(R.id.pb)
    ProgressBar pb;
    @Bind(R.id.wv)
    WebView wv;
    Toolbar toolbar;
    @Bind(R.id.baseToolbar)
    BaseToolBar baseToolbar;

    @Override
    protected void onSetContentView() {
//        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_webview_aty);
        
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (null == intent) {
            finish();
            return;
        }
        String link = intent.getStringExtra(KEY);
        if (TextUtils.isEmpty(link)) {
            finish();
            return;
        }
        baseToolbar.initToolbar(WebViewAty.this, null);
        baseToolbar.canBack(WebViewAty.this);

        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setDatabaseEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setAllowFileAccess(true);
        wv.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        wv.setWebViewClient(new MyClient());
        wv.setWebChromeClient(new MyChromeClient());
        wv.loadUrl(link);
    }

    private class MyClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            wv.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {


            super.onPageStarted(view, url, favicon);
            pb.setVisibility(View.VISIBLE);
            pb.setProgress(0);


        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            pb.setProgress(100);
            pb.setVisibility(View.GONE);
            baseToolbar.initToolbar(WebViewAty.this, view.getTitle());
        }
    }


    private class MyChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            pb.setVisibility(View.VISIBLE);
            pb.setProgress(newProgress);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wv.clearCache(true);
        wv.clearFormData();
        wv.clearHistory();
    }
}
