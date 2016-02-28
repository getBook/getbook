package com.xfzj.getbook.action;

import android.app.ProgressDialog;
import android.content.Context;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadBatchListener;
import com.xfzj.getbook.R;
import com.xfzj.getbook.common.SecondBook;
import com.xfzj.getbook.utils.MyToast;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by zj on 2016/2/28.
 */
public class UploadPicAction extends BaseAction {
    private Context context;
    private SecondBook secondBook;
    private ProgressDialog pd;

    public UploadPicAction(Context context, SecondBook secondBook) {
        this.context = context;
        this.secondBook = secondBook;
        setProgressDialog(context.getString(R.string.publishing));
    }

    private void setProgressDialog(String string) {
        pd = ProgressDialog.show(context, "", string);
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
    }

    public void upload() {
        pd.show();
        BmobProFile.getInstance(context).uploadBatch(secondBook.getPictures(), new UploadBatchListener() {
            @Override
            public void onSuccess(boolean b, String[] strings, String[] strings1, BmobFile[] bmobFiles) {
                if (b) {
                secondBook.setPictures(strings);
                secondBook.save(context, new UploadListener());
                }
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {
            }

            @Override
            public void onError(int i, String s) {
                MyToast.show(context, "上传图片失败，请重试" + i + "   " + s);
                pd.dismiss();
            }
        });


    }


    private class UploadListener extends SaveListener {

        @Override
        public void onSuccess() {
            pd.dismiss();
            MyToast.show(context, "发布成功");
        }

        @Override
        public void onFailure(int i, String s) {
            pd.dismiss();
            MyToast.show(context, "发布失败" + i + "   " + s);
        }
    }
}
