package com.jrd.loan.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.RemoteViews;

import com.jrd.loan.R;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.util.AppInfoUtil;
import com.jrd.loan.util.FileUtil;
import com.jrd.loan.util.ToastUtil;

/**
 * 状态栏下载Service
 *
 * @author Jacky
 */
public final class StateBarDownloadService extends IntentService {
    public final static String DOWNLOAD_URL = "download_url";
    private final static int UPDATE_DOWNLOAD_PROGRESS = 20000;
    private final static int UPDATE_DOWNLOAD_START = 20001;
    private final static int UPDATE_DOWNLOAD_END = 20002;
    private final static int UPDATE_DOWNLOAD_FAIL = 20003;
    private final static int STATE_BAR_DOWNLOAD_ID = 1000;
    private DownloadNotifination notifination;
    private String fileName;

    private static boolean isDownloading;

    public StateBarDownloadService() {
        super("StateBarDownloadService");
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_DOWNLOAD_START: // 下载开始
                    notifination = new DownloadNotifination();
                    notifination.notifyStateBarDownload(msg.arg1);
                    break;
                case UPDATE_DOWNLOAD_PROGRESS: // 下载过程中
                    notifination.updateDownloadProgress(msg.arg1);
                    break;
                case UPDATE_DOWNLOAD_END: // 下载结束
                    this.installApk();
                    break;
                case UPDATE_DOWNLOAD_FAIL: // 下载失败
                    this.handDownloadFail();
                    break;
            }
        }

        // 成功下载结束后自动启动安装程序
        private void installApk() {
            BaseActivity.setIsPresseHomeKey(true);

            AppInfoUtil.installApk(new File(FileUtil.UPGRADE_APK_DOWNLOAD_PATH, fileName).getAbsolutePath());
            notifination.cancelNotification();
        }

        // 处理下载失败的情况
        private void handDownloadFail() {
            notifination.cancelNotification();
            ToastUtil.showShort(StateBarDownloadService.this, R.string.loan_state_bar_download_fail);
        }
    };

    @Override
    protected void onHandleIntent(Intent intent) {
        // 如果前一次下载没有结束, 取消本次请求的下载
        if (isDownloading) {
            return;
        }

        // 标记正在下载
        isDownloading = true;

        String downloadUrl = intent.getStringExtra(DOWNLOAD_URL);
        this.fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);

        Downloader downloader = new Downloader();
        downloader.download(downloadUrl, new DownloadStateListener() {
            @Override
            public void onStartDownload(int max) {// 开始下载
                Message msg = Message.obtain();
                msg.what = UPDATE_DOWNLOAD_START;
                msg.arg1 = max;
                handler.sendMessage(msg);
            }

            @Override
            public void onProgress(int size) {// 下载过程中
                Message msg = Message.obtain();
                msg.what = UPDATE_DOWNLOAD_PROGRESS;
                msg.arg1 = size;
                handler.sendMessage(msg);
            }

            @Override
            public void onEndDownload() {// 成功下载结束
                isDownloading = false;
                Message msg = Message.obtain();
                msg.what = UPDATE_DOWNLOAD_END;
                handler.sendMessage(msg);
            }

            @Override
            public void onDownloadFail() {// 下载失败
                isDownloading = false;
                Message msg = Message.obtain();
                msg.what = UPDATE_DOWNLOAD_FAIL;
                handler.sendMessage(msg);
            }
        });
    }

    private final class DownloadNotifination {
        private Notification notification;
        private NotificationManager manager;
        private RemoteViews contentView;
        private int max;
        private int progress;

        public final void notifyStateBarDownload(int max) {
            this.max = max;

            this.manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            this.notification = new Notification(R.drawable.loan_app_icon, getString(R.string.loan_state_bar_download_picker), System.currentTimeMillis());
            this.notification.flags |= Notification.FLAG_NO_CLEAR;

            this.contentView = new RemoteViews(getPackageName(), R.layout.loan_state_bar_download_layout);
            this.contentView.setProgressBar(R.id.progress, this.max, this.progress, false);
            this.contentView.setImageViewResource(R.id.appIcon, R.drawable.loan_app_icon);
            this.contentView.setTextViewText(R.id.appSize, getString(R.string.loan_state_bar_download_size, "0", this.getByteSize(max)));
            this.contentView.setTextViewText(R.id.proText, getString(R.string.loan_state_bar_download_percent, 0, "%"));
            this.notification.contentView = this.contentView;

            Intent intent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(StateBarDownloadService.this, 0, intent, 0);
            this.notification.contentIntent = pendingIntent;
            this.manager.notify(STATE_BAR_DOWNLOAD_ID, notification);
        }

        public final void updateDownloadProgress(int currentSize) {
            this.progress += currentSize;
            this.contentView.setProgressBar(R.id.progress, this.max, this.progress, false);

            // 更新已下载文件的大小
            this.contentView.setTextViewText(R.id.appSize, getString(R.string.loan_state_bar_download_size, this.getByteSize(this.progress), this.getByteSize(this.max)));

            // 更新下载进度(百分比)
            this.contentView.setTextViewText(R.id.proText, getString(R.string.loan_state_bar_download_percent, ((int) this.progress * 100 / this.max), "%"));
            this.manager.notify(STATE_BAR_DOWNLOAD_ID, this.notification);
        }

        public final void cancelNotification() {
            this.manager.cancel(STATE_BAR_DOWNLOAD_ID);
        }

        // 将字节转换成KB(MB或者GB)
        public final String getByteSize(int num) {
            DecimalFormat format = new DecimalFormat("0.00");
            double sizeByte = 0;

            if (num >= 1024) {
                sizeByte = num / 1024.0;

                if (sizeByte >= 1024) {
                    sizeByte = sizeByte / 1024.0;

                    if (sizeByte >= 1024) {
                        sizeByte = sizeByte / 1024.0;
                        return new StringBuffer(format.format(sizeByte)).append("G").toString();
                    } else {
                        return new StringBuffer(format.format(sizeByte)).append("M").toString();
                    }
                } else {
                    return new StringBuffer(format.format(sizeByte)).append("K").toString();
                }
            } else {
                return new StringBuffer().append(num).append("B").toString();
            }
        }
    }

    private interface DownloadStateListener {
        public void onStartDownload(int max);

        public void onProgress(int size);

        public void onEndDownload();

        public void onDownloadFail();
    }

    private class Downloader {
        // 连接超时时间
        private final static int TIMEOUT_TIME = 30000;

        public void download(String url, DownloadStateListener downloadStateListener) {
            HttpURLConnection conn = null;
            BufferedInputStream bufferedInputStream = null;
            BufferedOutputStream bufferedOutputStream = null;
            InputStream inputStream = null;
            FileOutputStream outputStream = null;

            try {
                URL apkUrl = new URL(url);

                conn = (HttpURLConnection) apkUrl.openConnection();
                conn.setDoInput(true);
                conn.setReadTimeout(TIMEOUT_TIME);
                conn.setConnectTimeout(TIMEOUT_TIME);

                int resCode = conn.getResponseCode();

                if (resCode == 200) {
                    int fileSize = conn.getContentLength();

                    // 下载开始
                    downloadStateListener.onStartDownload(fileSize);

                    byte[] buffer = new byte[4096];
                    int len = 0;

                    inputStream = conn.getInputStream();
                    bufferedInputStream = new BufferedInputStream(inputStream);

                    outputStream = new FileOutputStream(new File(FileUtil.UPGRADE_APK_DOWNLOAD_PATH, fileName));
                    bufferedOutputStream = new BufferedOutputStream(outputStream);

                    int currTotalSize = 0;

                    while ((len = bufferedInputStream.read(buffer)) != -1) {
                        bufferedOutputStream.write(buffer, 0, len);

                        currTotalSize += len;

                        // 如果当前新下载达到1%, 更新进度条
                        if (100.0 * currTotalSize / fileSize >= 1) {
                            downloadStateListener.onProgress(currTotalSize);
                            currTotalSize = 0;
                        }
                    }

                    if (currTotalSize != 0) {
                        downloadStateListener.onProgress(currTotalSize);
                        currTotalSize = 0;
                    }

                    bufferedOutputStream.flush();

                    // 成功下载结束
                    downloadStateListener.onEndDownload();
                } else {// 下载失败
                    downloadStateListener.onDownloadFail();
                }
            } catch (MalformedURLException e) {
                // 下载失败
                downloadStateListener.onDownloadFail();
                e.printStackTrace();
            } catch (IOException e) {
                // 下载失败
                downloadStateListener.onDownloadFail();
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                    conn = null;
                }

                try {
                    if (bufferedInputStream != null) {
                        bufferedInputStream.close();
                        bufferedInputStream = null;
                    }

                    if (bufferedOutputStream != null) {
                        bufferedOutputStream.close();
                        bufferedOutputStream = null;
                    }

                    if (inputStream != null) {
                        inputStream.close();
                        inputStream = null;
                    }

                    if (outputStream != null) {
                        outputStream.close();
                        outputStream = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}