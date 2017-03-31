package com.tgf.study.weathersample.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tgf.study.weathersample.bean.area.ProvinceBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tugaofeng on 17/3/30.
 */
public class ProvinceDAO {
    private static final String TAG = "ProvinceDAO";
    private Context mContext;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public ProvinceDAO(Context mContext) {
        this.mContext = mContext;
        this.dbHelper = new DBHelper(mContext);
    }

    public void insert(ProvinceBean bean){
        Log.i(TAG, "insert: ");
        
        db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put("id",bean.getId());
        values.put("name",bean.getName());
        db.insert(DBHelper.TABLE_PROVINCE,null,values);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public List<ProvinceBean> query(){
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_PROVINCE,null,null,null,null,null,null);
        List<ProvinceBean> list = new ArrayList<ProvinceBean>();
        if (cursor != null){
            while (cursor.moveToNext()){
                ProvinceBean bean = new ProvinceBean();
                bean.setId(cursor.getString(cursor.getColumnIndex("id")));
                bean.setName(cursor.getString(cursor.getColumnIndex("name")));
                list.add(bean);
            }
        }
        cursor.close();
        db.close();
        return list;
    }

}
