package com.kunxun.future.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kunxun.future.CTP.CTraderSpi;
import com.kunxun.future.CTP.ITraderSpiEvent;
import com.kunxun.future.R;
import com.kunxun.future.Utils.CodeUtils;
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

public class LoginFragment extends Fragment implements ITraderSpiEvent{

    static {
        System.loadLibrary("thosttraderapi");
        System.loadLibrary("thosttraderapi_wrap");
    }

    private static final String TAG = "Lily";

    private RadioGroup rgServer;
    private ImageView imgValidateCode;
    private EditText etPassword;

    private CThostFtdcTraderApi traderApi;
    private int iRequestId = 0;
    private  String FRONT_ADDRESS = "tcp://180.168.212.76:41205";
    private static final String BROKER_ID = "8000";
    private String userId, password, validateCode;

    private CodeUtils mCodeUtils;
    private Bitmap mBitmap;

    private int threadCount =0;

    // 会话参数
    private int	FRONT_ID;	//前置编号 TThostFtdcFrontIDType
    private int SESSION_ID;	//会话编号 TThostFtdcSessionIDType
	private String ORDER_REF;	//报单引用 TThostFtdcOrderRefType

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        initLayout(view);

        return view;
    }

    private void initLayout(View view){
        rgServer = view.findViewById(R.id.rgServer);
        if (rgServer.getCheckedRadioButtonId() == R.id.rbServer10010){
            FRONT_ADDRESS = "tcp://27.115.78.155:41205";
        }

        Button btnLogin = view.findViewById(R.id.btnLogin);
        imgValidateCode = view.findViewById(R.id.imgValidateCode);
        mCodeUtils = CodeUtils.getInstance();
        mBitmap = mCodeUtils.createBitmap();
        imgValidateCode.setImageBitmap(mBitmap);

        etPassword = view.findViewById(R.id.etPassword);
        final EditText etValidateCode = view.findViewById(R.id.etValidateCode);
        final AutoCompleteTextView actvUserId = view.findViewById(R.id.actvUserId);

        final CheckBox ckVisible = view.findViewById(R.id.ckVisible);

        View.OnClickListener mClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.imgValidateCode:
                        Toast.makeText(getContext(), "看不清楚，再来一次", Toast.LENGTH_SHORT).show();
                        mCodeUtils = CodeUtils.getInstance();
                        mBitmap = mCodeUtils.createBitmap();
                        imgValidateCode.setImageBitmap(mBitmap);
                        break;
                    case R.id.btnLogin:
                        userId = actvUserId.getText().toString().trim();
                        password = etPassword.getText().toString().trim();
                        validateCode = etValidateCode.getText().toString().trim();

                        if(validateEditText())
                        {
                            new Thread(mRunnable).start();
                        }

                        break;
                    case R.id.ckVisible:
                        if (ckVisible.isChecked()) {
                            etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        }
                        else {
                            etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        }
                        break;

                }
            }
        };

        btnLogin.setOnClickListener(mClickListener);
        imgValidateCode.setOnClickListener(mClickListener);
        ckVisible.setOnClickListener(mClickListener);

    }

    private boolean validateEditText() {
        boolean ret = false;
        if (userId == null || TextUtils.isEmpty(userId)) {
            Toast.makeText(getContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
            return ret;
        }
        if (password == null || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
            return ret;
        }

        if (validateCode == null || TextUtils.isEmpty(validateCode)) {
            Toast.makeText(getContext(), "验证码不能为空", Toast.LENGTH_SHORT).show();
            return ret;
        }

        if (!validateCode.equalsIgnoreCase(mCodeUtils.getCode())) {
            Toast.makeText(getContext(), "验证码错误，请重新输入", Toast.LENGTH_SHORT).show();
            return ret;
        }
        return true;
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, Thread.currentThread().getName()+" "+ threadCount);
            traderUserLogin();
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private void traderUserLogin(){

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            String file = getContext().getFilesDir().toString();

            CTraderSpi traderSpi = new CTraderSpi();
            traderSpi.setInterface(this);
            traderApi = CThostFtdcTraderApi.CreateFtdcTraderApi(file);
            traderApi.RegisterSpi(traderSpi);
            traderApi.RegisterFront(FRONT_ADDRESS);
            traderApi.SubscribePublicTopic(THOST_TE_RESUME_TYPE.THOST_TERT_RESTART);
            traderApi.SubscribePrivateTopic(THOST_TE_RESUME_TYPE.THOST_TERT_QUICK);
            traderApi.Init();
        }
    }

    @Override
    public void OnFrontConnected() {
        CThostFtdcReqUserLoginField loginField = new CThostFtdcReqUserLoginField();
        loginField.setBrokerID(BROKER_ID);
        loginField.setUserID(userId);
        loginField.setPassword(password);
        int iResult = traderApi.ReqUserLogin(loginField, ++iRequestId);
        Log.i(TAG, "--->发送用户登录请求: " + ((iResult == 0) ? "成功" : "失败"));
        if (iResult == 0) {
            mHandler.sendEmptyMessage(0x001);
        }
        else {
            mHandler.sendEmptyMessage(0x110);
        }
    }

    @Override
    public void OnRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {

        if (bIsLast && pRspInfo.getErrorID() == 0) {
            Log.i(TAG, "--->当前交易日:" + pRspUserLogin.getTradingDay());

            FRONT_ID = pRspUserLogin.getFrontID();
            SESSION_ID = pRspUserLogin.getSessionID();
            ORDER_REF = pRspUserLogin.getMaxOrderRef();


//            CThostFtdcSettlementInfoConfirmField pSettlementInfoConfirm = new CThostFtdcSettlementInfoConfirmField();
//            pSettlementInfoConfirm.setBrokerID(BROKER_ID);
//            pSettlementInfoConfirm.setInvestorID(userId);

//            String[] ins = {"rb1810"};
//            int iResult = mdApi.SubscribeMarketData(INSTRUMENTS, INSTRUMENTS.length);
//            Log.i(TAG, "--->>> 发送行情订阅请求: " + ((iResult == 0) ? "成功" : "失败"));
        }
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
}
