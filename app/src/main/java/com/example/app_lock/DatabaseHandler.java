package com.example.app_lock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

public class DatabaseHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_NAME = "App_Locker";
    private static final String TABLE_NAME = "Installed";
    private static final String TABLE_NAME2 = "locked";
    private static final String APP_ID = "id";
    private static final String APP_ID2 = "id2";

    private static final String APP_NAME = "app_name";
    private static final String APP_STATUS = "status";
    Cursor cursor;
    ArrayList<String> ListViewClickItemArray = new ArrayList<String>();

    public DatabaseHandler(@Nullable Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ArrayList<String> Name = new ArrayList<String>();

        String applist = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + APP_ID + " INTEGER PRIMARY KEY," + APP_NAME + " TEXT,"
                + APP_STATUS + " INTEGER" + ")";
        db.execSQL(applist);

        String lockedapplist = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME2 + "("
                + APP_ID2 + " INTEGER PRIMARY KEY," + APP_NAME + " TEXT,"
                + APP_STATUS + " INTEGER" + ")";
        db.execSQL(lockedapplist);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addApp(AppModel app) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(APP_NAME, app.getAppname());
        values.put(APP_STATUS, app.getStatus());

        db.insert(TABLE_NAME, null, values);
    }




    void display(){
        SQLiteDatabase db = this.getWritableDatabase();
        ListViewClickItemArray=  new ArrayList<String>();
        cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME2+"", null);
        if (cursor.moveToFirst()) {
            do {

                //Inserting Column ID into Array to Use at ListView Click Listener Method.
                ListViewClickItemArray.add(cursor.getString(cursor.getColumnIndex(APP_NAME)));

            }while (cursor.moveToNext());
        }
        db.close();
        Log.e("KEYY", String.valueOf(ListViewClickItemArray));
    }


    public List<AppModel> getAllApps() {
        List<AppModel> appList = new ArrayList<AppModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                AppModel app = new AppModel();
                app.setAppname(cursor.getString(1));
                app.setStatus(Integer.parseInt(String.valueOf(cursor.getInt(2))));
                appList.add(app);
            } while (cursor.moveToNext());
        }
        return appList;
    }

    void addApptoLockedList(AppModel app) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(APP_NAME, app.getAppname());
        values.put(APP_STATUS, 1);

        db.insert(TABLE_NAME2, null, values);
    }

    void removeAppfromLockedList(AppModel app) {
        SQLiteDatabase db = this.getWritableDatabase();
        // db.delete(TABLE_NAME2,APP_NAME+"=" + app.getAppname(),null);
        db.execSQL("DELETE FROM " + TABLE_NAME2+ " WHERE "+APP_NAME+"='"+app.getAppname()+"'");
        db.close();
    }

    public List<AppModel> getAllLockedApps() {
        List<AppModel> appList = new ArrayList<AppModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME2;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                AppModel app = new AppModel();
                app.setAppname(cursor.getString(1));
                app.setStatus(1);
                appList.add(app);
            } while (cursor.moveToNext());
        }
        return appList;
    }

    public void changeStatus(AppModel app, int status) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE "+TABLE_NAME+" SET status = " + status  + " WHERE "+APP_NAME +" = " +"'"+ app.getAppname()+"'");
    }
}
