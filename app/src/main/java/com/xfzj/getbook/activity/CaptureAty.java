package com.xfzj.getbook.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.result.ResultHandler;
import com.google.zxing.client.result.ParsedResultType;
import com.xfzj.getbook.R;
import com.xfzj.getbook.fragment.RecommendFrag;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.utils.MyUtils;
import com.xfzj.getbook.views.view.BaseToolBar;

/**
 * Created by zj on 2016/2/27.
 */
public class CaptureAty extends CaptureActivity implements Toolbar.OnMenuItemClickListener {
    BaseToolBar baseToolBar;
    Toolbar toolbar;
    public static final String FROM = "CaptureAty.class";

    @Override
    public void onCreate(Bundle icicle) {

        super.onCreate(icicle);
        baseToolBar = (BaseToolBar) findViewById(R.id.baseToolbar);
        if (null != baseToolBar) {
            baseToolBar.initToolbar(this, getString(R.string.sao_yi_sao));
            toolbar = baseToolBar.getToolbar();
            toolbar.setOnMenuItemClickListener(this);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected int[] setFrameWidthHeight() {
        int width = MyUtils.getScreenWidth(getApplicationContext());
        return new int[]{width * 4 / 5, width * 3 / 5};
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
            return true;
        }
        return false;
    }

    @Override
    protected void handleDecodeInternally(Result rawResult, ResultHandler resultHandler, Bitmap barcode) {
        restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);
        if (null == rawResult || null == resultHandler) {
            MyToast.show(getApplicationContext(), "扫码失败，请重试");
            return;
        }
        if (null != resultHandler.getType() && resultHandler.getType().toString().equals(ParsedResultType.ISBN.toString())) {
            if (TextUtils.isEmpty(rawResult.getText())) {
                MyToast.show(getApplicationContext(), "扫码失败，请重试");
                return;
            }
            String from = getIntent().getStringExtra(FROM);
            if (TextUtils.isEmpty(from)) {
                startPublishActivity(rawResult.getText());
            } else if (from.equals(RecommendFrag.class.getName())) {
                startRecommendFrag(rawResult.getText());
            }


        } else {
            MyToast.show(getApplicationContext(), "暂时只能识别书本的条形码哦");
        }


    }

    private void startRecommendFrag(String text) {
        Intent intent = new Intent(this, PublishSecondBookActivity.class);
        intent.putExtra(RecommendFrag.ISBN, text);
        setResult(RecommendFrag.SCAN, intent);
        finish();
    }

    private void startPublishActivity(String text) {
        Intent intent = new Intent(this, PublishSecondBookActivity.class);
        intent.putExtra(com.xfzj.getbook.activity.PublishSecondBookActivity.ISBN, text);
        intent.putExtra(com.xfzj.getbook.activity.PublishSecondBookActivity.FROM, FROM);
        startActivity(intent);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
