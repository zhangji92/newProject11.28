package com.example.lenovo.amuse.util;

import android.content.Context;
import android.util.Log;

import com.example.lenovo.amuse.mode.ResultCodeBean;

import net.tsz.afinal.FinalDb;

import java.util.List;

/**
 * Created by lenovo on 2016/9/28.
 * 数据库操作
 */

public class MyFinalDB {
    private FinalDb finalDb;

    private static MyFinalDB db;
    private MyFinalDB(Context context){
        finalDb=FinalDb.create(context,"test.db");
    }
    public static MyFinalDB getInstance(Context context){
        if (db==null){
            db=new MyFinalDB(context);
        }
        return db;
    }

    /**
     * 保存数据
     * @param successMode 数据源
     */
    public void saveFinalDB(ResultCodeBean successMode) {
        boolean b=finalDb.saveBindId(successMode);
        Log.e("aaa", "saveFinalDB: "+b );
    }
    /**
     * 更新数据
     * @param successMode 数据源
     */
    public void upDataFinalDB(ResultCodeBean successMode) {
        finalDb.update(successMode);
    }
    /**
     * 删除数据
     * @param successMode 数据源
     */
    public void deleteFinalDB( ResultCodeBean successMode) {
        finalDb.delete(successMode);
    }
    /**
     * 查询数据
     */
    public ResultCodeBean selectFinalDB() {

        List<ResultCodeBean> list=finalDb.findAll(ResultCodeBean.class);
        ResultCodeBean resultCodeBean=null;
        if (list.size()>0){
            resultCodeBean=list.get(0);

        }
        return resultCodeBean;
    }
}
