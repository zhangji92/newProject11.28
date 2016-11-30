package com.yoka.mrskin.db;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.data.PlanTask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.Log;
/**
 * 
 * @author zlz
 * @Data 2016年8月9日
 */
public class PlanTaskDao {
	private static final String TAG = PlanTaskDao.class.getSimpleName().toString();
	private TaskDataBaseHelper mHelper;
	private SQLiteDatabase db; // 数据库的对象
	private Context context;

	private static final String TABLE_NAME = "task";//表名
	private static final String TASK_ID = "tid";//任务id
	private static final String USER_ID = "uid";//用户id
	private static final String INDEX_TIME = "index_time";//

	public PlanTaskDao(Context context) {
		super();
		mHelper = new TaskDataBaseHelper(context);
		this.context = context;
	}
	/**
	 * 存入本地数据
	 * @param _task
	 */
	public void insertTask(PlanTask _task){

		db = mHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(USER_ID, _task.getUid());
		values.put(TASK_ID, _task.getTaskId());
		values.put(INDEX_TIME, _task.getIndexTime());

		db.insert(TABLE_NAME, null, values);
		Log.d(TAG, "------插入成功"+_task.getUid()+"-"+_task.getTaskId()+"-"+_task.getIndexTime());
		db.close();

	}
	/**
	 * 查询本地数据
	 * @return
	 */
	public ArrayList<JSONObject> queryTask(String uId){
		ArrayList<JSONObject> taskList = new ArrayList<>();
		db = mHelper.getReadableDatabase();
		// 游标
		/**
		 * table 表名 columns 要查询的列 selection 选择条件 selectionArgs 选择条件参数 groupBy
		 * 分组查询 having 和groupby连用 orderby 是否排序
		 */
		Cursor cursor = db.query(TABLE_NAME, null, USER_ID + " =?", new String[]{String.valueOf(uId)}, null, null,"_id desc");
		if (cursor != null && cursor.getCount() > 0) {
			// 循环遍历 取出所有的数据
			while (cursor.moveToNext()) {
				JSONObject task = new JSONObject();
				try {
					task.put("uid",cursor.getString(cursor.getColumnIndex(USER_ID))+"");
					task.put("tid", cursor.getString(cursor.getColumnIndex(TASK_ID))+"");
					task.put("index_time", cursor.getInt(cursor.getColumnIndex(INDEX_TIME)));

				} catch (JSONException e) {
					e.printStackTrace();
				}


				taskList.add(task);
			}

		}
		Log.d(TAG, "------chaxun成功"+taskList.size());
		db.close();
		return taskList;

	}
	/**
	 * 删除表中所有记录
	 */
	public void remove(){
		db = mHelper.getWritableDatabase();
		db.execSQL("DELETE FROM "+TABLE_NAME);
		db.close();
	}



}
