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

    private static final int MINUTEDATA=1;
    private static final int MINUTEDATA_ID =2;

    private static final int MINUTES5DATA = 3;
    private static final int MINUTES5DATA_ID = 4;

    private static final int HOURDATA = 5;
    private static final int HOURDATA_ID = 6;

    private static final int DAYDATA = 7;
    private static final int DAYDATA_ID = 8;

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


        switch (Objects.requireNonNull(sUriMatcher).match(uri)) {
            case MINUTEDATA:
            case MINUTEDATA_ID:
                qb.setTables(Provider.MinuteDataColumns.TABLE_NAME);
                if (TextUtils.isEmpty(sortOrder)) {
                    orderBy = Provider.MinuteDataColumns.DEFAULT_SORT_ORDER;
                } else {
                    orderBy = sortOrder;
                }
                break;

            case MINUTES5DATA:
            case MINUTES5DATA_ID:
                qb.setTables(Provider.Minutes5DataColumns.TABLE_NAME);
                if (TextUtils.isEmpty(sortOrder)) {
                    orderBy = Provider.Minutes5DataColumns.DEFAULT_SORT_ORDER;
                } else {
                    orderBy = sortOrder;
                }
                break;

            case HOURDATA:
            case HOURDATA_ID:
                qb.setTables(Provider.HourDataColumns.TABLE_NAME);
                if (TextUtils.isEmpty(sortOrder)) {
                    orderBy = Provider.HourDataColumns.DEFAULT_SORT_ORDER;
                } else {
                    orderBy = sortOrder;
                }
                break;

            case DAYDATA:
            case DAYDATA_ID:
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

        switch (Objects.requireNonNull(sUriMatcher).match(uri)) {
            case MINUTEDATA:
            case MINUTES5DATA:
            case HOURDATA:
            case DAYDATA:
                qb.setProjectionMap(projectionMap);
                break;

            case MINUTEDATA_ID:
                qb.setProjectionMap(projectionMap);
                qb.appendWhere(Provider.MinuteDataColumns._ID + " = " + uri.getPathSegments().get(1));
                break;

            case MINUTES5DATA_ID:
                qb.setProjectionMap(projectionMap);
                qb.appendWhere(Provider.Minutes5DataColumns._ID + " = " + uri.getPathSegments().get(1));
                break;

            case HOURDATA_ID:
                qb.setProjectionMap(projectionMap);
                qb.appendWhere(Provider.HourDataColumns._ID + " = " + uri.getPathSegments().get(1));
                break;

            case DAYDATA_ID:
                qb.setProjectionMap(projectionMap);
                qb.appendWhere(Provider.DayDataColumns._ID + " = " + uri.getPathSegments().get(1));
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
            case MINUTEDATA:
            case MINUTES5DATA:
            case HOURDATA:
            case DAYDATA:
                return Provider.CONTENT_TYPE;
            case MINUTEDATA_ID:
            case MINUTES5DATA_ID:
            case HOURDATA_ID:
            case DAYDATA_ID:
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
            case MINUTEDATA:
                tableName = Provider.MinuteDataColumns.TABLE_NAME;
                instrumentId = Provider.MinuteDataColumns.INSTRUMENT_ID;
                contentUri = Provider.MinuteDataColumns.CONTENT_URI;
                break;

            case MINUTES5DATA:
                tableName = Provider.Minutes5DataColumns.TABLE_NAME;
                instrumentId = Provider.Minutes5DataColumns.INSTRUMENT_ID;
                contentUri = Provider.Minutes5DataColumns.CONTENT_URI;
                break;

            case HOURDATA:
                tableName =Provider.HourDataColumns.TABLE_NAME;
                instrumentId = Provider.HourDataColumns.INSTRUMENT_ID;
                contentUri = Provider.HourDataColumns.CONTENT_URI;
                break;

            case DAYDATA:
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
    //endregion

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(Provider.AUTHORITY, "minuteData", MINUTEDATA);
//        sUriMatcher.addURI(Provider.LeaderColumns.AUTHORITY, "leaders/#", LEADER_ID);

        projectionMap = new HashMap<>();
        projectionMap.put(Provider.MinuteDataColumns._ID, Provider.MinuteDataColumns._ID);
//        projectionMap.put(Provider.LeaderColumns.NAME, Provider.LeaderColumns.NAME);
//        projectionMap.put(Provider.LeaderColumns.TITLE, Provider.LeaderColumns.TITLE);
//        projectionMap.put(Provider.LeaderColumns.LEVEL, Provider.LeaderColumns.LEVEL);
    }
}
