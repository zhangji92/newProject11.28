package com.allactivity.util;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 张继 on 2016/11/9.
 * 下载线程
 */

public class HttpThread extends Thread {
    private String mUrl;
    //下载目录
    File downFile = null;
    File sdFile = null;
    InputStream in = null;
    //文件输出
    FileOutputStream out = null;

    public HttpThread(String mUrl) {
        this.mUrl = mUrl;
    }

    @Override
    public void run() {
        Log.e("aaa", "start downLoad");

        URL httpUrl = null;
        try {
            httpUrl = new URL(mUrl);
            HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
//            connection.setRequestProperty("Content-Type", "application/vnd.android");
            connection.setRequestProperty("User-Agent","MSIE");
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            //输入输出流
            connection.setDoInput(true);
//            connection.setDoOutput(true);

            int code = connection.getResponseCode();
            if (code == 200) {

                //输入流
                in = connection.getInputStream();
                //判断sd卡是否存在
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    //外置的存储目录
                    downFile = Environment.getExternalStorageDirectory();
                    sdFile = new File(downFile, "text.apk");
                    //把下载下来的文件写到sd卡文件里面
                    out = new FileOutputStream(sdFile);
                }
                byte[] b = new byte[6 * 1024];
                int len;
                while ((len = in.read(b)) != -1) {
                    out.write(b, 0, len);
                }
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                Log.e("aaa", "downLoad success");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
