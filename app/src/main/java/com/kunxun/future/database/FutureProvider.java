package com.kunxun.future.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;

import android.content.UriMatcher;
import android.database.Cursor;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Objects;


public class FutureProvider extends ContentProvider {

    private DatabaseHelper mOpenHelper;

    private static final int MINUTE_DATA =1;
    private static final int MINUTE_DATA_ID =2;

    private static final int MINUTES5_DATA = 3;
    private static final int MINUTES5_DATA_ID = 4;

    private static final int HOUR_DATA = 5;
    private static final int HOUR_DATA_ID = 6;

    private static final int DAY_DATA = 7;
    private static final int DAY_DATA_ID = 8;

    private static final UriMatcher sUriMatcher;

    private static HashMap<String, String> projectionMap;

    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    //region query
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String orderBy;


        switch(sUriMatcher.match(uri)) {
            case MINUTE_DATA:
            case MINUTE_DATA_ID:
                qb.setTables(Provider.MinuteDataColumns.TABLE_NAME);
                if (TextUtils.isEmpty(sortOrder)) {
                    orderBy = Provider.MinuteDataColumns.DEFAULT_SORT_ORDER;
                } else {
                    orderBy = sortOrder;
                }
                break;

            case MINUTES5_DATA:
            case MINUTES5_DATA_ID:
                qb.setTables(Provider.Minutes5DataColumns.TABLE_NAME);
                if (TextUtils.isEmpty(sortOrder)) {
                    orderBy = Provider.Minutes5DataColumns.DEFAULT_SORT_ORDER;
                } else {
                    orderBy = sortOrder;
                }
                break;

            case HOUR_DATA:
            case HOUR_DATA_ID:
                qb.setTables(Provider.HourDataColumns.TABLE_NAME);
                if (TextUtils.isEmpty(sortOrder)) {
                    orderBy = Provider.HourDataColumns.DEFAULT_SORT_ORDER;
                } else {
                    orderBy = sortOrder;
                }
                break;

            case DAY_DATA:
            case DAY_DATA_ID:
                qb.setTables(Provider.DayDataColumns.TABLE_NAME);
                if (TextUtils.isEmpty(sortOrder)) {
                    orderBy = Provider.DayDataColumns.DEFAULT_SORT_ORDER;
                } else {
                    orderBy = sortOrder;
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }

        switch (sUriMatcher.match(uri)) {
            case MINUTE_DATA:
            case MINUTES5_DATA:
            case HOUR_DATA:
            case DAY_DATA:
                qb.setProjectionMap(projectionMap);
                break;

            case MINUTE_DATA_ID:
                qb.setProjectionMap(projectionMap);
                qb.appendWhere(Provider.MinuteDataColumns.INSTRUMENT_ID + " = " + uri.getPathSegments().get(1));
                break;

            case MINUTES5_DATA_ID:
                qb.setProjectionMap(projectionMap);
                qb.appendWhere(Provider.Minutes5DataColumns.INSTRUMENT_ID + " = " + uri.getPathSegments().get(1));
                break;

            case HOUR_DATA_ID:
                qb.setProjectionMap(projectionMap);
                qb.appendWhere(Provider.HourDataColumns.INSTRUMENT_ID + " = " + uri.getPathSegments().get(1));
                break;

            case DAY_DATA_ID:
                qb.setProjectionMap(projectionMap);
                qb.appendWhere(Provider.DayDataColumns.INSTRUMENT_ID + " = " + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI" + uri);

        }

        // Get the database and run the query
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null,orderBy);

        // Tell the cursor what uri to watch, so it knows when its source data changes
        c.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(),uri);
        return c;
    }
    //endregion

    //region getType
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        switch (sUriMatcher.match(uri)) {
            case MINUTE_DATA:
            case MINUTES5_DATA:
            case HOUR_DATA:
            case DAY_DATA:
                return Provider.CONTENT_TYPE;
            case MINUTE_DATA_ID:
            case MINUTES5_DATA_ID:
            case HOUR_DATA_ID:
            case DAY_DATA_ID:
                return Provider.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }
    //endregion

    //region insert
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        SQLiteDatabase db =mOpenHelper.getWritableDatabase();
        String tableName, instrumentId;
        Uri contentUri;

        switch (Objects.requireNonNull(sUriMatcher).match(uri)) {
            case MINUTE_DATA:
                tableName = Provider.MinuteDataColumns.TABLE_NAME;
                instrumentId = Provider.MinuteDataColumns.INSTRUMENT_ID;
                contentUri = Provider.MinuteDataColumns.CONTENT_URI;
                break;

            case MINUTES5_DATA:
                tableName = Provider.Minutes5DataColumns.TABLE_NAME;
                instrumentId = Provider.Minutes5DataColumns.INSTRUMENT_ID;
                contentUri = Provider.Minutes5DataColumns.CONTENT_URI;
                break;

            case HOUR_DATA:
                tableName =Provider.HourDataColumns.TABLE_NAME;
                instrumentId = Provider.HourDataColumns.INSTRUMENT_ID;
                contentUri = Provider.HourDataColumns.CONTENT_URI;
                break;

            case DAY_DATA:
                tableName = Provider.DayDataColumns.TABLE_NAME;
                instrumentId = Provider.DayDataColumns.INSTRUMENT_ID;
                contentUri = Provider.DayDataColumns.CONTENT_URI;
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        long rowId = db.insert(tableName, instrumentId, values);
        if (rowId >0)
        {
            Uri noteUri = ContentUris.withAppendedId(contentUri, rowId);
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

//    public boolean insertList(List<MinuteData> list){
//
//        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
//        if (list == null || list.size() == 0)
//        {
//            return false;
//        }
//
//        try {
//            String sql = "INSERT INTO " + Provider.MinuteDataColumns.TABLE_NAME + "("
//                    +Provider.MinuteDataColumns.INSTRUMENT_ID +", "
//                    +Provider.MinuteDataColumns.TRADING_DAY+", "
//                    +Provider.MinuteDataColumns.UPDATE_TIME +", "
//                    +Provider.MinuteDataColumns.OPEN_PRICE +", "
//                    +Provider.MinuteDataColumns.CLOSE_PRICE +", "
//                    +Provider.MinuteDataColumns.MACD_DIFF +", "
//                    +Provider.MinuteDataColumns.TARGET_PRICE
//                    +") "
//                    +"VALUES (?, ?, ?, ?, ?, ?, ?)";
//
//            //预编译Sql语句避免重复解析Sql语句
//            SQLiteStatement stat = db.compileStatement(sql);
//            //开启事务
//            db.beginTransaction();
//            for (MinuteData data : list) {
//                stat.bindString(1, data.instrumentId);
//                stat.bindString(2, data.tradingDay);
//                stat.bindString(3, data.updateTime);
//                stat.bindDouble(4, data.openPrice);
//                stat.bindDouble(5, data.closePrice);
//                stat.bindDouble(6, data.macdDiff);
//                stat.bindDouble(7, data.targetPrice);
//                long result = stat.executeInsert();
//                if (result < 0) {
//                    return false;
//                }
//            }
//            //控制回滚，如果不设置此项自动回滚
//            db.setTransactionSuccessful();
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            return false;
//        }
//        finally {
//            try {
//                if (null != db) {
//                    //事务提交
//                    db.endTransaction();
//                    db.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return true;
//    }
    //endregion

    //region delete
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
    //endregion

    //region update
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
    //endregion

    //region initData
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(Provider.AUTHORITY, "minuteData", MINUTE_DATA);
        sUriMatcher.addURI(Provider.AUTHORITY, "minuteData/#", MINUTE_DATA_ID);

        sUriMatcher.addURI(Provider.AUTHORITY, "minuteData", MINUTES5_DATA);
        sUriMatcher.addURI(Provider.AUTHORITY, "minuteData/#", MINUTES5_DATA_ID);

        sUriMatcher.addURI(Provider.AUTHORITY, "minuteData", HOUR_DATA);
        sUriMatcher.addURI(Provider.AUTHORITY, "minuteData/#", HOUR_DATA_ID);

        sUriMatcher.addURI(Provider.AUTHORITY, "minuteData", DAY_DATA);
        sUriMatcher.addURI(Provider.AUTHORITY, "minuteData/#", DAY_DATA_ID);


        projectionMap = new HashMap<>();
        projectionMap.put("m" + Provider.MinuteDataColumns._ID, Provider.MinuteDataColumns._ID);
        projectionMap.put("m" + Provider.MinuteDataColumns.INSTRUMENT_ID, Provider.MinuteDataColumns.INSTRUMENT_ID);
        projectionMap.put("m" + Provider.MinuteDataColumns.TRADING_DAY, Provider.MinuteDataColumns.TRADING_DAY);
        projectionMap.put("m" + Provider.MinuteDataColumns.UPDATE_TIME, Provider.MinuteDataColumns.UPDATE_TIME);
        projectionMap.put("m" + Provider.MinuteDataColumns.OPEN_PRICE, Provider.MinuteDataColumns.OPEN_PRICE);
        projectionMap.put("m" + Provider.MinuteDataColumns.CLOSE_PRICE, Provider.MinuteDataColumns.CLOSE_PRICE);
        projectionMap.put("m" + Provider.MinuteDataColumns.MACD_DIFF, Provider.MinuteDataColumns.MACD_DIFF);
        projectionMap.put("m" + Provider.MinuteDataColumns.TARGET_PRICE, Provider.MinuteDataColumns.TARGET_PRICE);

        projectionMap.put("ms" + Provider.Minutes5DataColumns._ID, Provider.Minutes5DataColumns._ID);
        projectionMap.put("ms" + Provider.Minutes5DataColumns.INSTRUMENT_ID, Provider.Minutes5DataColumns.INSTRUMENT_ID);
        projectionMap.put("ms" + Provider.Minutes5DataColumns.TRADING_DAY, Provider.Minutes5DataColumns.TRADING_DAY);
        projectionMap.put("ms" + Provider.Minutes5DataColumns.UPDATE_TIME, Provider.Minutes5DataColumns.UPDATE_TIME);
        projectionMap.put("ms" + Provider.Minutes5DataColumns.OPEN_PRICE, Provider.Minutes5DataColumns.OPEN_PRICE);
        projectionMap.put("ms" + Provider.Minutes5DataColumns.CLOSE_PRICE, Provider.Minutes5DataColumns.CLOSE_PRICE);
        projectionMap.put("ms" + Provider.Minutes5DataColumns.M20VALUE, Provider.Minutes5DataColumns.M20VALUE);
        projectionMap.put("ms" + Provider.Minutes5DataColumns.TENDENCY, Provider.Minutes5DataColumns.TENDENCY);

        projectionMap.put("h" + Provider.HourDataColumns._ID, Provider.HourDataColumns._ID);
        projectionMap.put("h" + Provider.HourDataColumns.INSTRUMENT_ID, Provider.HourDataColumns.INSTRUMENT_ID);
        projectionMap.put("h" + Provider.HourDataColumns.TRADING_DAY, Provider.HourDataColumns.TRADING_DAY);
        projectionMap.put("h" + Provider.HourDataColumns.UPDATE_TIME, Provider.HourDataColumns.UPDATE_TIME);
        projectionMap.put("h" + Provider.HourDataColumns.OPEN_PRICE, Provider.HourDataColumns.OPEN_PRICE);
        projectionMap.put("h" + Provider.HourDataColumns.CLOSE_PRICE, Provider.HourDataColumns.CLOSE_PRICE);
        projectionMap.put("h" + Provider.HourDataColumns.M20VALUE, Provider.HourDataColumns.M20VALUE);
        projectionMap.put("h" + Provider.HourDataColumns.TENDENCY, Provider.HourDataColumns.TENDENCY);

        projectionMap.put("d" + Provider.DayDataColumns._ID, Provider.DayDataColumns._ID);
        projectionMap.put("d" + Provider.DayDataColumns.INSTRUMENT_ID, Provider.DayDataColumns.INSTRUMENT_ID);
        projectionMap.put("d" + Provider.DayDataColumns.TRADING_DAY, Provider.DayDataColumns.TRADING_DAY);
        projectionMap.put("d" + Provider.DayDataColumns.UPDATE_TIME, Provider.DayDataColumns.UPDATE_TIME);
        projectionMap.put("d" + Provider.DayDataColumns.OPEN_PRICE, Provider.DayDataColumns.OPEN_PRICE);
        projectionMap.put("d" + Provider.DayDataColumns.CLOSE_PRICE, Provider.DayDataColumns.CLOSE_PRICE);
        projectionMap.put("d" + Provider.DayDataColumns.M20VALUE, Provider.DayDataColumns.M20VALUE);
        projectionMap.put("d" + Provider.DayDataColumns.TENDENCY, Provider.DayDataColumns.TENDENCY);

    }
    //endregion
}
