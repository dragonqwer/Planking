package com.dragon.planking.sql;

import java.util.ArrayList;
import com.dragon.planking.MenuActivity;
import com.dragon.planking.model.Data;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    
    String TAG = "DBManager";
    private DataDBHHelper helper;
    private SQLiteDatabase db;
    private Context mcontext;
    
    public DBManager(Context context) {
        mcontext = context;
        helper = new DataDBHHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }
    
    
//    /**
//     * add goods
//     * @param goods
//     */
//    public void addGoodsList(List<Goods> goods) {
//        db.beginTransaction();  //开始事务
//        try {
//            for (Goods good : goods) {
//                ContentValues cv = new ContentValues();//实例化一个ContentValues用来装载待插入的数据
//                cv.put("name",good.getName());//商品名称
//                cv.put("fixedprice",good.getFixedprice()); //商品固定价格
//                cv.put("highinfos", good.getHighinfos());
//                cv.put("lowinfos", good.getLowinfos());
//                db.insert(DataDBHHelper.GOODS_TABLE,null,cv);//执行插入操作
//            }
//            db.setTransactionSuccessful();  //设置事务成功完成
//        } finally {
//            db.endTransaction();    //结束事务
//        }
//    }
    /**
     * add data
     * @param data
     */
    public void addData(final Data data) {
//        SharedPreferences DataSave = mcontext.getSharedPreferences(MenuActivity.DataSave, Context.MODE_PRIVATE);
//        if(DataSave.getLong("Max", -1) < data.getResult()){
//            
//            DataSave.edit().putLong("Max", data.getResult()).commit();
//        }

        
        db.beginTransaction();  //开始事务
        try {
            ContentValues cv = new ContentValues();//实例化一个ContentValues用来装载待插入的数据

            cv.put("date_y",data.getDate_y());
            cv.put("date_mo",data.getDate_mo());
            cv.put("date_d",data.getDate_d());
            cv.put("date_h",data.getDate_h());
            cv.put("date_m",data.getDate_m());
            cv.put("date_s",data.getDate_s());
            cv.put("result",data.getResult()); 

            db.insert(DataDBHHelper.DATA_TABLE,null,cv);//执行插入操作
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }
    
    

    
    /**
     * query all Data, return list
     * @return List<Data>
     */
    public ArrayList<Data> queryDataList() {
        ArrayList<Data> datalist = new ArrayList<Data>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            Data data = new Data();
            data.setDate_y(c.getInt(c.getColumnIndex("date_y")));
            data.setDate_mo(c.getInt(c.getColumnIndex("date_mo")));
            data.setDate_d(c.getInt(c.getColumnIndex("date_d")));
            data.setDate_h(c.getInt(c.getColumnIndex("date_h")));
            data.setDate_m(c.getInt(c.getColumnIndex("date_m")));
            data.setDate_s(c.getInt(c.getColumnIndex("date_s")));

            data.setResult(c.getLong(c.getColumnIndex("result")));
//            Log.d(TAG,"query=="+good.toString());
            datalist.add(data);
        }
        c.close();
        return datalist;
    }
    
    /**
     * query all persons, return cursor
     * @return  Cursor
     */
    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM "+DataDBHHelper.DATA_TABLE, null);
        return c;
    }
    
    public boolean tabIsNotEmpty(String tabName){
        boolean result = false;
        if(tabName == null){
                return false;
        }
        try {
               
            Cursor c = queryTheCursor(); 
                if(c.getCount()>0)
                {
                    return true;
                }
                
        } catch (Exception e) {
                // TODO: handle exception
        }                
        return result;
    }
    
    public void deleteDB()
    {
        db.execSQL("DROP TABLE IF EXISTS "+DataDBHHelper.DATA_TABLE);
    }
    
    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }
}
