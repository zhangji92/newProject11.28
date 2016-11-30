package com.allactivity.asynctask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.allactivity.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by 张继 on 2016/11/25.
 *  onPreExecute--加载进度条
 *  doInBackground--下载网络图片（耗时操作）
 *  onPostExecute--显示图片
 */

public class ImageTest extends Activity {
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    String url="http://brp1.yokacdn.com/upload/20161124/2/14799199529692.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_test);
        initViews();
        //设置传递进去的参数
        new MyAsyncTsk().execute(url);
    }

    private void initViews() {
        mImageView= (ImageView) findViewById(R.id.test_img);
        mProgressBar= (ProgressBar) findViewById(R.id.test_progressBar);
    }
    class MyAsyncTsk extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);

        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mProgressBar.setVisibility(View.GONE);
            mImageView.setImageBitmap(bitmap);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            //doInBackground可变长的数组，也就是可以传递很多参数，
            //由于现在指传一个，所有params[0]获取传递进来的url
            String url=params[0];
            Bitmap bitmap=null;
            URLConnection connection;
            InputStream is=null;
            BufferedInputStream bis=null;
            try {
                connection=new URL(url).openConnection();//获取网络链接对象
                is=connection.getInputStream();
                bis=new BufferedInputStream(is);
                Thread.sleep(3000);
                //通过BitmapFactory.decodeStream(bis)解析输入流，转化Bitmap格式
                bitmap= BitmapFactory.decodeStream(bis);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (is!=null&&bis!=null){
                    try {
                        //关闭了
                        is.close();
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            //把bitmap作为返回值返回
            return bitmap;
        }
    }
}
