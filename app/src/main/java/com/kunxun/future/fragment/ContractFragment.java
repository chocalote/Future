package com.kunxun.future.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.kunxun.future.CTP.CMdSpi;
import com.kunxun.future.CTP.IMdSpiEvent;
import com.kunxun.future.R;
import com.sfit.ctp.thostmduserapi.CThostFtdcDepthMarketDataField;
import com.sfit.ctp.thostmduserapi.CThostFtdcForQuoteRspField;
import com.sfit.ctp.thostmduserapi.CThostFtdcMdApi;
import com.sfit.ctp.thostmduserapi.CThostFtdcMdSpi;
import com.sfit.ctp.thostmduserapi.CThostFtdcReqUserLoginField;
import com.sfit.ctp.thostmduserapi.CThostFtdcRspInfoField;
import com.sfit.ctp.thostmduserapi.CThostFtdcRspUserLoginField;
import com.sfit.ctp.thostmduserapi.CThostFtdcSpecificInstrumentField;
import com.sfit.ctp.thostmduserapi.CThostFtdcUserLogoutField;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContractFragment extends Fragment implements IMdSpiEvent {

    static
    {
        System.loadLibrary("thostmduserapi");
        System.loadLibrary("thostmduserapi_wrap");
    }

    public static CThostFtdcMdApi mdApi;
    private int iRequestId = 0;
    private static final String FRONT_ADDRESS ="tcp://180.168.146.187:10010";
    private static final String BROKER_ID = "9999";
    private static final String USER_ID = "021131";
    private static final String PASSWORD = "chen8885257";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contract, container, false);

        mdRequest();
        //updateLayout(view);

        return view;
    }


    private void mdRequest(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            String file = getContext().getCacheDir().toString();

            CMdSpi mdSpi = new CMdSpi();
            mdSpi.setInterface(this);

            mdApi = CThostFtdcMdApi.CreateFtdcMdApi(file);
            mdApi.RegisterSpi(mdSpi);
            mdApi.RegisterFront(FRONT_ADDRESS);
            mdApi.Init();
//            mdApi.Join();
        }
    }

    private void updateLayout(View view) {
        List<HashMap<String, String>> listData = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            HashMap<String, String> item = new HashMap<>();
            item.put("Name", "RS180" + String.valueOf(i));
            item.put("LatestPrice", "201" + String.valueOf(i));
            item.put("Change", "2" + String.valueOf(i));

            listData.add(item);
        }
        ListView mListView = view.findViewById(R.id.mContractList);
        SimpleAdapter listViewAdapter = new SimpleAdapter(getActivity(), listData,
                R.layout.listitem_contract,
                new String[]{"Name", "LatestPrice", "Change"},
                new int[]{R.id.contractName, R.id.contractLatestPrice, R.id.contractChange});


        mListView.setAdapter(listViewAdapter);

        SwipeRefreshLayout mSwipeLayout = view.findViewById(R.id.mSwipeRefreshLayout);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getContext(), "Refresh", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void OnFrontConnected(){
        CThostFtdcReqUserLoginField loginField = new CThostFtdcReqUserLoginField();
        loginField.setBrokerID(BROKER_ID);
        loginField.setUserID(USER_ID);
        loginField.setPassword(PASSWORD);
        int iResult = mdApi.ReqUserLogin(loginField,++iRequestId);
        Log.i("Lily", "--->发送用户登录请求: " + ((iResult == 0) ? "成功" : "失败"));
    }

    public void OnFrontDisconnected(int nReason){

    }

    public  void OnHeartBeatWarning(int nTimeLapse){

    }


    public void OnRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin,
                                        CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast){
        if (bIsLast && pRspInfo.getErrorID() ==0){
            Log.i("Lily","--->当前交易日:" + pRspUserLogin.getTradingDay());

            String []  ins = {"rb1810"};
            int iResult = mdApi.SubscribeMarketData(ins,ins.length);
            Log.i("Lily", "--->>> 发送行情订阅请求: "+((iResult == 0) ? "成功" : "失败"));
        }
    }

    public void OnRspUserLogout(CThostFtdcUserLogoutField pUserLogout,
                                         CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast){

    }

    public void OnRspError(CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast){

    }

    public  void OnRspSubMarketData(CThostFtdcSpecificInstrumentField pSpecificInstrument,
                                            CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast){

    }

    public  void OnRspUnSubMarketData(CThostFtdcSpecificInstrumentField pSpecificInstrument,
                                              CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast){

    }

    public  void OnRspSubForQuoteRsp(CThostFtdcSpecificInstrumentField pSpecificInstrument,
                                             CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast){

    }
    public  void OnRspUnSubForQuoteRsp(CThostFtdcSpecificInstrumentField pSpecificInstrument,
                                               CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast){

    }

    public  void OnRtnDepthMarketData(CThostFtdcDepthMarketDataField pDepthMarketData){
        Log.i("Lily",pDepthMarketData.getUpdateTime()+": "+pDepthMarketData.getInstrumentID()+ ", "+ pDepthMarketData.getLastPrice());
    }

    public  void OnRtnForQuoteRsp(CThostFtdcForQuoteRspField pForQuoteRsp){

    }

}
