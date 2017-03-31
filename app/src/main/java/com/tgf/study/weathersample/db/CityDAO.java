package com.tgf.study.weathersample.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tgf.study.weathersample.bean.area.CityBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tugaofeng on 17/3/31.
 */
public class CityDAO {
    private static final String TAG = "CityDAO";
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public CityDAO(Context mContext) {
        dbHelper = new DBHelper(mContext);
    }

    public void insert(CityBean bean){
        Log.i(TAG, "insert: provinceId= "+bean.getProvinceId());

        db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put("id",bean.getId());
        values.put("name",bean.getName());
        values.put("province_id",bean.getProvinceId());
        db.insert(DBHelper.TABLE_CITY,null,values);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public List<CityBean> query(String provinceId){
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_CITY,null,"province_id=?",new String[]{provinceId},null,null,null);
        List<CityBean> list = new ArrayList<CityBean>();
        while (cursor.moveToNext()){
            CityBean bean = new CityBean();
            bean.setId(cursor.getString(cursor.getColumnIndex("id")));
            bean.setName(cursor.getString(cursor.getColumnIndex("name")));
            bean.setProvinceId(cursor.getString(cursor.getColumnIndex("province_id")));
            list.add(bean);
        }
        cursor.close();
        db.close();
        return list;
    }
}
