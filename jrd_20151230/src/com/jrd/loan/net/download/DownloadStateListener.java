package com.jrd.loan.net.download;

public interface DownloadStateListener {
    public void onStartDownload(int max);

    public void onProgress(int size);

    public void onEndDownload();

    public void onDownloadFail();
}
