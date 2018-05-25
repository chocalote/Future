package com.kunxun.future.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class Provider {

    public static final String AUTHORITY = "com.kunxun.future.Database";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.kunxun.future";

    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.kunxun.future";


    public static final class MinuteDataColumns implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/minutedata");

        public static final String TABLE_NAME = "MinuteData";
        public static final String DEFAULT_SORT_ORDER = "TradingDay, UpdateTime asc";


        public static final String INSTRUMENT_ID = "InstrumentId";
        public static final String TRADING_DAY = "TradingDay";
        public static final String UPDATE_TIME = "UpdateTime";
        public static final String OPEN_PRICE = "OpenPrice";
        public static final String CLOSE_PRICE = "ClosePrice";
        public static final String HIGH_PRICE = "HighPrice";
        public static final String LOW_PRICE = "LowPrice";
        public static final String MACD_DIFF = "MACD_Diff";
        public static final String MACD_DEA = "MACD_Dea";
        public static final String MACD_VALUE = "MACD_Value";
        public static final String TARGET_PRICE = "TargetPrice";

        public static final String[] DEFAULT_QUERY_COLUMNS = {INSTRUMENT_ID, TRADING_DAY,
                UPDATE_TIME, OPEN_PRICE, CLOSE_PRICE, HIGH_PRICE, LOW_PRICE, MACD_DIFF, MACD_DEA, MACD_VALUE, TARGET_PRICE};

    }

    public static final class Minutes5DataColumns implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/minutes5data");

        public static final String TABLE_NAME = "Minutes5Data";
        public static final String DEFAULT_SORT_ORDER = "TradingDay, UpdateTime asc";

        public static final String INSTRUMENT_ID = "InstrumentId";
        public static final String TRADING_DAY = "TradingDay";
        public static final String UPDATE_TIME = "UpdateTime";
        public static final String OPEN_PRICE = "OpenPrice";
        public static final String CLOSE_PRICE = "ClosePrice";
        public static final String HIGH_PRICE = "HighPrice";
        public static final String LOW_PRICE = "LowPrice";
        public static final String M20VALUE = "M20Value";
        public static final String TENDENCY = "Tendency";

        public static final String[] DEFAULT_QUERY_COLUMNS = {INSTRUMENT_ID, TRADING_DAY,
                UPDATE_TIME, OPEN_PRICE, CLOSE_PRICE, HIGH_PRICE, LOW_PRICE, M20VALUE, TENDENCY};
    }

    public static final class HourDataColumns implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/hourdata");

        public static final String TABLE_NAME = "HourData";
        public static final String DEFAULT_SORT_ORDER = "TradingDay, UpdateTime asc";

        public static final String INSTRUMENT_ID = "InstrumentId";
        public static final String TRADING_DAY = "TradingDay";
        public static final String UPDATE_TIME = "UpdateTime";
        public static final String OPEN_PRICE = "OpenPrice";
        public static final String CLOSE_PRICE = "ClosePrice";
        public static final String HIGH_PRICE = "HighPrice";
        public static final String LOW_PRICE = "LowPrice";
        public static final String M20VALUE = "M20Value";
        public static final String TENDENCY = "Tendency";

        public static final String[] DEFAULT_QUERY_COLUMNS = {INSTRUMENT_ID, TRADING_DAY,
                UPDATE_TIME, OPEN_PRICE, CLOSE_PRICE, HIGH_PRICE, LOW_PRICE, M20VALUE, TENDENCY};
    }

    public static final class DayDataColumns implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/daydata");

        public static final String TABLE_NAME = "DayData";
        public static final String DEFAULT_SORT_ORDER = "TradingDay, UpdateTime asc";

        public static final String INSTRUMENT_ID = "InstrumentId";
        public static final String TRADING_DAY = "TradingDay";
        public static final String UPDATE_TIME = "UpdateTime";
        public static final String OPEN_PRICE = "OpenPrice";
        public static final String CLOSE_PRICE = "ClosePrice";
        public static final String HIGH_PRICE = "HighPrice";
        public static final String LOW_PRICE = "LowPrice";
        public static final String M20VALUE = "M20Value";
        public static final String TENDENCY = "Tendency";

        public static final String[] DEFAULT_QUERY_COLUMNS = {INSTRUMENT_ID, TRADING_DAY,
                UPDATE_TIME, OPEN_PRICE, CLOSE_PRICE, HIGH_PRICE, LOW_PRICE, M20VALUE, TENDENCY};
    }
}
