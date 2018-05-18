package com.kunxun.future.CTP;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import com.kunxun.future.fragment.ContractFragment;
import com.sfit.ctp.thostmduserapi.CThostFtdcDepthMarketDataField;
import com.sfit.ctp.thostmduserapi.CThostFtdcForQuoteRspField;
import com.sfit.ctp.thostmduserapi.CThostFtdcMdApi;
import com.sfit.ctp.thostmduserapi.CThostFtdcReqUserLoginField;
import com.sfit.ctp.thostmduserapi.CThostFtdcRspInfoField;
import com.sfit.ctp.thostmduserapi.CThostFtdcRspUserLoginField;
import com.sfit.ctp.thostmduserapi.CThostFtdcSpecificInstrumentField;
import com.sfit.ctp.thostmduserapi.CThostFtdcUserLogoutField;


public class MdService extends Service implements IMdSpiEvent{

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
    private String [] INSTRUMENTS ;

    @Override
    public void onCreate() {
        android.os.Debug.waitForDebugger();
        initMdRequest();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    private void initMdRequest()
    {
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
    private String[] getInstruments(){
        String PREFERENCES = "future";
        SharedPreferences sp = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        int iCount = sp.getInt("InstrumentsCount",1);
        String [] ins= new String[iCount];
        for(int i=0; i<iCount; i++)
        {
            ins[i] = sp.getString("Instrument"+String.valueOf(i), "rb1810");
        }
        return ins;
    }

    private int getPosition(String ins){
        int ret = 0;
        for (int i=0;i<INSTRUMENTS.length;i++)
        {
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

            INSTRUMENTS = getInstruments();
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
//        Log.i(TAG, "--->>>" + pDepthMarketData.getUpdateTime() + ": " + pDepthMarketData.getInstrumentID() + " " + pDepthMarketData.getLastPrice());

        Intent intent = new Intent();
        intent.setAction(ContractFragment.ACTION_UPDATE_UI);

//        intent.putExtra("position", getPosition(pDepthMarketData.getInstrumentID()));
//        intent.putExtra("last_price", pDepthMarketData.getLastPrice());
//        intent.putExtra("change", pDepthMarketData.getLastPrice() - pDepthMarketData.getOpenPrice());

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    @Override
    public void OnRtnForQuoteRsp(CThostFtdcForQuoteRspField pForQuoteRsp) {

    }
    //endregion


}
