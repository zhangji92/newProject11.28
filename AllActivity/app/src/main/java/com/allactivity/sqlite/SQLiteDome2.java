package com.allactivity.sqlite;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by 张继 on 2016/11/3.
 * 数据库
 */

public class SQLiteDome2 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SQLiteDatabase db = openOrCreateDatabase("stu.db", MODE_PRIVATE, null);
        db.execSQL("CREATE table if not exists stub(_id Integer primary key autoincrement,name text not null,age integer not null,sex text not null)");
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", "张三");
        contentValues.put("age", 19);
        contentValues.put("sex", "男");
        /**
         * 第一个参数是表名
         * 第二个是默认值
         * 第三个是数据
         */
        Long rowId = db.insert("stub", null, contentValues);
        //清空数据
        contentValues.clear();
        contentValues.put("name", "张三");
        contentValues.put("age", 19);
        contentValues.put("sex", "男");
        db.insert("stub", null, contentValues);

        contentValues.put("name", "张三风");
        contentValues.put("age", 30);
        contentValues.put("sex", "男");
        db.insert("stub", null, contentValues);

        contentValues.put("name", "张三封");
        contentValues.put("age", 55);
        contentValues.put("sex", "男");
        db.insert("stub", null, contentValues);
        contentValues.clear();
        contentValues.put("sex", "女");
        //将全部ID大于3的人的性别改成女
        db.update("stub", contentValues, "_id>?", new String[]{"2"});
        //将所有名字带为风的人数  据删除
        db.delete("stub", "name like ?", new String[]{"%风%"});
        Cursor c = db.query("stub", null, "_id>?", new String[]{"0"}, null, null, "name");
        if (c != null) {
            //查询出所以字段
            String[] columns = c.getColumnNames();
            while (c.moveToNext()) {
                for (String columnName : columns) {
                    Log.i("data_dome", "columnName:" + c.getString(c.getColumnIndex(columnName)));
                }
            }
            c.close();
        }
        db.close();
    }
}
