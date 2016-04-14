package com.xfzj.getbook.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.xfzj.getbook.R;

/**
 * Created by zj on 2016/4/10.
 */
public class AlertDialogUtils {
    private AlertDialog.Builder builder;

    /**
     * 创建正常模式的对话框，包含标题，内容，确定，取消
     * @param context
     * @param title
     * @param message
     */
    public void buildMode1(Context context, String title, String message) {
        builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
    }

    /**
     * 创建菜单模式的对话框，包括菜单选择
     * @param context
     * @param items
     * @param title
     * @param onClickListener
     */
    public void buildMode2(Context context, String[] items, String title, DialogInterface.OnClickListener onClickListener) {
        builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(items,onClickListener);
    }
    public void setCancelable(boolean b) {
        builder.setCancelable(b);
    }

    public void setContentView(View view) {
        builder.setView(view);
    }

    public void show() {
        builder.create().show();
    }

    public void setPositiveClick(DialogInterface.OnClickListener click) {
        builder.setPositiveButton(R.string.ensure, click);
        
    }
    public void setNegativeClick(DialogInterface.OnClickListener click) {
        builder.setNegativeButton(R.string.cancel, click);

    }
}
