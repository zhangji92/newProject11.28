package com.allactivity.sqlite;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by 张继 on 2016/10/27.
 * SQLite数据库
 */
public class SQLiteDome extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //每个程序的数据可都是唯一的(每个程序都有自己的数据库)默认的情况下各自互相不干扰
        //创建一个数据库并且打开
        SQLiteDatabase db = openOrCreateDatabase("user.db", MODE_PRIVATE, null);
        //创建表
        db.execSQL("CREATE table if not exists usertb(_id Integer primary key autoincrement,name text not null,age integer not null,sex text not null)");
        db.execSQL("insert into usertb(name,sex,age) values ('张三','女',18)");
        db.execSQL("insert into usertb(name,sex,age) values ('李四','男',20)");
//        db.execSQL("insert into usertb(name,sex,age) values ('张三','女',18)");
        Cursor c = db.rawQuery("select * from usertb", null);
        if (c != null) {
            while (c.moveToNext()) {
                Log.i("info", "_id:" + c.getInt(c.getColumnIndex("_id")));
                Log.i("info", "name:" + c.getString(c.getColumnIndex("name")));
                Log.i("info", "age:" + c.getInt(c.getColumnIndex("age")));
                Log.i("info", "sex:" + c.getString(c.getColumnIndex("sex")));
                Log.i("info", "---------------------------------");
            }
            c.close();
        }
        db.close();

    }

}
