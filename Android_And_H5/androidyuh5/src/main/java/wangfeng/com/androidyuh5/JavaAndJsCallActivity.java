package wangfeng.com.androidyuh5;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class JavaAndJsCallActivity extends Activity implements View.OnClickListener{
    private EditText etNumber;
    private EditText etPassword;
    private Button btnLogin;
    private  WebView web;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
        initWebView();
    }
    private void findViews() {
        setContentView(R.layout.activity_java_and_js);
        etNumber = (EditText)findViewById( R.id.et_number );//账号
        etPassword = (EditText)findViewById( R.id.et_password );//密码
        btnLogin = (Button)findViewById( R.id.btn_login );//登录
        btnLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if ( v == btnLogin ) {//点击登录
          login();
        }
    }

    private void login() {
        //得到账号和密码
        String name=etNumber.getText().toString().trim();
        String pass=etPassword.getText().toString().trim();
        //判断账号密码不为空
        if(TextUtils.isEmpty(name)||TextUtils.isEmpty(pass)){//用户名密码为空
            Toast.makeText(this, "用户名或密码为空", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "登录", Toast.LENGTH_SHORT).show();
//            initWebView();
//            initWebView(name);
           loginJs(name);
        }
    }

    private void loginJs(String name) {
        //WebView加载js方法，该方法在加载的Html中（java调JavaScript）
        web.loadUrl("javascript:javaCallJs("+"'"+name+"'"+")");
        //加载文js后重新加载WebView
        setContentView(web);
    }

    /**
     * 初始化WebView
     *
     */
    private void initWebView() {
        web=new WebView(this);
        WebSettings webset=web.getSettings();
        webset.setJavaScriptEnabled(true);
        //不调用浏览器 自定义浏览器，
        web.setWebViewClient(new WebViewClient());
        //可以加载网页，本地页面
//        web.loadUrl("http://www.atguigu.com/teacher.shtm1");
        /**添加一个js调用java的接口,
         * object obj 该类中实现js调用java的代码
         * string name name 为js代码中的一个标识
         */
        //以后js通过Android这个字段 调用AndroidAndJsInterface类中的任何方法
        web.addJavascriptInterface(new AndroidAndJsInterface(),"Android");
        web.loadUrl("file:///android_asset/JavaAndJavaScriptCall.html");
//        setContentView(web);//加载WebView
    }

    class AndroidAndJsInterface{
        @JavascriptInterface
        public void showToast(){
            Toast.makeText(JavaAndJsCallActivity.this, "js调用java代码", Toast.LENGTH_SHORT).show();
        }
    }
}
