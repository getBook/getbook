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
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.views.view.BaseToolBar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zj on 2016/2/27.
 */
public class CaptureAty extends CaptureActivity implements Toolbar.OnMenuItemClickListener {
    @Bind(R.id.baseToolbar)
    BaseToolBar baseToolBar;
    Toolbar toolbar;
    public static final String FROM = "CaptureAty.class";
    @Override
    public void onCreate(Bundle icicle) {
        
        super.onCreate(icicle);
        ButterKnife.bind(this);
        baseToolBar.initToolbar(this, getString(R.string.sao_yi_sao));
        toolbar = baseToolBar.getToolbar();
        toolbar.setOnMenuItemClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
    }

    @Override
    protected void onResume() {
        super.onResume();
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

            startActivity(rawResult.getText());


        }else {
            MyToast.show(getApplicationContext(), "暂时只能识别书本的条形码哦");
        }
        

    }

    private void startActivity(String text) {
        Intent intent = new Intent(this, PublishSecondBookActivity.class);
        intent.putExtra(PublishSecondBookActivity.ISBN, text);
        intent.putExtra(PublishSecondBookActivity.FROM, FROM);
        startActivity(intent);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
