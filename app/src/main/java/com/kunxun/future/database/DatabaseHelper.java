package com.kunxun.future.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "future.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + Provider.MinuteDataColumns.TABLE_NAME + " ("
                + Provider.MinuteDataColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Provider.MinuteDataColumns.INSTRUMENT_ID + " TEXT, "
                + Provider.MinuteDataColumns.TRADING_DAY + " TEXT, "
                + Provider.MinuteDataColumns.UPDATE_TIME + " TEXT, "
                + Provider.MinuteDataColumns.OPEN_PRICE + " NUMERIC, "
                + Provider.MinuteDataColumns.CLOSE_PRICE + " NUMERIC, "
                + Provider.MinuteDataColumns.HIGH_PRICE + " NUMERIC, "
                + Provider.MinuteDataColumns.LOW_PRICE + " NUMERIC, "
                + Provider.MinuteDataColumns.MACD_DIFF + " NUMERIC, "
                + Provider.MinuteDataColumns.MACD_DEA + " NUMERIC, "
                + Provider.MinuteDataColumns.MACD_VALUE + " NUMERIC, "
                + Provider.MinuteDataColumns.TARGET_PRICE + " NUMERIC "
                + "); ");

        db.execSQL("CREATE TABLE " + Provider.Minutes5DataColumns.TABLE_NAME + " ("
                + Provider.Minutes5DataColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Provider.Minutes5DataColumns.INSTRUMENT_ID + " TEXT, "
                + Provider.Minutes5DataColumns.TRADING_DAY + " TEXT, "
                + Provider.Minutes5DataColumns.UPDATE_TIME + " TEXT, "
                + Provider.Minutes5DataColumns.OPEN_PRICE + " NUMERIC, "
                + Provider.Minutes5DataColumns.CLOSE_PRICE + " NUMERIC, "
                + Provider.Minutes5DataColumns.HIGH_PRICE + " NUMERIC, "
                + Provider.Minutes5DataColumns.LOW_PRICE + " NUMERIC, "
                + Provider.Minutes5DataColumns.M20VALUE + " NUMERIC, "
                + Provider.Minutes5DataColumns.TENDENCY + " TEXT "
                + "); ");

        db.execSQL("CREATE TABLE " + Provider.HourDataColumns.TABLE_NAME + " ("
                + Provider.HourDataColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Provider.HourDataColumns.INSTRUMENT_ID + " TEXT, "
                + Provider.HourDataColumns.TRADING_DAY + " TEXT, "
                + Provider.HourDataColumns.UPDATE_TIME + " TEXT, "
                + Provider.HourDataColumns.OPEN_PRICE + " NUMERIC, "
                + Provider.HourDataColumns.CLOSE_PRICE + " NUMERIC, "
                + Provider.HourDataColumns.HIGH_PRICE + " NUMERIC, "
                + Provider.HourDataColumns.LOW_PRICE + " NUMERIC, "
                + Provider.HourDataColumns.M20VALUE + " NUMERIC, "
                + Provider.HourDataColumns.TENDENCY + " TEXT "
                + "); ");

        db.execSQL("CREATE TABLE " + Provider.DayDataColumns.TABLE_NAME + " ("
                + Provider.DayDataColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Provider.DayDataColumns.INSTRUMENT_ID + " TEXT, "
                + Provider.DayDataColumns.TRADING_DAY + " TEXT, "
                + Provider.DayDataColumns.UPDATE_TIME + " TEXT, "
                + Provider.DayDataColumns.OPEN_PRICE + " NUMERIC, "
                + Provider.DayDataColumns.CLOSE_PRICE + " NUMERIC, "
                + Provider.DayDataColumns.HIGH_PRICE + " NUMERIC, "
                + Provider.DayDataColumns.LOW_PRICE + " NUMERIC, "
                + Provider.DayDataColumns.M20VALUE + " NUMERIC, "
                + Provider.DayDataColumns.TENDENCY + " TEXT "
                + "); ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Provider.MinuteDataColumns.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Provider.Minutes5DataColumns.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Provider.HourDataColumns.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Provider.DayDataColumns.TABLE_NAME);
        onCreate(db);
        onCreate(db);
    }
}
