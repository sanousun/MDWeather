package com.sanousun.mdweather.support.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.sanousun.mdweather.model.BasicCity
import com.sanousun.mdweather.model.UpdateTime

/**
 * 数据库操作帮助类
 */

class DataSource(context: Context) {

    private val mHelper: DataBaseHelper = DataBaseHelper(context)
    private val mDatabase: SQLiteDatabase

    companion object {
        var dataSource: DataSource? = null
        fun getInstance(context: Context): DataSource? {
            if (dataSource == null) {
                dataSource = DataSource(context)
            }
            return dataSource
        }
    }

    init {
        mDatabase = mHelper.writableDatabase
    }

    /* ******************** sql操作 ******************** */

    fun save(city: BasicCity) {
        val values = ContentValues().apply {
            put(DataBaseHelper.COL_CITY_ID, city.id)
            put(DataBaseHelper.COL_CITY_NAME, city.city)
            put(DataBaseHelper.COL_PROV_NAME, city.prov)
            put(DataBaseHelper.COL_CNTY_NAME, city.cnty)
            put(DataBaseHelper.COL_LAT, city.lat)
            put(DataBaseHelper.COL_LON, city.lon)
        }
        if (!isCityExist(city.id)) {
            mDatabase.insert(DataBaseHelper.TABLE_CITY, null, values)
        } else {
            mDatabase.update(DataBaseHelper.TABLE_CITY, values, DataBaseHelper.COL_CITY_ID + "=?", arrayOf(city.id))
        }
    }

    fun isCityExist(name: String): Boolean {
        var isCityExist = false
        val cursor = mDatabase.query(
                DataBaseHelper.TABLE_CITY,
                arrayOf(DataBaseHelper.COL_CITY_ID),
                DataBaseHelper.COL_CITY_ID + "=?",
                arrayOf(name),
                null, null, null)
        cursor.moveToFirst()
        if (cursor.count > 0) {
            isCityExist = true
        }
        cursor.close()
        return isCityExist
    }

    fun delete(cityId: String) {
        mDatabase.delete(DataBaseHelper.TABLE_CITY, DataBaseHelper.COL_CITY_ID + "=?", arrayOf(cityId))
    }

    fun getCityList(): List<BasicCity> {
        val cityIdList = ArrayList<BasicCity>()
        val cursor = mDatabase.query(DataBaseHelper.TABLE_CITY, null, null, null, null, null, null)
        cursor.moveToFirst()
        if (cursor.count == 0) return cityIdList
        do {
            cityIdList.add(BasicCity(
                    cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL_CITY_ID)),
                    cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL_CITY_NAME)),
                    cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL_PROV_NAME)),
                    cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL_CNTY_NAME)),
                    cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL_LAT)),
                    cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL_LON)),
                    UpdateTime("", ""))
            )
        } while (cursor.moveToNext())
        cursor.close()
        return cityIdList
    }
}
