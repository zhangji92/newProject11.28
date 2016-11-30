package wangfeng.com.androidyuh5;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class JsCallJavaVideoActivity extends AppCompatActivity {

    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js_call_java_video);
        webview = (WebView) findViewById(R.id.webview);
        WebSettings webset = webview.getSettings();
        //设置支持javaScript脚步语言
        webset.setJavaScriptEnabled(true);
        //不调用浏览器 自定义浏览器，
        webview.setWebViewClient(new WebViewClient());
        //可以加载网页，本地页面
//        web.loadUrl("http://www.atguigu.com/teacher.shtm1");

        /**添加一个js调用java的接口,
         * object obj 该类中实现js调用java的代码
         * string name name 为js代码中的一个标识
         */
        //设置支持javaScript脚步语言
        //以后js通过Android这个字段 调用AndroidAndJsInterface类中的任何方法
        webview.addJavascriptInterface(new AndroidAndJSInterface(), "android");
        //加载视屏页的Html
        webview.loadUrl("file:///android_asset/RealNetJSCallJavaActivity.htm");
//        setContentView(webview);//加载WebView
        Log.e("onCreat: ", "----------");
    }

    class AndroidAndJSInterface {

        /**
         * 该方法将被Js调用
         *
         * @param id
         * @param videourl
         * @param title
         */
        @JavascriptInterface
        public void playVideo(int id, String videourl, String title) {
            Log.e("playVideo: ", "----------");
            Toast.makeText(JsCallJavaVideoActivity.this, "videourl" + videourl, Toast.LENGTH_SHORT).show();
            if (!videourl.equals("")) {
                //调起系统所有播放器
                Intent intent = new Intent();
                intent.setDataAndType(Uri.parse(videourl), "video/*");
                startActivity(intent);
                Toast.makeText(JsCallJavaVideoActivity.this, "videourl" + videourl, Toast.LENGTH_SHORT).show();

            }
        }
    }
}
