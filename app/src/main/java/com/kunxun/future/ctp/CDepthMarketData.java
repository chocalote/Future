package com.kunxun.future.ctp;

import com.sfit.ctp.thostmduserapi.CThostFtdcDepthMarketDataField;

import java.io.Serializable;

public class CDepthMarketData implements Serializable {

    public String instrumentId;
    public double lastPrice;
    public double openPrice;
    public int volume;

    public double bidPrice1;
    public int bidVolume1;
    public double askPrice1;
    public int askVolume1;
    public double openInterest;
    public double interestChange;

    public double highestPrice;
    public double lowestPrice;

    public String tradingDay;
    public String updateTime;
    public int iUpdateTime;
    public int iUpdateMilliSec;


    public CDepthMarketData(CThostFtdcDepthMarketDataField dataField) {

        instrumentId = dataField.getInstrumentID();
        lastPrice = dataField.getLastPrice();
        openPrice = dataField.getOpenPrice();
        volume = dataField.getVolume();

        bidPrice1 = dataField.getBidPrice1();
        bidVolume1 = dataField.getBidVolume1();

        askPrice1 = dataField.getAskPrice1();
        askVolume1 = dataField.getAskVolume1();

        openInterest = dataField.getOpenInterest();
        interestChange = dataField.getOpenInterest() - dataField.getPreOpenInterest();

        highestPrice = dataField.getHighestPrice();
        lowestPrice = dataField.getLowestPrice();

        tradingDay = dataField.getTradingDay();
        updateTime = dataField.getUpdateTime();

        iUpdateTime = Integer.parseInt(updateTime.substring(0, 1)) * 3600
                + Integer.parseInt(updateTime.substring(3, 4)) * 60
                + Integer.parseInt(updateTime.substring(6, 7));
        iUpdateMilliSec = dataField.getUpdateMillisec() + 1000 * iUpdateTime;

    }
}
