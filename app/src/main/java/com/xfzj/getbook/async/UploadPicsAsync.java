package com.xfzj.getbook.async;

import android.content.Context;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadBatchListener;
import com.xfzj.getbook.R;
import com.xfzj.getbook.common.SecondBook;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by zj on 2016/2/28.
 */
public class UploadPicsAsync extends BaseAsyncTask<Void,Void,Void> {

    private SecondBook secondBook;
    private Context context;
    private SaveListener saveListener;
    public UploadPicsAsync(Context context, SecondBook secondBook) {
        
        super(context);
        this.context = context;
        this.secondBook = secondBook;
        setProgressDialog("", context.getString(R.string.publishing));
    }

    public void setSaveListener(SaveListener saveListener) {
        this.saveListener = saveListener;
    }

    @Override
    protected void onPost(Void aVoid) {
        
    }

    @Override
    protected Void doExcute(Void[] params) {
        BmobProFile.getInstance(context).uploadBatch(secondBook.getPictures(), new UploadBatchListener() {
            @Override
            public void onSuccess(boolean b, String[] strings, String[] strings1, BmobFile[] bmobFiles) {
                secondBook.setPictures(strings);
                secondBook.save(context, saveListener);
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {

            }

            @Override
            public void onError(int i, String s) {

            }
        });
        
        
        
        return null;
    }
}
