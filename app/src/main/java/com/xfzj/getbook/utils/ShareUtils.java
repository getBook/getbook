package com.xfzj.getbook.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.xfzj.getbook.R;

/**
 * Created by zj on 2016/5/4.
 */
public class ShareUtils {
    public static String otherUrl = "http://getfun.bmob.cn/";
    public static String wxUrl = "http://a.app.qq.com/o/simple.jsp?pkgname=com.xfzj.getbook";
    private static ShareContent wxShareContent = new ShareContent();

    private static ShareContent ShareContent = new ShareContent();
    static final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
            {
                    SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA,
                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
            };

    static final ShareContent[] shareContents = new ShareContent[]
            {
                    wxShareContent, wxShareContent, ShareContent,
                    ShareContent, ShareContent
            };

    public static void shareDefautl(final Activity activity) {
        String title = activity.getString(R.string.app_name);
        String text = title + "——南京信息工程大学第三方App,二手交易、校园公告、一卡通、查成绩、图书馆功能一应俱全！";
        String[] url = new String[2];
        url[0] = otherUrl;
        url[1] = wxUrl;
        share(activity, text, title, url, R.mipmap.ic_launcher, new UMShareListener() {
            @Override
            public void onResult(SHARE_MEDIA share_media) {
                MyToast.show(activity, activity.getString(R.string.share_succ));
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                MyToast.show(activity, activity.getString(R.string.share_fail));
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {

            }
        });
    }
    public static void share(final Activity activity, String text, String title, int bitmapId) {
        share(activity, text, title, new String[]{otherUrl, wxUrl}, bitmapId, new UMShareListener() {
            @Override
            public void onResult(SHARE_MEDIA share_media) {
                MyToast.show(activity, activity.getString(R.string.share_succ));
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                MyToast.show(activity, activity.getString(R.string.share_fail));
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {

            }
        });
    }

    public static void share(Activity activity, String text, String title, String[] url, String imageUrl, UMShareListener umShareListener) {

        UMImage umImage = new UMImage(activity, imageUrl);
        share(activity, text, title, url, umImage, umShareListener);
    }

    public static void share(Activity activity, String text, String title, String[] url, int bitmapId, UMShareListener umShareListener) {

        UMImage umImage = new UMImage(activity, BitmapFactory.decodeResource(activity.getResources(), bitmapId));
        share(activity, text, title, url, umImage, umShareListener);
    }
    public static void share(final Activity activity, String text, String title, String[] url, int bitmapId) {

        UMImage umImage = new UMImage(activity, BitmapFactory.decodeResource(activity.getResources(), bitmapId));
        share(activity, text, title, url, umImage, new UMShareListener() {
            @Override
            public void onResult(SHARE_MEDIA share_media) {
                MyToast.show(activity, activity.getString(R.string.share_succ));
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                MyToast.show(activity, activity.getString(R.string.share_fail));
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {

            }
        });
    }
    public static void share(final Activity activity, String text, String title, String url, int bitmapId) {
        share(activity, text, title, new String[]{url, url}, bitmapId, new UMShareListener() {
            @Override
            public void onResult(SHARE_MEDIA share_media) {
                MyToast.show(activity, activity.getString(R.string.share_succ));
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                MyToast.show(activity, activity.getString(R.string.share_fail));
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {

            }
        });
    }
    public static void share(Activity activity, String text, String title, String url, String imageUrl, UMShareListener umShareListener) {
        share(activity, text, title, new String[]{url, url}, imageUrl, umShareListener);
    }

    public static void share(Activity activity, String text, String title, String url, int bitmapId, UMShareListener umShareListener) {
        share(activity, text, title, new String[]{url, url}, bitmapId, umShareListener);
    }

    public static void share(Activity activity, String text, String title, String imageUrl, UMShareListener umShareListener) {
        share(activity, text, title, new String[]{otherUrl, wxUrl}, imageUrl, umShareListener);
    }

    public static void share(Activity activity, String text, String title, int bitmapId, UMShareListener umShareListener) {
        share(activity, text, title, new String[]{otherUrl, wxUrl}, bitmapId, umShareListener);
    }

    public static void share(Activity activity, String text, String imageUrl, UMShareListener umShareListener) {
        share(activity, text, activity.getString(R.string.app_name), new String[]{otherUrl, wxUrl}, imageUrl, umShareListener);
    }

    public static void share(Activity activity, String text, int bitmapId, UMShareListener umShareListener) {
        share(activity, text, activity.getString(R.string.app_name), new String[]{otherUrl, wxUrl}, bitmapId, umShareListener);
    }

    public static void share(Activity activity, String text, Bitmap bitmap, UMShareListener umShareListener) {
        share(activity, text, activity.getString(R.string.app_name), bitmap, umShareListener);
    }
    public static void share(Activity activity, String text,String title, Bitmap bitmap, UMShareListener umShareListener) {
        UMImage umImage = new UMImage(activity, bitmap);
        share(activity, text, title, new String[]{otherUrl, wxUrl}, umImage, umShareListener);
    }
    public static void share(Activity activity, String text, String title, String[] url, UMImage umImage, UMShareListener umShareListener) {
        
        ShareContent.mTargetUrl = url[0];
        ShareContent.mText = text;
        ShareContent.mTitle = title;
        ShareContent.mMedia = umImage;
        wxShareContent.mText = text;
        wxShareContent.mTitle = title;
        wxShareContent.mMedia = umImage;
        wxShareContent.mTargetUrl = url[1];
        new ShareAction(activity).setDisplayList(displaylist).setContentList(shareContents)
                .withMedia(umImage)
                .setListenerList(umShareListener)
                .open();

    }
}
