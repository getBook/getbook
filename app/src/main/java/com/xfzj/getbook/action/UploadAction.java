package com.xfzj.getbook.action;

import android.app.ProgressDialog;
import android.content.Context;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadBatchListener;
import com.xfzj.getbook.R;
import com.xfzj.getbook.common.BookInfo;
import com.xfzj.getbook.common.SecondBook;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.utils.MyUtils;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by zj on 2016/2/28.
 */
public class UploadAction extends BaseAction {
    private Context context;
    private SecondBook secondBook;
    private ProgressDialog pd;
    private BookInfo bookInfo;

    public UploadAction(Context context, SecondBook secondBook, BookInfo bookInfo) {

        this.context = context;
        this.secondBook = secondBook;
        this.bookInfo = bookInfo;
        setProgressDialog(context.getString(R.string.publishing));
    }

    private void setProgressDialog(String string) {
        pd = ProgressDialog.show(context, "", string);
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
    }

    public void upload(final UploadListener uploadListener) {

        pd.show();
        File file = MyUtils.getDiskCacheDir(context, bookInfo.getIsbn() + ".jpg");

        BmobProFile.getInstance(context).upload(file.getPath(), new com.bmob.btp.callback.UploadListener() {
            @Override
            public void onSuccess(String s, String s1, BmobFile bmobFile) {
                bookInfo.setImage(s);
                secondBook.setBookInfo(bookInfo);
                bookInfo.save(context, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        uploadSecondBook(uploadListener);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        MyToast.show(context, "发布失败，请重试"+i+s);
                        onFail(uploadListener);
                    }
                });

            }

            @Override
            public void onProgress(int i) {

            }

            @Override
            public void onError(int i, String s) {
                MyToast.show(context, "上传图片失败，请重试");
                onFail(uploadListener);
            }
        });


    }

    private void onSucc(UploadListener uploadListener) {
        if (pd.isShowing()) {
            pd.dismiss();
        }
        if (null != uploadListener) {
            uploadListener.onSuccess();

        }
    }

    private void onFail(UploadListener uploadListener) {
        if (pd.isShowing()) {
            pd.dismiss();
        }
        if (null != uploadListener) {
            uploadListener.onFail();
        }
    }

    private void uploadSecondBook(final UploadListener uploadListener) {
        BmobProFile.getInstance(context).uploadBatch(secondBook.getPictures(), new UploadBatchListener() {
            @Override
            public void onSuccess(boolean b, String[] strings, String[] strings1, BmobFile[] bmobFiles) {
                if (b) {
                    secondBook.setPictures(strings);
                    secondBook.save(context, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            onSucc(uploadListener);
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            MyToast.show(context, "发布失败，请重试");
                            onFail(uploadListener);
                        }
                    });
                }
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {
            }

            @Override
            public void onError(int i, String s) {
                MyToast.show(context, "上传图片失败，请重试"+i+s);
                onFail(uploadListener);
            }
        });
    }


    public interface UploadListener {
        void onSuccess();

        void onFail();
    }


}
