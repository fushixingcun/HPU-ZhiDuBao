package com.hpu.rule.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.hpu.rule.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownLoadService extends Service {
    private NotificationManager manager = null;
    private NotificationCompat.Builder builder;
    private DownloadImageTask downloadTask;
    public File file;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("url");
        if (downloadTask != null && !downloadTask.isCancelled()) {
            Toast.makeText(getApplicationContext(), "正在下载中！", Toast.LENGTH_SHORT).show();
            return startId;
        }
        downloadTask = new DownloadImageTask();
        downloadTask.execute(url);
        return super.onStartCommand(intent, flags, startId);
    }

    private class DownloadImageTask extends AsyncTask<String, Integer, Long> {
        @Override
        protected void onPreExecute() {
            builder = new NotificationCompat.Builder(getApplicationContext());
            //builder.setSmallIcon(R.mipmap.xiaoyuan);
            //builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.xiaoyuan));
            builder.setContentTitle("制度宝");
            builder.setContentText("河南理工大学制度宝");
            builder.setProgress(100, 0, false);
            builder.setTicker("下载中...");
            manager.notify(1, builder.build());
        }

        @Override
        protected Long doInBackground(String... params) {
            URL url;
            try {
                url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                // 当前线程下载的总大小
                int total = 0;
                File positionFile =null;
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    positionFile = new File(Environment.getExternalStorageDirectory(),
                            getFileName(params[0]) + ".txt");
                } else {
                    positionFile = new File(Environment.getRootDirectory(), getFileName(params[0]) + ".txt");
                }
                // 接着从上一次的位置继续下载数据
                long startIndex = 0;
                if (positionFile.exists() && positionFile.length() > 0) {
                    FileInputStream fis = new FileInputStream(positionFile);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                    // 获取线程上次下载的总大小
                    String lastTotalstr = reader.readLine();
                    int lastTotal = Integer.parseInt(lastTotalstr);
                    startIndex += lastTotal;
                    total += lastTotal;// 加上上次下载的总大小。
                    fis.close();
                }
                conn.setRequestProperty("Range", "bytes=" + startIndex + "-");
                int responseCode = conn.getResponseCode();
                long length = conn.getContentLength();
                // 创建一个大小和服务器文件一样大小的文件
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    file = new File(Environment.getExternalStorageDirectory(), getFileName(params[0]));
                } else {
                    file = new File(Environment.getRootDirectory(), getFileName(params[0]));
                }
                RandomAccessFile rcf = new RandomAccessFile(file, "rw");
                rcf.setLength(length);
                long endIndex = length - 1;
                // 总大小
                int threadSize = (int) (endIndex - startIndex);
                InputStream is = conn.getInputStream();
                // 指定文件开始写的位置
                rcf.seek(startIndex);
                int len = 0;
                byte[] buffer = new byte[1024];
                int progress = 0;// 当前下载进度
                while ((len = is.read(buffer)) != -1) {
                    RandomAccessFile rf = new RandomAccessFile(positionFile, "rwd");
                    rcf.write(buffer, 0, len);
                    total += len;
                    rf.write(String.valueOf(total).getBytes());
                    rf.close();
                    progress = (int) ((total / (float) length) * 100);
                    this.onProgressUpdate(progress);
                }
                is.close();
                rcf.close();
                System.out.println("下载完毕了");
                return (long) progress;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 只有所有的线程都下载完毕后 才可以删除记录文件。
                File f = null;
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    file = new File(Environment.getExternalStorageDirectory(), getFileName(params[0]));
                } else {
                    file = new File(Environment.getRootDirectory(), getFileName(params[0]));
                }
                f.delete();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            builder.setContentText("下载进度：" + values[0] + "%");
            builder.setProgress(100, values[0], false);
            Notification no = builder.build();
            no.flags = Notification.FLAG_NO_CLEAR;
            manager.notify(1, no);
            no = null;
        }

        @Override
        protected void onPostExecute(Long result) {
            if (result == 100) {
                builder.setProgress(0, 0, true);
                builder.setAutoCancel(true)// 点击后让通知将消失
                        .setContentTitle("下载完成").setContentText("点击安装").setTicker("下载完成！");
                Intent apkIntent = new Intent();
                apkIntent.setAction(android.content.Intent.ACTION_VIEW);
                Uri uri = Uri.fromFile(file);
                apkIntent.setDataAndType(uri, "application/vnd.android.package-archive");
                PendingIntent contextIntent = PendingIntent.getActivity(DownLoadService.this, 0, apkIntent, 0);
                builder.setContentIntent(contextIntent);
                Notification no = builder.build();
                no.defaults = Notification.DEFAULT_SOUND;
                manager.notify(1, no);
                downloadTask = null;
            } else {
                builder.setProgress(0, 0, true);
                builder.setContentText("下载失败...");
                manager.notify(1, builder.build());
                Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getFileName(String path) {
        int start = path.lastIndexOf("/") + 1;
        return path.substring(start);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (downloadTask != null && downloadTask.getStatus() == AsyncTask.Status.RUNNING) {
            downloadTask.cancel(true);
        }
    }
}
