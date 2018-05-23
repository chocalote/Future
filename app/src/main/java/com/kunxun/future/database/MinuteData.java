package com.kunxun.future.database;


public class MinuteData {

    public String instrumentId;
    public String tradingDay;
    public String updateTime;
    public double openPrice;
    public double closePrice;
    public double macd;
    public double targetPrice;


//    public int insert(Context context)
//    {
//        ContentValues values = new ContentValues();
//        values.put(Provider.MinuteDataColumns.INSTRUMENT_ID, instrumentId);
//        values.put(Provider.MinuteDataColumns.TRADING_DAY, tradingDay);
//        values.put(Provider.MinuteDataColumns.UPDATE_TIME, updateTime);
//        values.put(Provider.MinuteDataColumns.OPEN_PRICE, openPrice);
//        values.put(Provider.MinuteDataColumns.CLOSE_PRICE, closePrice);
//        values.put(Provider.MinuteDataColumns.MACD, macd);
//        values.put(Provider.MinuteDataColumns.TARGET_PRICE, targetPrice);
//
//        Uri uri = context.getContentResolver().insert(Provider.MinuteDataColumns.CONTENT_URI, values);
//
//        Log.i("Lily", "insert uri = "+uri);
//        String lastPath = uri.getPathSegments().get(1);
//        if (TextUtils.isEmpty(lastPath)){
//            Log.i("Lily", "insert failure!");
//        }
//        else {
//            Log.i("Lily", "insert success! the id is " + lastPath);
//        }
//        return Integer.parseInt(lastPath);
//    }
//
//    public Cursor query(Context context, String insId) {
//
//       return context.getContentResolver().query(Provider.MinuteDataColumns.CONTENT_URI,
//                Provider.MinuteDataColumns.DEFAULT_QUERY_COLUMNS,
//                Provider.MinuteDataColumns.INSTRUMENT_ID + " = '",
//                new String[]{insId + "'"},
//                Provider.MinuteDataColumns.DEFAULT_SORT_ORDER);
//    }
}
