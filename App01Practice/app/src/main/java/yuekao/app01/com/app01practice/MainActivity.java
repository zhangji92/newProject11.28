package yuekao.app01.com.app01practice;

import android.app.usage.UsageEvents;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //按退出键的间隔时间
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((CourseApplication) getApplication()).activities.add(this);
        TextView t1 = (TextView) findViewById(R.id.t1);
        TextView t2 = (TextView) findViewById(R.id.t2);
        TextView t3 = (TextView) findViewById(R.id.t3);
        TextView t4 = (TextView) findViewById(R.id.t4);
        TextView t5 = (TextView) findViewById(R.id.t5);
        TextView t6 = (TextView) findViewById(R.id.t6);
        t1.setOnClickListener(this);
        t2.setOnClickListener(this);
        t3.setOnClickListener(this);
        t4.setOnClickListener(this);
        t5.setOnClickListener(this);
        t6.setOnClickListener(this);
    }

    /**
     * 退出监听
     *
     * @param keyCode 返回键
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次后退键退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                //退出代码
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.t1:
                Intent intent1 = new Intent(MainActivity.this, ElectiveCourse.class);
                startActivity(intent1);
                break;
            case R.id.t2:
                Intent intent2 = new Intent(MainActivity.this, CourseNumber.class);
                startActivity(intent2);
                break;
            case R.id.t3:
                Intent intent3 = new Intent(MainActivity.this, StudentMessage.class);
                startActivity(intent3);
                break;
            case R.id.t4:
                Intent intent4 = new Intent(MainActivity.this, CourseDetails.class);
                startActivity(intent4);
                break;
            case R.id.t5:
                Intent intent5 = new Intent(MainActivity.this, Help.class);
                startActivity(intent5);
                break;
            case R.id.t6:

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("确定要退出系统吗")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                CourseApplication courseApplication = (CourseApplication) getApplication();
                                List<AppCompatActivity> list = courseApplication.activities;
                                for (int i = 0; i < list.size(); i++) {
                                    finish();

                                }

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create()
                        .show();
                break;
        }
    }


}
