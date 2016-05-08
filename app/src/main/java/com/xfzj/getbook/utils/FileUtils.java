package com.xfzj.getbook.utils;

import android.content.Context;
import android.os.Environment;

import com.xfzj.getbook.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zj on 2016/1/29.
 */
public class FileUtils {

    private Context context;

    public FileUtils(Context context) {
        this.context = context;
    }

    public String write(String fileName, String str) {
        return write(fileName, str.getBytes());
    }

    public String write(String fileName, byte[] bytes) {
        try {

            File file = new File(context.getCacheDir(), fileName);
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

    public static void createDownloadDir() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = Environment.getExternalStorageDirectory();
            File path = new File(file.getPath() + "/aaadownloads");
            if (!path.exists()) {
                path.mkdirs();
            }

        }
    }

    /**
     * 创建校园公告下载文件路径
     *
     * @return
     * @throws IOException
     */
    public static File getDownloadFile(Context context, String uri, String filename) throws IOException {
        File file1 = getDownloadLibrary(context);
        if (null == file1) {
            return null;
        }
        String flag = MyUtils.getFlag(uri);
        File file2 = new File(file1, filename + flag);
        if (!file2.exists()) {
            file2.createNewFile();
        }
        return file2;
    }

    /**
     * 创建校园公告下载文件的文件夹
     * @param context
     * @return
     */
    public static File getDownloadLibrary(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = Environment.getExternalStorageDirectory();
            File file1 = new File(file.getPath() + "/" + context.getString(R.string.app_name) + "downloads");

            if (!file1.exists()) {
                file1.mkdirs();
            }
            return file1;
        }
        return null;
    }

    /**
     * 读取表情配置文件
     *
     * @param context
     * @return
     */
    public static List<String> getEmojiFile(Context context) {
        try {
            List<String> list = new ArrayList<String>();
            InputStream in = context.getResources().getAssets().open("emoji");// �ļ�����Ϊrose.txt
            BufferedReader br = new BufferedReader(new InputStreamReader(in,
                    "UTF-8"));
            String str = null;
            while ((str = br.readLine()) != null) {
                list.add(str);
            }

            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}