package yuekao.app01.com.app01practice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import yuekao.app01.com.app01practice.mode.CourseList;

/**
 * 查询学生信息结果
 */
public class QueryResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_result);
        ((CourseApplication)getApplication()).activities.add(this);
        findViewById(R.id.arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //获取intent
        Intent intent = getIntent();
        //接受消息
        String name = intent.getStringExtra("name");
        //数据源
        CourseApplication courseApplication = (CourseApplication) getApplication();
        //把数据存进list中
        List<CourseList> list = courseApplication.lists;

        String str = "";
        //遍历数据
        for (int i = 0; i < list.size(); i++) {
            //判断该课程是否有学生
            if (list.get(i).getStudents().size() >= 1) {
                //遍历该课程的学生
                for (int j = 0; j < list.get(i).getStudents().size(); j++) {
                    if (name.equals(list.get(i).getStudents().get(j).getStudentName())) {
                        str +="已选择"+ list.get(i).getClassName()+"\n";
                    }else{
                        str +="未选择"+ list.get(i).getClassName()+"\n";
                    }
                }
            }else{
                str +="未选择"+ list.get(i).getClassName()+"\n";
            }
        }

        TextView textView= (TextView) findViewById(R.id.query);
        textView.setText(str);

    }
}
