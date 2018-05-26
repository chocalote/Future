package com.kunxun.future.database;


public class MinuteData {

    public String instrumentId;
    public String tradingDay;
    public String updateTime;
    public double openPrice;
    public double closePrice;
    public double highPrice;
    public double lowPrice;
    public Macd macd;
    public double targetPrice;

    public class Macd {
        public double diff;
        public double dea;
        public double value;
    }

    public MinuteData() {
        instrumentId = "";
        tradingDay = "";
        updateTime = "";
        openPrice = 0;
        closePrice = 0;
        highPrice = 0;
        lowPrice = 0;
        targetPrice = 0;
        macd.diff = 0;
        macd.dea = 0;
        macd.value = 0;
    }
}
