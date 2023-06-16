package com.example.taxifare.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.taxifare.model.Trip

private const val DATABASE_NAME = "TaxiFare.db"
private const val DATABASE_VERSION = 1

private const val TABLE_NAME = "trip"
private const val COLUMN_ID = "id"
private const val COLUMN_DISTANCE = "distance"
private const val COLUMN_TIME = "time"
private const val COLUMN_FARE = "fare"
private const val COLUMN_DATE = "date"

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_DISTANCE REAL," +
                    "$COLUMN_TIME REAL," +
                    "$COLUMN_FARE REAL," +
                    "$COLUMN_DATE TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
    // Thêm chuyến đi mới vào cơ sở dữ liệu

fun addTrip(trip: Trip) {
        val values = ContentValues().apply {
            put(COLUMN_DISTANCE, trip.distance)
            put(COLUMN_TIME, trip.time)
            put(COLUMN_FARE, trip.fare)
            put(COLUMN_DATE, trip.date)
        }
        writableDatabase.insert(TABLE_NAME, null, values)
    }

    // Lấy danh sách các chuyến đi đã lưu trong cơ sở dữ liệu
    fun getAllTrips(): List<Trip> {
        val trips = mutableListOf<Trip>()
        val cursor = readableDatabase.query(TABLE_NAME, null, null, null, null, null, null)
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COLUMN_ID))
                val distance = getFloat(getColumnIndexOrThrow(COLUMN_DISTANCE))
                val time = getDouble(getColumnIndexOrThrow(COLUMN_TIME))
                val fare = getDouble(getColumnIndexOrThrow(COLUMN_FARE))
                val date = getString(getColumnIndexOrThrow(COLUMN_DATE))
                trips.add(Trip(id, distance, time, fare, date))
            }
        }
        cursor.close()
        return trips
    }
}