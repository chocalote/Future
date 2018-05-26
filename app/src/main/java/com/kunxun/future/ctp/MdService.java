package com.kunxun.future.ctp;

import android.app.Service;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.print.PrinterId;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.kunxun.future.database.DayData;
import com.kunxun.future.database.HourData;
import com.kunxun.future.database.MinuteData;
import com.kunxun.future.database.Minutes5Data;
import com.kunxun.future.database.Provider;
import com.kunxun.future.fragment.ContractFragment;
import com.sfit.ctp.thostmduserapi.CThostFtdcDepthMarketDataField;
import com.sfit.ctp.thostmduserapi.CThostFtdcForQuoteRspField;
import com.sfit.ctp.thostmduserapi.CThostFtdcMdApi;
import com.sfit.ctp.thostmduserapi.CThostFtdcReqUserLoginField;
import com.sfit.ctp.thostmduserapi.CThostFtdcRspInfoField;
import com.sfit.ctp.thostmduserapi.CThostFtdcRspUserLoginField;
import com.sfit.ctp.thostmduserapi.CThostFtdcSpecificInstrumentField;
import com.sfit.ctp.thostmduserapi.CThostFtdcUserLogoutField;

import java.util.ArrayList;
import java.util.List;


public class MdService extends Service implements IMdSpiEvent {

    static {
        System.loadLibrary("thostmduserapi");
        System.loadLibrary("thostmduserapi_wrap");
    }

    private static final String TAG = "Lily";

    private CThostFtdcMdApi mdApi;
    private static final String FRONT_ADDRESS = "tcp://180.168.146.187:10010";
    private static final String BROKER_ID = "9999";
    private static final String USER_ID = "021131";
    private static final String PASSWORD = "chen8885257";
    private static int iRequestId = 0;
    private String[] INSTRUMENTS;
    private MinuteData minuteData;
    private Minutes5Data minutes5Data;
    private HourData hourData;
    private DayData dayData;
    private List<List<Double>> closePriceList = new ArrayList<>();

    private int iPreMinute = 0;

