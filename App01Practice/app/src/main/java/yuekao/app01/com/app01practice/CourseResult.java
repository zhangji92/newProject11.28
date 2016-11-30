package yuekao.app01.com.app01practice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * 课程结果
 */
public class CourseResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_result);
        ((CourseApplication)getApplication()).activities.add(this);
        findViewById(R.id.arrow1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //获取Intent
        Intent intent=getIntent();
        //接受消息
        String str=intent.getStringExtra("course");
        //获取组件
        TextView textView= (TextView) findViewById(R.id.result);
        //把接受过来的消息赋值给控件
        textView.setText(str);
    }
}
