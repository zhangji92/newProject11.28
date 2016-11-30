package com.jrd.loan.net.download;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Handler;
import android.os.Message;

import com.jrd.loan.util.FileUtil;

/**
 * file download
 *
 * @author Jacky
 */
public final class FileDownload {
    private final static int UPDATE_DOWNLOAD_PROGRESS = 20000;
    private final static int UPDATE_DOWNLOAD_START = 20001;
    private final static int UPDATE_DOWNLOAD_END = 20002;
    private final static int UPDATE_DOWNLOAD_FAIL = 20003;

    private String fileName;
    private String downloadUrl;

    private DownloadStateListener downloadStateListener;

    public FileDownload(String downloadUrl, DownloadStateListener downloadStateListener) {
        this.downloadUrl = downloadUrl;
        this.downloadStateListener = downloadStateListener;
        this.fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_DOWNLOAD_START: // 下载开始
                    downloadStateListener.onStartDownload(msg.arg1);
                    break;
                case UPDATE_DOWNLOAD_PROGRESS: // 下载过程中
                    downloadStateListener.onProgress(msg.arg1);
                    break;
                case UPDATE_DOWNLOAD_END: // 下载结束
                    downloadStateListener.onEndDownload();
                    break;
                case UPDATE_DOWNLOAD_FAIL: // 下载失败
                    downloadStateListener.onDownloadFail();
                    break;
            }
        }
    };

    public void startDownloadFile() {
        new Thread() {
            @Override
            public void run() {
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
                        Message msg = Message.obtain();
                        msg.what = UPDATE_DOWNLOAD_END;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onDownloadFail() {// 下载失败
                        Message msg = Message.obtain();
                        msg.what = UPDATE_DOWNLOAD_FAIL;
                        handler.sendMessage(msg);
                    }
                });
            }
        }.start();
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
