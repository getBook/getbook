package com.xfzj.getbook.async;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.net.HttpHelper;
import com.xfzj.getbook.net.LibraryHttpImp;

/**
 * Created by zj on 2016/3/28.
 */
public class GetLibraryCaptureAsync extends BaseAsyncTask<String, Void, GetLibraryCaptureAsync.Capture> {

    public GetLibraryCaptureAsync(Context context) {
        super(context);
    }

    @Override
    protected void onPost(Capture c) {
        if (null != c) {
            if (null != onTaskListener) {
                onTaskListener.onSuccess(c);
            }
        } else {
            if (null != onTaskListener) {
                onTaskListener.onFail();
            }
        }


    }

    @Override
    protected Capture doExcute(String[] params) {
        try {
            LibraryHttpImp libraryHttpImp = new LibraryHttpImp();
            libraryHttpImp.setProperty("Referer", LibraryHttpImp.INDEX);
            HttpHelper httpHelper = new HttpHelper(libraryHttpImp);
            httpHelper.DoConnection(BaseHttp.LOGINLIBRARY);
            String cookie = httpHelper.getCookie();
            if (!TextUtils.isEmpty(cookie)) {
                libraryHttpImp.setProperty("Referer", LibraryHttpImp.LOGIN);
                byte[] b = httpHelper.DoConnection(BaseHttp.CAPTCHA);
                if (null != b) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                    return new Capture(httpHelper.getCookie(), bitmap);
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public class Capture {

        String cookie;
        Bitmap bitmap;

        public Capture(String cookie, Bitmap bitmap) {
            this.cookie = cookie;
            this.bitmap = bitmap;
        }

        public String getCookie() {
            return cookie;
        }

        public void setCookie(String cookie) {
            this.cookie = cookie;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }
    }
}
