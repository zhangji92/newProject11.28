package wangfeng.com.androidyuh5;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button btnJavaAndJs;
    private Button btnJsCallJava;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
    }

    /**
     * 初始化视图
     */
    private void findViews() {
        setContentView(R.layout.activity_main);
        btnJavaAndJs = (Button) findViewById(R.id.btn_java_and_js);//Java代码和H5代码互调
        btnJsCallJava = (Button) findViewById(R.id.btn_js_call_java);

        btnJavaAndJs.setOnClickListener(this);
        btnJsCallJava.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == btnJavaAndJs) {//Java代码和H5代码互调
            Intent intent = new Intent(this, JavaAndJsCallActivity.class);
            startActivity(intent);
            Toast.makeText(this, "互动", Toast.LENGTH_SHORT).show();
        } else if (v == btnJsCallJava) {//H5调用Android播放视频
            Intent intent = new Intent(this, JsCallJavaVideoActivity.class);
            startActivity(intent);
            Toast.makeText(this, "播放视频", Toast.LENGTH_SHORT).show();
        }
    }

}
