package com.xfzj.getbook.async;

import android.content.Context;

import com.xfzj.getbook.common.BookInfo;
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.net.HttpHelper;

/**
 * Created by zj on 2016/2/29.
 */
public class GetBookInfoAsync extends BaseAsyncTask<String, Void, BookInfo> {

    private String url= BaseHttp.GetBookInfo;
    
    public GetBookInfoAsync(Context context) {
        super(context);
    }
    
    

    @Override
    protected void onPost(BookInfo bookInfo) {
        if (null != bookInfo) {
            if (null != onTaskListener) {
                onTaskListener.onSuccess(bookInfo);
                
            }
        }else{
            if (null != onTaskListener) {
                onTaskListener.onFail();
            }
        }


    }

    @Override
    protected BookInfo doExcute(String[] params) {
        try {
            byte[] bytes = new HttpHelper().DoConnection(url + params[0]);
            if (null == bytes) {
                return null;
            } else {
                String str = new String(bytes, "utf-8");
                BookInfo bookInfo = gson.fromJson(str, BookInfo.class);
                bookInfo.setIsbn(params[0]);
                return bookInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    
}
