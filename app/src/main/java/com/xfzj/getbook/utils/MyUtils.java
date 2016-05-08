package com.xfzj.getbook.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MyUtils {

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }
    /**
     * 计算字符数
     *
     * @param c
     * @return
     */
    public static long calculateLength(CharSequence c) {
        if (null == c) {
            return 0;
        }
        c = c.toString().replaceAll("[em]+\\S+[/em]", "1");
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }

    /**
     * 获取淡处理的颜色
     * @param color
     * @return
     */
    public static int getBrighterColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv); // convert to hsv

        hsv[1] = hsv[1] - 0.5f; // less saturation
        hsv[2] = hsv[2] + 0.5f; // more brightness
        int darkerColor = Color.HSVToColor(hsv);
        return darkerColor;
    }

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DisplayMetrics getScreenMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarColor(Window window, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (color != Color.TRANSPARENT) {
                window
                        .getDecorView()
                        .setSystemUiVisibility(
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

            }
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            );
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    public static int getScreenWidth(Context context) {
        return getScreenMetrics(context).widthPixels;
    }

    public static float dp2px(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static boolean checkDeviceHasNavigationBar(Context activity) {

        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar  
        boolean hasMenuKey = ViewConfiguration.get(activity)
                .hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap
                .deviceHasKey(KeyEvent.KEYCODE_BACK);

        if (!hasMenuKey && !hasBackKey) {
            return true;
        }
        return false;
    }

    public static int getNavigationBarHeight(Context activity) {
        if (checkDeviceHasNavigationBar(activity)) {
            Resources resources = activity.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height",
                    "dimen", "android");
            //获取NavigationBar的高度  
            int height = resources.getDimensionPixelSize(resourceId);
            return height;
        }
        return 0;
    }


    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public static void executeInThread(Runnable runnable) {
        new Thread(runnable).start();
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        boolean externalStorageAvailable = Environment
                .getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        final String cachePath;
        if (externalStorageAvailable) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }

        return new File(cachePath + File.separator + uniqueName);
    }
    /***/
    /**
     * 图片去色,返回灰度图片
     *
     * @param bmpOriginal 传入的图片
     * @return 去色后的图片
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    public static String getFlag(String uri) {
        return uri.substring(uri.lastIndexOf("."), uri.length());
    }


    public static boolean isPicture(String uri) {
        String flag = getFlag(uri);
        String[] str = new String[]{".jpg", ".png", ".jpeg"};
        for (int i = 0; i < str.length; i++) {
            if (str[0].contains(flag.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isFile(String uri) {
        String flag = getFlag(uri);
        String[] str = new String[]{".doc", ".xls", ".ppt", ".docx", "xlsx", ".pdf", ".pptx"};
        for (int i = 0; i < str.length; i++) {
            if (str[0].contains(flag.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWord(String uri) {
        String flag = getFlag(uri);
        if (flag.toLowerCase().contains(".doc") || flag.toLowerCase().contains(".docx")) {
            return true;
        }
        return false;
    }

    public static boolean isExcel(String uri) {
        String flag = getFlag(uri);
        if (flag.toLowerCase().contains(".xls") || flag.toLowerCase().contains(".xlsx")) {
            return true;
        }
        return false;
    }

    public static boolean isSuccess(JSONObject jsonObject) {

        try {

            return jsonObject.getBoolean("success");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
