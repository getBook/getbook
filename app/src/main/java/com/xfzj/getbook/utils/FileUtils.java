package com.xfzj.getbook.utils;

import android.content.Context;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by zj on 2016/1/29.
 */
public class FileUtils {

    private Context context;

    public FileUtils(Context context) {
        this.context = context;
    }

    public String write(String fileName, String str) {
      return  write(fileName, str.getBytes());
    }

    public String write(String fileName, byte[] bytes) {
        try {

            File file = new File(context.getCacheDir(),fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String readStr(String fileName) {
        try {
            return new String(readByte(fileName), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] readByte(String fileName) {
        try {
            File file = new File(context.getFilesDir(), fileName);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = fis.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
            }
            bos.flush();
            bos.close();
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    public static  void createDownloadDir() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file=Environment.getExternalStorageDirectory();
            File path = new File(file.getPath() + "/aaadownloads");
            if (!path.exists()) {
                path.mkdirs();
            }

        }
    }
}
