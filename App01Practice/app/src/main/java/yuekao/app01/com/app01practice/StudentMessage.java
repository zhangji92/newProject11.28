package yuekao.app01.com.app01practice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 学生信息
 */
public class StudentMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_message);
        ((CourseApplication)getApplication()).activities.add(this);
        findViewById(R.id.arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void submit(View view) {
        //获取组件
        EditText editText = (EditText) findViewById(R.id.name);
        //获取输入的姓名
        String name = editText.getText().toString();
        //创建intent
        Intent intent = new Intent(StudentMessage.this, QueryResult.class);
        //发送姓名
        intent.putExtra("name", name);
        if (name != null && !name.equals("")) {
            //启动
            startActivity(intent);
        }else{
            Toast.makeText(this,"姓名不能为空",Toast.LENGTH_SHORT).show();
        }
    }
}
