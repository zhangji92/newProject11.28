package yuekao.app01.com.app01practice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import yuekao.app01.com.app01practice.mode.Student;

public class ElectiveCourse extends AppCompatActivity implements OnClickListener {
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elective_course);
        ((CourseApplication)getApplication()).activities.add(this);
        ImageView img1 = (ImageView) findViewById(R.id.arrow);
        img1.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        //关闭该页面
        finish();
    }

    //单选按钮
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        RadioButton radioButton1 = (RadioButton) findViewById(R.id.boy);
        RadioButton radioButton2 = (RadioButton) findViewById(R.id.girl);
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.boy:
                if (checked) {
                    radioButton2.setChecked(false);

                }
                break;
            case R.id.girl:
                if (checked) {
                    radioButton1.setChecked(false);
                }
                break;
        }
    }

    /**
     * 跳转页面的点击事件
     *
     * @param view 视图
     */
    public void prompt(View view) {


        Intent intent = new Intent(ElectiveCourse.this, MessageNumber.class);
        intent.putExtra("student", getStudent());
        //判断姓名是否为空
        if (student.getStudentName() != null && !student.getStudentName().equals("")) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 获取学生信息
     *
     * @return 返回学生对象
     */
    private Student getStudent() {
        //获取输入组件
        EditText editText_stuName = (EditText) findViewById(R.id.stuName);
        //获取输入的学生姓名
        String stuName = editText_stuName.getText().toString();
        //获取控件
        RadioButton boy_button = (RadioButton) findViewById(R.id.boy);
        RadioButton girl_button = (RadioButton) findViewById(R.id.girl);
        int stuSex = 0;
        //判断男女
        if (boy_button.isChecked()) {
            stuSex = 0;
        } else if (girl_button.isChecked()) {
            stuSex = 1;
        }
        //获取年龄
        EditText editText_age = (EditText) findViewById(R.id.stuAge);
        String strAge = editText_age.getText().toString();
        int stuAge = 0;
        try {
            stuAge = Integer.parseInt(strAge);
        } catch (Exception e) {
            stuAge = 0;
        }
        student = new Student(stuName, stuSex, stuAge);
        return student;
    }

}
