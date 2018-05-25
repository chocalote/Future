package com.kunxun.future.ctp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private List<Double> closePriceList = new ArrayList<>();

    private int iPreMinute = 0;

    @Override
    public void onCreate() {
//        android.os.Debug.waitForDebugger();
        INSTRUMENTS = getInstruments();
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

//        saveData(data);
    }

    @Override
    public void OnRtnForQuoteRsp(CThostFtdcForQuoteRspField pForQuoteRsp) {

    }
    //endregion


    private void saveData(CDepthMarketData data) {

        saveMinuteData(data);

    }

    private void saveMinuteData(CDepthMarketData data)
    {
        //1 minute
        if (isFirstDataInMinute(data, 1)) {
            minuteData = new MinuteData();
            minuteData.instrumentId = data.instrumentId;
            minuteData.openPrice = data.lastPrice;
            minuteData.tradingDay = data.tradingDay;
            minuteData.updateTime = data.updateTime;
            minuteData.highPrice = data.lastPrice;
            minuteData.lowPrice = data.lastPrice;
        }
        else if (isLastDataInMinute(data, 1)){
            minuteData.closePrice = data.lastPrice;
        }
        else {
            minuteData.highPrice = Math.max(minuteData.highPrice, data.lastPrice);
            minuteData.lowPrice = Math.min(minuteData.lowPrice, data.lastPrice);
        }
    }


//    private MinuteData.Macd macdCalute(double price, int cnt){
//
//    }

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
        iTime /= 1000;
        return (iTime == 9 * 60 * 60) || (iTime == (13 * 60 + 30) * 60) || (iTime == 21 * 60 * 60);
    }


}
