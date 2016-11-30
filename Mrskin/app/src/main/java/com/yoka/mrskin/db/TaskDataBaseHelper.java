package com.yoka.mrskin.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDataBaseHelper extends SQLiteOpenHelper
{
	// 数据库名称
		private static final String DB_NAME = "plan.db";
		// 数据表名称
		private static final String TABLE_NAME = "task";
		// 数据库版本
		private static final int DB_VERSION = 1;
		
		// 创建数据表SQL语句
		private static final String CREATE_TABLE = 
				"CREATE TABLE " + TABLE_NAME
				+ "(" 
				+ "_id		INTEGER			PRIMARY KEY ,"
				+ "uid		INTEGER ,"
				+ "tid 	INTEGER ," 
				+ "index_time INTEGER" 
				+ ")";
		// 删除数据表SQL语句
		private static final String DROP_TABLE = "DROP IF TABLE EXISTS "
				+ TABLE_NAME;

	public TaskDataBaseHelper(Context context)
	{
		super(context, DB_NAME, null, DB_VERSION);
		System.out.println("--------------------数据库创建成功!!");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建数据表
				db.execSQL(CREATE_TABLE);
				System.out.println("------------------------数据表创建成功!!");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_TABLE);
		System.out.println("-------------------------数据表更新成功!!");
		onCreate(db);
	}
}
