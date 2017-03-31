package com.tgf.study.weathersample.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tugaofeng on 17/3/30.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "weatherDB";
    private static final int DB_VERSION = 1;

    public static final String TABLE_PROVINCE = "province";
    public static final String TABLE_CITY = "city";
    public static final String TABLE_COUNTY = "county";

    private static final String CREATE_PROVINCE = "create table province(id text, name text)";
    private static final String CREATE_CITY = "create table city(id text,name text,province_id text)";
    private static final String CREATE_COUNTY = "create table county(id text,name text, weather_id text, city_id)";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
