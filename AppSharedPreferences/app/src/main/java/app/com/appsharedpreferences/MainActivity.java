package app.com.appsharedpreferences;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * SharedPreferences中的Editor每一次操作都得commit提交
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences pre;
    SharedPreferences.Editor editor;
    EditText editText_user;
    EditText editText_pad;
    CheckBox checkBox;
    String preName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //默认的SharedPreferences对象
//        SharedPreferences pre= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        //自定义的SharedPreferences
        /**
         * getSharedPreferences(String name, int mode)
         * 1.文件名称
         * 2.权限
         */
        pre = getSharedPreferences("myPre", MODE_PRIVATE);
        //编辑对象
        editor = pre.edit();

//        editor.putString("name", "耗子");
//        editor.putInt("age", 5);
//        editor.putLong("time", System.currentTimeMillis());
//        editor.putBoolean("default", true);
//        editor.commit();
//        //移除，
//        editor.remove("default");
//        editor.commit();
//        String name = pre.getString("name", "猴子");
//        Toast.makeText(MainActivity.this, name, Toast.LENGTH_LONG).show();

        init();
    }

    /**
     * 登陆初始化
     */
    private void init() {

        Button bnt_login = (Button) findViewById(R.id.bnt_login);
        bnt_login.setOnClickListener(this);

        editText_user = (EditText) findViewById(R.id.user_edit);
        editText_pad = (EditText) findViewById(R.id.pad_edit);
        checkBox = (CheckBox) findViewById(R.id.check_box);
        checkBox.setOnClickListener(this);
        preName = pre.getString("user", "");
        if (!preName.equals("")) {
            editText_user.setText(preName);
            checkBox.setChecked(true);
        }

    }

    /**
     * 登陆
     */
    private void login() {
        String name = editText_user.getText().toString();
        String pad = editText_pad.getText().toString();


        if (name.equals("admin") && pad.equals("123456")) {
            if (checkBox.isChecked()) {
                editor.putString("user", name);
                editor.putString("pad", pad);
                editor.commit();
            }
            Toast.makeText(this, "登陆成功", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "登陆失败", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bnt_login:
                login();
                break;
            case R.id.check_box:
                if (!checkBox.isChecked()) {
                    editor.remove("user");
                    editor.remove("pad");
                    editor.commit();
                }
                break;
        }
    }
}
