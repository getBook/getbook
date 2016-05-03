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
import com.xfzj.getbook.net.BaseHttp;
import com.xfzj.getbook.net.HttpHelper;
import com.xfzj.getbook.utils.FileUtils;
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
    public static final String NOTIFY = "notify";
    /**
     * 通知栏的id
     */
    private int i;
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
                    manager.notify(i, notification);
                    MyToast.show(getApplicationContext(), getString(R.string.downoad_complete));
                    if (uri.contains("doc") || uri.contains("docx")) {
                        downloadFile = new DownloadFile(file.getPath(), filename,
                                0);
                    } else if (uri.contains("xls") || uri.contains("xlsx")) {
                        downloadFile = new DownloadFile(file.getPath(), filename,
                                1);
                    } else if (uri.contains("ppt") || uri.contains("pptx")) {
                        downloadFile = new DownloadFile(file.getPath(), filename,
                                2);
                    } else if (uri.contains("jpg") || uri.contains("png") || uri.contains("jpeg")) {
                        downloadFile = new DownloadFile(file.getPath(), filename,
                                3);
                    } else {
                        downloadFile = new DownloadFile(file.getPath(), filename,
                                4);
                    }
                    break;

                case DOWNLOADING:

                    break;
                case DOWNLOADFAILED:
                    MyToast.show(getApplicationContext(), getString(R.string.download_fail));
                    break;
                case NO_FILE:
                    MyToast.show(getApplicationContext(), getString(R.string.no_sdcard_fail));

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
        i = intent.getIntExtra(NOTIFY,0);
        uri = intent.getStringExtra(DOWNLOADURL);
        if (null == uri || "".equals(uri)) {

            stopSelf();
        }
        try {
            if (!uri.contains("http")) {
                uri = BaseHttp.DOWNLOADHOST + uri;
            }
            int first = uri.lastIndexOf("/") + 1;
            int last = uri.lastIndexOf(".");
            String str = uri.substring(first, last);
            Pattern pattern = Pattern.compile("[\\u4e00-\\u9faf]\\S+[\\u4e00-\\u9faf]");
            Matcher matcher = pattern.matcher(str);

            if (matcher.find()) {
                str = URLEncoder.encode(str, "utf-8");
                uri = uri.substring(0, first) + str + uri.substring(last, uri.length());
                uri = uri.replaceAll("\\+", "%20");
            }
            filename = intent.getStringExtra(DOWNLOADFILENAME);


            new AsyncTask<Void, Integer, Void>() {

                @Override
                protected Void doInBackground(Void... params) {

                    if (isCancelled()) {
                        return null;
                    }
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
                            file = FileUtils.getDownloadFile(getApplicationContext(), uri, filename);
                            fos = new FileOutputStream(file);
                            Intent intent = new Intent("android.intent.action.VIEW");
                            if (MyUtils.isPicture(uri)) {
                                intent.setDataAndType(Uri.fromFile(file), "image/*");
                            } else if (MyUtils.isWord(uri)) {
                                intent.setDataAndType(Uri.fromFile(file), "application/msword");
                            } else if (MyUtils.isExcel(uri)) {
                                intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
                            } else {
                                intent.setDataAndType(Uri.fromFile(file), "application/*");
                            }
                            PendingIntent PIntent = PendingIntent.getActivity(
                                    getApplicationContext(), 0, intent, 0);
                            Notification.Builder builder = new Notification.Builder(getApplicationContext());
                            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            builder.setContent(new RemoteViews(
                                    getPackageName(), R.layout.notification));
                            builder.setSmallIcon(R.mipmap.ic_launcher);
                            builder.setTicker(getString(R.string.start_downloading));
                            builder.setContentIntent(PIntent);
                            builder.setOngoing(true);
                            notification = builder.build();
                            notification.flags = Notification.FLAG_AUTO_CANCEL;

                            notification.contentView.setTextViewText(
                                    R.id.tvFileName, filename);
                            manager.notify(i, notification);
                        } catch (FileNotFoundException e) {
                            MyToast.show(getApplicationContext(), getString(R.string.create_file_fail));
                            cancel(true);
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                            cancel(true);
                            MyToast.show(getApplicationContext(), getString(R.string.write_file_fail));
                        }

                    } else {
                        MyToast.show(getApplicationContext(), getString(R.string.no_sdcard_fail));
                        cancel(true);
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
                    manager.notify(i, notification);

                    super.onProgressUpdate(values);
                }

            }.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
