package com.tgf.study.weathersample.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tgf.study.weathersample.bean.area.CountyBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tugaofeng on 17/3/31.
 */
public class CountyDAO {
    private static final String TAG = "CountyDAO";
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public CountyDAO(Context mContext) {
        dbHelper = new DBHelper(mContext);
    }

    public void insert(CountyBean bean){
        Log.i(TAG, "insert: cityId= "+bean.getCityId());
        db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put("id",bean.getId());
        values.put("name",bean.getName());
        values.put("weather_id",bean.getWeatherId());
        values.put("city_id",bean.getCityId());
        db.insert(DBHelper.TABLE_COUNTY,null,values);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public List<CountyBean> query(String cityId){
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_COUNTY,null,"city_id=?",new String[]{cityId},null,null,null);
        List<CountyBean> list = new ArrayList<CountyBean>();
        while (cursor.moveToNext()){
            CountyBean bean = new CountyBean();
            bean.setId(cursor.getString(cursor.getColumnIndex("id")));
            bean.setName(cursor.getString(cursor.getColumnIndex("name")));
            bean.setCityId(cursor.getString(cursor.getColumnIndex("city_id")));
            bean.setWeatherId(cursor.getString(cursor.getColumnIndex("weather_id")));
            list.add(bean);
        }
        cursor.close();
        db.close();
        return list;
    }
}