    @Override
    public void onCreate() {
//        android.os.Debug.waitForDebugger();
        INSTRUMENTS = getInstruments();
        for (int i = 0; i < INSTRUMENTS.length; i++) {
            closePriceList.add(null);
        }
        initMdRequest();
        super.onCreate();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "服务成功被绑定");
        return new MdBinder();
    }

    public class MdBinder extends Binder {

        public void callChangeInstrument(String ins) {
            int iResult = mdApi.UnSubscribeMarketData(INSTRUMENTS, INSTRUMENTS.length);
            Log.i(TAG, "--->>> 发送行情取消订阅请求: " + ((iResult == 0) ? "成功" : "失败"));
            iResult = mdApi.SubscribeMarketData(new String[]{ins}, 1);
            Log.i(TAG, "--->>> 发送行情订阅请求: " + ((iResult == 0) ? "成功" : "失败"));
        }
    }


    private void initMdRequest() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            String file = getFilesDir().toString();
            mdApi = CThostFtdcMdApi.CreateFtdcMdApi(file);
            CMdSpi mdSpi = new CMdSpi();
            mdSpi.SetInterFace(this);
            mdApi.RegisterSpi(mdSpi);
            mdApi.RegisterFront(FRONT_ADDRESS);
            mdApi.Init();

        }
    }

    //region get parameters
    private String[] getInstruments() {
        String PREFERENCES = "future";
        SharedPreferences sp = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        int iCount = sp.getInt("InstrumentsCount", 1);
        String[] ins = new String[iCount];
        for (int i = 0; i < iCount; i++) {
            ins[i] = sp.getString("Instrument" + String.valueOf(i), "rb1810");
        }
        return ins;
    }

    private int getPosition(String ins) {
        int ret = 0;
        for (int i = 0; i < INSTRUMENTS.length; i++) {
            if (INSTRUMENTS[i].equals(ins)) {
                ret = i;
                break;
            }
        }
        return ret;
    }
    //endregion

    //region CTP operation
    @Override
    public void OnFrontConnected() {
        CThostFtdcReqUserLoginField loginField = new CThostFtdcReqUserLoginField();
        loginField.setBrokerID(BROKER_ID);
        loginField.setUserID(USER_ID);
        loginField.setPassword(PASSWORD);
        int iResult = mdApi.ReqUserLogin(loginField, ++iRequestId);
        Log.i(TAG, "--->发送用户登录请求: " + ((iResult == 0) ? "成功" : "失败"));
    }

    @Override
    public void OnFrontDisconnected(int nReason) {

    }

    @Override
    public void OnHeartBeatWarning(int nTimeLapse) {

    }

    @Override
    public void OnRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        if (bIsLast && pRspInfo.getErrorID() == 0) {
            Log.i(TAG, "--->当前交易日:" + pRspUserLogin.getTradingDay());


            int iResult = mdApi.SubscribeMarketData(INSTRUMENTS, INSTRUMENTS.length);
            Log.i(TAG, "--->>> 发送行情订阅请求: " + ((iResult == 0) ? "成功" : "失败"));
        }
    }

    @Override
    public void OnRspUserLogout(CThostFtdcUserLogoutField pUserLogout, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {

    }

    @Override
    public void OnRspError(CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {

    }

    @Override
    public void OnRspSubMarketData(CThostFtdcSpecificInstrumentField pSpecificInstrument, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {

    }

    @Override
    public void OnRspUnSubMarketData(CThostFtdcSpecificInstrumentField pSpecificInstrument, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {

    }

    @Override
    public void OnRspSubForQuoteRsp(CThostFtdcSpecificInstrumentField pSpecificInstrument, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {

    }

    @Override
    public void OnRspUnSubForQuoteRsp(CThostFtdcSpecificInstrumentField pSpecificInstrument, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {

    }

    @Override
    public void OnRtnDepthMarketData(CThostFtdcDepthMarketDataField pDepthMarketData) {
        Log.i(TAG, "--->>>" + pDepthMarketData.getUpdateTime() + ". " + pDepthMarketData.getUpdateMillisec() + ": "
                + pDepthMarketData.getInstrumentID() + " " + pDepthMarketData.getLastPrice());

        CDepthMarketData data = new CDepthMarketData(pDepthMarketData);

        Intent intent = new Intent();
        intent.setAction(ContractFragment.ACTION_UPDATE_UI);
        Bundle bundle = new Bundle();
        bundle.putSerializable("DepthMarketData", data);
        intent.putExtras(bundle);

        LocalBroadcastManager.getInstance(MdService.this).sendBroadcast(intent);

        saveData(data, getPosition(data.instrumentId));
    }

    @Override
    public void OnRtnForQuoteRsp(CThostFtdcForQuoteRspField pForQuoteRsp) {

    }
    //endregion


    private void saveData(CDepthMarketData data, int position) {

        saveMinuteData(data, position);
        saveMinutes5Data(data, position);
        saveHourData(data, position);
        saveDayData(data, position);
    }

    private void saveMinuteData(CDepthMarketData data, int position) {
        if (newMinuteData(data, position)) {

            ContentValues values = new ContentValues(11);
            values.put(Provider.MinuteDataColumns.INSTRUMENT_ID, minuteData.instrumentId);
            values.put(Provider.MinuteDataColumns.TRADING_DAY, minuteData.tradingDay);
            values.put(Provider.MinuteDataColumns.UPDATE_TIME, minuteData.updateTime);
            values.put(Provider.MinuteDataColumns.OPEN_PRICE, minuteData.openPrice);
            values.put(Provider.MinuteDataColumns.CLOSE_PRICE, minuteData.closePrice);
            values.put(Provider.MinuteDataColumns.HIGH_PRICE, minuteData.highPrice);
            values.put(Provider.MinuteDataColumns.LOW_PRICE, minuteData.lowPrice);
            values.put(Provider.MinuteDataColumns.MACD_DIFF, minuteData.macd.diff);
            values.put(Provider.MinuteDataColumns.MACD_DEA, minuteData.macd.dea);
            values.put(Provider.MinuteDataColumns.MACD_VALUE, minuteData.macd.value);
            values.put(Provider.MinuteDataColumns.TARGET_PRICE, minuteData.targetPrice);

            Uri uri = getContentResolver().insert(Provider.MinuteDataColumns.CONTENT_URI, values);
            int id = (int) ContentUris.parseId(uri);
            Log.i(TAG, "saveMinuteData: " + data.instrumentId + ", id = " + id);
        }

    }

    private void saveMinutes5Data(CDepthMarketData data, int position) {
        if (newMinutes5Data(data, position)) {

            ContentValues values = new ContentValues(9);
            values.put(Provider.Minutes5DataColumns.INSTRUMENT_ID, minutes5Data.instrumentId);
            values.put(Provider.Minutes5DataColumns.TRADING_DAY, minutes5Data.tradingDay);
            values.put(Provider.Minutes5DataColumns.UPDATE_TIME, minutes5Data.updateTime);
            values.put(Provider.Minutes5DataColumns.OPEN_PRICE, minutes5Data.openPrice);
            values.put(Provider.Minutes5DataColumns.CLOSE_PRICE, minutes5Data.closePrice);
            values.put(Provider.Minutes5DataColumns.HIGH_PRICE, minutes5Data.highPrice);
            values.put(Provider.Minutes5DataColumns.LOW_PRICE, minutes5Data.lowPrice);
            values.put(Provider.Minutes5DataColumns.M20VALUE, minutes5Data.m20Value);
            values.put(Provider.Minutes5DataColumns.TENDENCY, minutes5Data.tendency);

            Uri uri = getContentResolver().insert(Provider.Minutes5DataColumns.CONTENT_URI, values);
            int id = (int) ContentUris.parseId(uri);
            Log.i(TAG, "saveMinutes5Data: " + data.instrumentId + ", id = " + id);
        }
    }

    private void saveHourData(CDepthMarketData data, int position) {
        if (newHourData(data, position)) {

            ContentValues values = new ContentValues(9);
            values.put(Provider.HourDataColumns.INSTRUMENT_ID, hourData.instrumentId);
            values.put(Provider.HourDataColumns.TRADING_DAY, hourData.tradingDay);
            values.put(Provider.HourDataColumns.UPDATE_TIME, hourData.updateTime);
            values.put(Provider.HourDataColumns.OPEN_PRICE, hourData.openPrice);
            values.put(Provider.HourDataColumns.CLOSE_PRICE, hourData.closePrice);
            values.put(Provider.HourDataColumns.HIGH_PRICE, hourData.highPrice);
            values.put(Provider.HourDataColumns.LOW_PRICE, hourData.lowPrice);
            values.put(Provider.HourDataColumns.M20VALUE, hourData.m20Value);
            values.put(Provider.HourDataColumns.TENDENCY, hourData.tendency);

            Uri uri = getContentResolver().insert(Provider.HourDataColumns.CONTENT_URI, values);
            int id = (int) ContentUris.parseId(uri);
            Log.i(TAG, "saveHourData: " + data.instrumentId + ", id = " + id);
        }
    }

    private void saveDayData(CDepthMarketData data, int position) {
        if (newDayData(data, position)) {

            ContentValues values = new ContentValues(9);
            values.put(Provider.DayDataColumns.INSTRUMENT_ID, dayData.instrumentId);
            values.put(Provider.DayDataColumns.TRADING_DAY, dayData.tradingDay);
            values.put(Provider.DayDataColumns.UPDATE_TIME, dayData.updateTime);
            values.put(Provider.DayDataColumns.OPEN_PRICE, dayData.openPrice);
            values.put(Provider.DayDataColumns.CLOSE_PRICE, dayData.closePrice);
            values.put(Provider.DayDataColumns.HIGH_PRICE, dayData.highPrice);
            values.put(Provider.DayDataColumns.LOW_PRICE, dayData.lowPrice);
            values.put(Provider.DayDataColumns.M20VALUE, dayData.m20Value);
            values.put(Provider.DayDataColumns.TENDENCY, dayData.tendency);

            Uri uri = getContentResolver().insert(Provider.DayDataColumns.CONTENT_URI, values);
            int id = (int) ContentUris.parseId(uri);
            Log.i(TAG, "saveDayData: " + data.instrumentId + ", id = " + id);
        }
    }

    private boolean newMinuteData(CDepthMarketData data, int position) {
        boolean isFinish = false;

        if (isFirstDataInMinute(data, 1)) {
            minuteData = new MinuteData();
            minuteData.instrumentId = data.instrumentId;
            minuteData.openPrice = data.lastPrice;
            minuteData.tradingDay = data.tradingDay;
            minuteData.updateTime = data.updateTime;
            minuteData.highPrice = data.lastPrice;
            minuteData.lowPrice = data.lastPrice;
        } else if (isLastDataInMinute(data, 1)) {
            minuteData.closePrice = data.lastPrice;
            closePriceList.get(position).add(data.lastPrice);
            if (closePriceList.get(position).size() >= 32)
                closePriceList.get(position).remove(0);
            minuteData.macd = macdCalculate(data.lastPrice, closePriceList.get(position).size(), position);
            minuteData.targetPrice = targetPriceCalculate(minuteData.macd, data.lastPrice, position);
            isFinish = true;
        } else {

            if (minuteData == null)
                return false;

            minuteData.highPrice = Math.max(minuteData.highPrice, data.lastPrice);
            minuteData.lowPrice = Math.min(minuteData.lowPrice, data.lastPrice);
        }

        return isFinish;
    }

    private boolean newMinutes5Data(CDepthMarketData data, int position) {
        boolean isFinish = false;

        if (isFirstDataInMinute(data, 5)) {
            minutes5Data = new Minutes5Data();
            minutes5Data.instrumentId = data.instrumentId;
            minutes5Data.openPrice = data.lastPrice;
            minutes5Data.tradingDay = data.tradingDay;
            minutes5Data.updateTime = data.updateTime;
            minutes5Data.highPrice = data.lastPrice;
            minutes5Data.lowPrice = data.lastPrice;
        } else if (isLastDataInMinute(data, 5)) {
            minutes5Data.closePrice = data.lastPrice;
            closePriceList.get(position).add(data.lastPrice);
            if (closePriceList.get(position).size() >= 32)
                closePriceList.get(position).remove(0);
//            minutes5Data.macd = macdCalculate(data.lastPrice, closePriceList.get(position).size(),position);
//            minuteData.targetPrice = targetPriceCalculate(minuteData.macd, data.lastPrice, position);
            isFinish = true;
        } else {

            if (minutes5Data == null)
                return false;

            minutes5Data.highPrice = Math.max(minutes5Data.highPrice, data.lastPrice);
            minutes5Data.lowPrice = Math.min(minutes5Data.lowPrice, data.lastPrice);
        }

        return isFinish;
    }

    private boolean newHourData(CDepthMarketData data, int position) {
        boolean isFinish = false;

        if (isFirstDataInMinute(data, 60)) {
            hourData = new HourData();
            hourData.instrumentId = data.instrumentId;
            hourData.openPrice = data.lastPrice;
            hourData.tradingDay = data.tradingDay;
            hourData.updateTime = data.updateTime;
            hourData.highPrice = data.lastPrice;
            hourData.lowPrice = data.lastPrice;
        } else if (isLastDataInMinute(data, 60)) {
            hourData.closePrice = data.lastPrice;
            closePriceList.get(position).add(data.lastPrice);
            if (closePriceList.get(position).size() >= 32)
                closePriceList.get(position).remove(0);
//            minutes5Data.macd = macdCalculate(data.lastPrice, closePriceList.get(position).size(),position);
//            minuteData.targetPrice = targetPriceCalculate(minuteData.macd, data.lastPrice, position);
            isFinish = true;
        } else {

            if (hourData == null)
                return false;

            hourData.highPrice = Math.max(hourData.highPrice, data.lastPrice);
            hourData.lowPrice = Math.min(hourData.lowPrice, data.lastPrice);
        }

        return isFinish;
    }

    private boolean newDayData(CDepthMarketData data, int position) {
        boolean isFinish = false;

        if (isMarketOpen(data.iUpdateMilliSec)) {
            dayData = new DayData();
            dayData.instrumentId = data.instrumentId;
            dayData.openPrice = data.lastPrice;
            dayData.tradingDay = data.tradingDay;
            dayData.updateTime = data.updateTime;
            dayData.highPrice = data.lastPrice;
            dayData.lowPrice = data.lastPrice;
        } else {
            dayData.closePrice = data.lastPrice;
            dayData.highPrice = data.highestPrice;
            dayData.lowPrice = data.lowestPrice;

//            closePriceList.get(position).add(data.lastPrice);
//            if (closePriceList.get(position).size() >= 32)
//                closePriceList.get(position).remove(0);
//            minutes5Data.macd = macdCalculate(data.lastPrice, closePriceList.get(position).size(),position);
//            minuteData.targetPrice = targetPriceCalculate(minuteData.macd, data.lastPrice, position);
            isFinish = true;
        }

        return isFinish;
    }


    //region MACD calculate
    private MinuteData.Macd macdCalculate(double price, int cnt, int position) {

        MinuteData.Macd ret = null;
        double[] arrClosePrice = new double[32];
        double[] diffLine = new double[9];

        for (int i = 0; i < cnt - 1; i++) {
            if (i >= cnt - 32) {
                arrClosePrice[i - (cnt - 32)] = closePriceList.get(position).get(i);
            }
        }
        arrClosePrice[31] = price;
        assert ret != null;
        ret.diff = EMA(arrClosePrice, 32, 10) - EMA(arrClosePrice, 32, 22);
        diffLine[8] = ret.diff;
        for (int i = 0; i < 8; i++) {
            diffLine[7 - i] = EMA(arrClosePrice, 31 - i, 10) - EMA(arrClosePrice, 31 - i, 22);
        }

        ret.dea = EMA(diffLine, 9, 9);
        ret.value = 2 * (ret.diff - ret.dea);
        return ret;
    }
    //endregion

    //region Target Price Calculate
    private double targetPriceCalculate(MinuteData.Macd macd, double price, int position) {
        double ret;

        if (macd.diff > macd.dea) {
            while (macd.diff > macd.dea) {
                price = price - 1;
                macd = macdCalculate(price, closePriceList.get(position).size() + 1, position);
            }
        } else {
            while (macd.diff < macd.dea) {
                price = price + 1;
                macd = macdCalculate(price, closePriceList.get(position).size() + 1, position);
            }
        }
        ret = price;
        return ret;
    }
    //endregion

    //region Exponential Moving Average
    private double EMA(double[] src, int cnt, int num) {
        double y = 0;
        int sum;
        sum = num * (num + 1) / 2;

        while (num > 0 && cnt > 1) {
            y = y + num * src[cnt - 1] / sum;
            cnt--;
            num--;
        }
        return y;
    }
    //endregion

    //region Time Condition
    private boolean isFirstDataInMinute(CDepthMarketData data, int k) {
        boolean isNewMinute = false;

        int millieInXMinute = data.iUpdateMilliSec % (k * 60000);
        int iTime = data.iUpdateTime / 60000;
        int iTimeRoundDown = iTime - iTime % k;

        if ((millieInXMinute < 500) || isMarketOpen(data.iUpdateTime) ||
                ((data.iUpdateTime / 60000 - iTimeRoundDown) >= k))
            isNewMinute = true;

        return isNewMinute;
    }

    private boolean isLastDataInMinute(CDepthMarketData data, int k) {
        return (data.iUpdateMilliSec % (k * 60000)) >= ((k - 1) * 60000 + 59500);
    }

    private boolean isMarketOpen(int iTime) {
//        iTime /= 1000;
        return (iTime == 9 * 60 * 60 * 1000);
//                || (iTime == (13 * 60 + 30) * 60) || (iTime == 21 * 60 * 60);
    }

    private boolean isMarketClose(int iTime) {
        return (iTime >= (14 * 60 + 59) * 60 * 1000) && (iTime <= 15 * 60 * 60 * 1000);
    }
    //endregion

}
