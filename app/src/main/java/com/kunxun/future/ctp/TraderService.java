package com.kunxun.future.ctp;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.kunxun.future.fragment.LoginFragment;
import com.sfit.ctp.thosttraderapi.CThostFtdcInputOrderActionField;
import com.sfit.ctp.thosttraderapi.CThostFtdcInputOrderField;
import com.sfit.ctp.thosttraderapi.CThostFtdcInstrumentCommissionRateField;
import com.sfit.ctp.thosttraderapi.CThostFtdcInstrumentField;
import com.sfit.ctp.thosttraderapi.CThostFtdcInstrumentMarginRateField;
import com.sfit.ctp.thosttraderapi.CThostFtdcInvestorPositionField;
import com.sfit.ctp.thosttraderapi.CThostFtdcOrderField;
import com.sfit.ctp.thosttraderapi.CThostFtdcReqUserLoginField;
import com.sfit.ctp.thosttraderapi.CThostFtdcRspInfoField;
import com.sfit.ctp.thosttraderapi.CThostFtdcRspUserLoginField;
import com.sfit.ctp.thosttraderapi.CThostFtdcSettlementInfoConfirmField;
import com.sfit.ctp.thosttraderapi.CThostFtdcTradeField;
import com.sfit.ctp.thosttraderapi.CThostFtdcTraderApi;
import com.sfit.ctp.thosttraderapi.CThostFtdcTradingAccountField;
import com.sfit.ctp.thosttraderapi.THOST_TE_RESUME_TYPE;

public class TraderService extends Service implements ITraderSpiEvent{

    static {
        System.loadLibrary("thosttraderapi");
        System.loadLibrary("thosttraderapi_wrap");
    }
    private static final String TAG = "Lily";

    private CThostFtdcTraderApi traderApi;
    private int iRequestId = 0;
    private  String FRONT_ADDRESS, USER_ID, PASSWORD;
    private static final String BROKER_ID = "8000";

    // 会话参数
//    private int	FRONT_ID;	//前置编号 TThostFtdcFrontIDType
//    private int SESSION_ID;	//会话编号 TThostFtdcSessionIDType
//    private String ORDER_REF;	//报单引用 TThostFtdcOrderRefType

    @Override
    public void onCreate() {
//        android.os.Debug.waitForDebugger();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        FRONT_ADDRESS = intent.getStringExtra("front_address");
        USER_ID = intent.getStringExtra("user_id");
        PASSWORD = intent.getStringExtra("password");

        Log.i(TAG,"onStart");
        initTraderRequest();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initTraderRequest(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            String file = getFilesDir().toString();

            CTraderSpi traderSpi = new CTraderSpi();
            traderSpi.setInterface(this);
            traderApi = CThostFtdcTraderApi.CreateFtdcTraderApi(file);
            traderApi.RegisterSpi(traderSpi);
            traderApi.RegisterFront(FRONT_ADDRESS);
            traderApi.SubscribePublicTopic(THOST_TE_RESUME_TYPE.THOST_TERT_QUICK);
            traderApi.SubscribePrivateTopic(THOST_TE_RESUME_TYPE.THOST_TERT_QUICK);
            traderApi.Init();
        }
    }

    //region CTP operation
    @Override
    public void OnFrontConnected() {
        CThostFtdcReqUserLoginField loginField = new CThostFtdcReqUserLoginField();
        loginField.setBrokerID(BROKER_ID);
        loginField.setUserID(USER_ID);
        loginField.setPassword(PASSWORD);
        int iResult = traderApi.ReqUserLogin(loginField, ++iRequestId);
        Log.i(TAG, "--->发送用户登录请求: " + ((iResult == 0) ? "成功" : "失败"));
        if(iResult == 0)
        {
            Intent intent = new Intent();
            intent.setAction(LoginFragment.ACTION_LOGIN);
            intent.putExtra("login_flag", iResult);
            LocalBroadcastManager.getInstance(TraderService.this).sendBroadcast(intent);

        }
    }

    @Override
    public void OnRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {

    }

    @Override
    public void OnRspSettlementInfoConfirm(CThostFtdcSettlementInfoConfirmField pSettlementInfoConfirm, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {

    }

    @Override
    public void OnRspQryInstrument(CThostFtdcInstrumentField pInstrument, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {

    }

    @Override
    public void OnRspQryTradingAccount(CThostFtdcTradingAccountField pTradingAccount, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {

    }

    @Override
    public void OnRspQryInvestorPosition(CThostFtdcInvestorPositionField pInvestorPosition, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {

    }

    @Override
    public void OnRspOrderInsert(CThostFtdcInputOrderField pInputOrder, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {

    }

    @Override
    public void OnRspOrderAction(CThostFtdcInputOrderActionField pInputOrderAction, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {

    }

    @Override
    public void OnRspError(CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {

    }

    @Override
    public void OnFrontDisconnected(int nReason) {

    }

    @Override
    public void OnHeartBeatWarning(int nTimeLapse) {

    }

    @Override
    public void OnRtnOrder(CThostFtdcOrderField pOrder) {

    }

    @Override
    public void OnRtnTrade(CThostFtdcTradeField pTrade) {

    }

    @Override
    public void OnRspQryInstrumentMarginRate(CThostFtdcInstrumentMarginRateField pInstrumentMarginRate, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {

    }

    @Override
    public void OnRspQryInstrumentCommissionRate(CThostFtdcInstrumentCommissionRateField pInstrumentCommissionRate, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {

    }
    //endregion
}
