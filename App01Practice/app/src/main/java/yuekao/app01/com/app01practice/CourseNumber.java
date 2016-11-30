package yuekao.app01.com.app01practice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * 课程信息
 */
public class CourseNumber extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_number);
        ((CourseApplication)getApplication()).activities.add(this);
        //返回上一页
        findViewById(R.id.arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //获取一个application数据源
        CourseApplication courseApplication = (CourseApplication) getApplication();
        //获取课程列表
        CourseAdapter courseAdapter = new CourseAdapter(CourseNumber.this, courseApplication.lists);
        ListView listView = (ListView) findViewById(R.id.kcxx);
        listView.setAdapter(courseAdapter);

    }

    public void prompt(View view) {
        Intent intent = new Intent(CourseNumber.this, CourseResult.class);
        startActivity(intent);
    }

}
