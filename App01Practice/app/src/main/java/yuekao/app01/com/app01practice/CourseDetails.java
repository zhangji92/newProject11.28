package yuekao.app01.com.app01practice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;

import yuekao.app01.com.app01practice.mode.CourseList;
import yuekao.app01.com.app01practice.mode.Student;

/**
 * 课程详情信息
 */
public class CourseDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        ((CourseApplication)getApplication()).activities.add(this);

        //获取控件
        ListView listView = (ListView) findViewById(R.id.detail);
        //返回上一个页面
        findViewById(R.id.arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //获取数据源
        CourseApplication courseApplication = (CourseApplication) getApplication();
        //获取课程信息
        CourseAdapter courseAdapter = new CourseAdapter(CourseDetails.this, courseApplication.lists);
        //把课程信息添加到ListView控件中
        listView.setAdapter(courseAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //获取数据源
                CourseApplication courseApplication = (CourseApplication) getApplication();
                //选择的课程
                CourseList courseList = courseApplication.lists.get(i);
                //查询该课程的信息
                String temp = courseList.selectCourse();
                //跳转页面
                Intent intent = new Intent(CourseDetails.this, CourseResult.class);
                //把查询的内容发送
                intent.putExtra("course", temp);
                //启动
                startActivity(intent);
            }
        });

    }

}
