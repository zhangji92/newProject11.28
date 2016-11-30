package yuekao.app01.com.app01practice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import yuekao.app01.com.app01practice.mode.CourseList;
import yuekao.app01.com.app01practice.mode.Student;
import yuekao.app01.com.app01practice.mode.Teacher;

/**
 * 选修课程编号类
 */
public class MessageNumber extends AppCompatActivity implements AdapterView.OnItemClickListener {
    //声明学生对象
    Student student;

    private CourseAdapter courseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_number);
        ((CourseApplication)getApplication()).activities.add(this);
        findViewById(R.id.arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //获取ListView控件
        ListView listView = (ListView) findViewById(R.id.listView);
        //获取一个application数据源
        CourseApplication courseApplication = (CourseApplication) getApplication();

        //获取从ElectiveCourse页面发送的信息
        Intent intent = getIntent();
        //获取学生信息
        student = (Student) intent.getSerializableExtra("student");

        //创建课程列表对象
        courseAdapter = new CourseAdapter(MessageNumber.this, courseApplication.lists);
        //把课程列表添加到listView中
        listView.setAdapter(courseAdapter);
        //获取ListView中单个点击事件
        listView.setOnItemClickListener(this);

    }

    /**
     * 选课点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //获取一个application数据源
        CourseApplication courseApplication = (CourseApplication) getApplication();
        //选择选修课程
        CourseList courseList = courseApplication.lists.get(i);
        //添加选修课程
        boolean b = courseList.addStudent(student);
        if (b) {
            Toast.makeText(this, "选课成功！", Toast.LENGTH_SHORT).show();
            //通知适配器重新加载数据
            courseAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "选课失败！", Toast.LENGTH_SHORT).show();
        }
    }
}
