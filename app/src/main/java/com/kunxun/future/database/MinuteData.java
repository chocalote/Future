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

    public static class Macd {
        public double diff;
        public double dea;
        public double value;
    }
}
