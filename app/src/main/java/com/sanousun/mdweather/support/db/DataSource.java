package com.sanousun.mdweather.support.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.sanousun.mdweather.support.db.DataBaseHelper.*;

public class DataSource {

    private final DataBaseHelper mHelper;
    private SQLiteDatabase mDatabase;

    public DataSource(Context context) {
        mHelper = new DataBaseHelper(context);
    }

    public void open() {
        mDatabase = mHelper.getWritableDatabase();
    }

    //----------------------------------------------------------------------------
    public void add(String name, String id) {
        ContentValues values = new ContentValues();
        values.put(TABLE_COL_CITY_NAME, name);
        values.put(TABLE_COL_CITY_ID, id);
        mDatabase.insert(TABLE_NAME, null, values);
    }

    public void update(String name, String id) {
        ContentValues values = new ContentValues();
        values.put(TABLE_COL_CITY_NAME, name);
        values.put(TABLE_COL_CITY_ID, id);
        mDatabase.update(TABLE_NAME, values, TABLE_COL_CITY_ID + "=?", new String[]{id});
    }

    public void insert(String name, String id) {
        if (getCityId(name) != null) {
            update(name, id);
        } else {
            add(name, id);
        }
    }

    public String getCityId(String name) {
        String cityId = null;
        Cursor cursor = mDatabase.query(TABLE_NAME, new String[]{TABLE_COL_CITY_ID},
                TABLE_COL_CITY_NAME + "=?", new String[]{name}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cityId = cursor.getString(cursor.getColumnIndex(TABLE_COL_CITY_ID));
        }
        cursor.close();
        return cityId;
    }

    public void delete(String id) {
        mDatabase.delete(TABLE_NAME, TABLE_COL_CITY_ID + "=?", new String[]{id});
    }

    public List<String> getCityIdList() {
        List<String> cityIdList = new ArrayList<>();
        Cursor cursor = mDatabase.query(TABLE_NAME, new String[]{TABLE_COL_CITY_ID},
                null, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) return cityIdList;
        do {
            cityIdList.add(cursor.getString(cursor.getColumnIndex(TABLE_COL_CITY_ID)));
        } while (cursor.moveToNext());
        cursor.close();
        return cityIdList;
    }

}
