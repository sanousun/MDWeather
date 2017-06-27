package com.sanousun.mdweather.support.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * 数据库帮助类
 */

class DataBaseHelper(context: Context) :
        SQLiteOpenHelper(context, DataBaseHelper.DATABASE_NAME, null, DataBaseHelper.DATABASE_VERSION) {

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CITY_CREATE)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL(TABLE_CITY_DROP)
        onCreate(sqLiteDatabase)
    }

    companion object {

        /** 数据库名*/
        val DATABASE_NAME = "db_mdweather"
        /** 数据库版本*/
        val DATABASE_VERSION = 1

        /** 城市表名*/
        val TABLE_CITY = "table_city"
        /** 主键*/
        val COL_PRIMARY_ID = "city_primary_id"
        val COL_CITY_ID = "city_id"
        val COL_CITY_NAME = "city_name"
        val COL_PROV_NAME = "prov_name"
        val COL_CNTY_NAME = "cnty_name"
        val COL_LAT = "lat"
        val COL_LON = "lon"

        /** 创建city表*/
        private val TABLE_CITY_CREATE =
                "CREATE TABLE " +
                        TABLE_CITY + "(" +
                        COL_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_CITY_ID + " VARCHAR(32) NOT NULL, " +
                        COL_CITY_NAME + " VARCHAR(64), " +
                        COL_PROV_NAME + " VARCHAR(64), " +
                        COL_CNTY_NAME + " VARCHAR(64), " +
                        COL_LAT + " VARCHAR(32), " +
                        COL_LON + " VARCHAR(32)" +
                        ");"

        /** 删除city表*/
        private val TABLE_CITY_DROP =
                "DROP TABLE IF EXISTS " +
                        TABLE_CITY
    }
}
