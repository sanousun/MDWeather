package com.sanousun.mdweather.support.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "db_city_list";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "table_city_list";
    public static final String TABLE_COL_CITY_NAME = "column_city_name";
    public static final String TABLE_COL_CITY_ID = "column_city_id";
    public static final String TABLE_COL_ID = "column_id";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    TABLE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TABLE_COL_CITY_NAME + " CHAR(8), " +
                    TABLE_COL_CITY_ID + " NOT NULL" +
                    ");";

    private static final String TABLE_DROP =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(TABLE_DROP);
        onCreate(sqLiteDatabase);
    }
}
