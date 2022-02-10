package com.example.quanlynhanvien.DBManager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

public class DBManager extends SQLiteOpenHelper {
    private  final String ERROR="ErroDB";
    public DBManager(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public Boolean writeData(String sql){
        try{
            SQLiteDatabase sqLiteDatabase=getWritableDatabase();
            sqLiteDatabase.execSQL(sql);
            return true;
        }catch (Exception ex){
            Log.d(ERROR,ex.getMessage());
            return  false;
        }
    }

    public Cursor getData(String sql){
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        return sqLiteDatabase.rawQuery(sql,null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
