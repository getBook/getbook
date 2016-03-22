package com.xfzj.getbook;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

import com.xfzj.getbook.common.DownloadFile;
import com.xfzj.getbook.db.DownLoadFileManager;
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.net.HttpHelper;
import com.xfzj.getbook.utils.MyToast;
import com.xfzj.getbook.utils.MyUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownLoadSevice extends Service {
    public static final String DOWNLOADURL = "downurl";
    public static final String DOWNLOADFILENAME = "downfilename";
    private Notification notification;
    private NotificationManager manager;
    private String uri;
    private String filename;
    private File file;
    private FileOutputStream fos;
    private DownloadFile downloadFile;
    private static final int COMPLETED = 1, DOWNLOADING = 2,
            DOWNLOADFAILED = 3, NO_FILE = 4;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case COMPLETED:

                    notification.contentView.setTextViewText(R.id.tvProgress,
                            "100%");
                    notification.contentView.setProgressBar(R.id.pb, 100, 100,
                            false);
                    manager.notify(0, notification);
                    MyToast.show(getApplicationContext(), "下载完成");
                    if (uri.contains("doc")||uri.contains("docx")) {
                        downloadFile = new DownloadFile(file.getPath(), filename,
                                R.mipmap.word);
                    } else if (uri.contains("xls")||uri.contains("xlsx")) {
                        downloadFile = new DownloadFile(file.getPath(), filename,
                                R.mipmap.excel);
                    } else if (uri.contains("ppt")||uri.contains("pptx")) {
                        downloadFile = new DownloadFile(file.getPath(), filename,
                                R.mipmap.powerpoint);
                    } else if(uri.contains("jpg")||uri.contains("png")||uri.contains("jpeg")) {
                        downloadFile = new DownloadFile(file.getPath(), filename,
                                R.mipmap.picture);
                    }else{
                        downloadFile = new DownloadFile(file.getPath(), filename,
                                R.mipmap.office);
                    }
                    DownLoadFileManager downLoadFileManager = new DownLoadFileManager(
                            getApplicationContext());
                    downLoadFileManager.insert(downloadFile);

                    break;

                case DOWNLOADING:

                    break;
                case DOWNLOADFAILED:
                    MyToast.show(getApplicationContext(), "下载失败");
                    break;
                case NO_FILE:
                    MyToast.show(getApplicationContext(), "未发现存储卡，下载失败");
                    break;
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == intent) {
            return START_NOT_STICKY;
        }
        uri = intent.getStringExtra(DOWNLOADURL);
        if (null == uri || "".equals(uri)) {

            stopSelf();
        }
        try {
            if (!uri.contains("http")) {
                uri = BaseHttp.DOWNLOADHOST + uri;
            }
            int first=uri.lastIndexOf("/")+1;
            int last=uri.lastIndexOf(".");
            String str=uri.substring(first, last);
            Pattern pattern = Pattern.compile("[\\u4e00-\\u9faf]\\S+[\\u4e00-\\u9faf]");
            Matcher matcher = pattern.matcher(str);

            if (matcher.find()) {
                str=URLEncoder.encode(str, "utf-8");
                uri=uri.substring(0, first)+str+uri.substring(last, uri.length());
                uri = uri.replaceAll("\\+", "%20");
            }
            filename = intent.getStringExtra(DOWNLOADFILENAME);


            new AsyncTask<Void, Integer, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        byte[] bytes = new HttpHelper().DoConnection(uri);
                        fos.write(bytes);
                        fos.flush();
                        fos.close();
                        Message msg = handler.obtainMessage();
                        msg.what = COMPLETED;
                        handler.sendMessage(msg);


                    } catch (Exception e) {
                        Message msg = handler.obtainMessage();
                        msg.what = DOWNLOADFAILED;
                        handler.sendMessage(msg);
                        e.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    if (Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {

                        try {
                            file = getDownloadFile();
                            fos = new FileOutputStream(file);
                            Intent intent = new Intent("android.intent.action.VIEW");
                            if(MyUtils.isPicture(uri)){
                                intent.setDataAndType(Uri.fromFile(file),
                                        "image/*");
                            }else{
                                intent.setDataAndType(Uri.fromFile(file),
                                        "application/*");
                            }
                            PendingIntent PIntent = PendingIntent.getActivity(
                                    getApplicationContext(), 0, intent, 0);
                            notification = new Notification();
                            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notification.contentView = new RemoteViews(
                                    getPackageName(), R.layout.notification);
                            notification.icon = R.mipmap.ic_launcher;
                            notification.tickerText = "开始下载";
                            notification.contentIntent = PIntent;
                            notification.contentView.setTextViewText(
                                    R.id.tvFileName, filename);
                            manager.notify(0, notification);
                        } catch (FileNotFoundException e) {
                            MyToast.show(getApplicationContext(), "创建文件失败");
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                            MyToast.show(getApplicationContext(), "写入文件失败");
                        }

                    } else {
                        MyToast.show(getApplicationContext(), "未发现存储卡，下载失败");
                    }

                }

                @Override
                protected void onPostExecute(Void result) {
                    super.onPostExecute(result);
                    stopSelf();
                }

                @Override
                protected void onProgressUpdate(Integer... values) {

                    notification.contentView.setTextViewText(R.id.tvProgress,
                            String.valueOf(values[0]));
                    notification.contentView.setProgressBar(R.id.pb, 100,
                            values[0], false);
                    manager.notify(0, notification);

                    super.onProgressUpdate(values);
                }

            }.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }


    public  File getDownloadFile() throws IOException {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file=Environment.getExternalStorageDirectory();
            File file1 = new File(file.getPath() + "/getBookdownloads");

            if (!file1.exists()) {
                file1.mkdirs();
            }

            String flag= MyUtils.getFlag(uri);
            File file2 = new File(file1, filename+flag);
            if (!file2.exists()) {
                file2.createNewFile();
            }
            return file2;
        }
        return null;
    }
}
