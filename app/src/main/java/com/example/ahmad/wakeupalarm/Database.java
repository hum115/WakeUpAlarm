package com.example.ahmad.wakeupalarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by Ahmad on 4/13/2016.
 */
public class Database extends SQLiteOpenHelper {
    public static final String Name = "Alarms.db";
    public static final String table_Name = "list_of_Alarms";
    public static final String column_1= "ID";
    public static final String column_2 = "INTENT";
    public static final String column_3= "Date";


    public Database (Context context){
        super(context,Name,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Name + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, Date TEXT)");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Name);
    }

    public boolean pushAlarm(String intent,String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(column_2,intent);
        contentValues.put(column_3,date);
        long result = db.insert(Name, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;


    }

    public Cursor readAlarm() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor alarms = db.rawQuery("select * from " + Name, null);
        return alarms;
    }

}
