package com.dragon.planking.sql;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataDBHHelper extends SQLiteOpenHelper
{
    String TAG = "DataDBHHelper";
    private static final String DATABASE_NAME = "test2.db";  
    public static final String DATA_TABLE = "time"; 
    private static final int version = 5;   //数据库版本  

    public DataDBHHelper(Context context, String name, CursorFactory factory, int version)
    {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }
    
    

    public DataDBHHelper(Context context){  
        
        this(context,DATABASE_NAME,null,version);  
    }  
    
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // TODO Auto-generated method stub
        Log.d(TAG,"onCreate");
        
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DATA_TABLE +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " date_y INTEGER," +
                " date_mo INTEGER," +
                " date_d INTEGER," +
                " date_h INTEGER," +
                " date_m INTEGER," +
                " date_s INTEGER," +
                " result LONG)");  
        
        //DATETIME用来存储日期/时间的不限制长度的字符串类型.要求的格式是 ‘YYYY-MM-DD HH:MM:SS’,其他的东西被忽略.
    }
    

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // TODO Auto-generated method stub
        Log.d(TAG,"onUpgrade");
        db.execSQL("DROP TABLE IF EXISTS " + DATA_TABLE);
        this.onCreate(db);
    }
    
    

}
