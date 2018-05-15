package com.kunxun.future.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kunxun.future.CTP.CMdSpi;
import com.kunxun.future.CTP.IMdSpiEvent;
import com.kunxun.future.R;
import com.kunxun.future.adapter.CommonAdapter;
import com.kunxun.future.adapter.CommonViewHolder;
import com.sfit.ctp.thostmduserapi.CThostFtdcDepthMarketDataField;
import com.sfit.ctp.thostmduserapi.CThostFtdcForQuoteRspField;
import com.sfit.ctp.thostmduserapi.CThostFtdcMdApi;
import com.sfit.ctp.thostmduserapi.CThostFtdcReqUserLoginField;
import com.sfit.ctp.thostmduserapi.CThostFtdcRspInfoField;
import com.sfit.ctp.thostmduserapi.CThostFtdcRspUserLoginField;
import com.sfit.ctp.thostmduserapi.CThostFtdcSpecificInstrumentField;
import com.sfit.ctp.thostmduserapi.CThostFtdcUserLogoutField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContractFragment extends Fragment implements IMdSpiEvent {

    static {
        System.loadLibrary("thostmduserapi");
        System.loadLibrary("thostmduserapi_wrap");
    }

    private static final String TAG = "Lily";
    public static CThostFtdcMdApi mdApi;
    private int iRequestId = 0;
    private static final String FRONT_ADDRESS = "tcp://180.168.146.187:10010";
    private static final String BROKER_ID = "9999";
    private static final String USER_ID = "021131";
    private static final String PASSWORD = "chen8885257";

    private String[] INSTRUMENTS = {"rb1810", "cu1807", "sc1809"};
    private String[] INSTRUMENT_NAMES = {"螺纹1810", "沪铜1807", "原油1809"};
    private List<HashMap<String, String>> listData = new ArrayList<>();

    private ListView mListView;
    private CommonAdapter commonAdapter;
    private TextView tv_Edit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contract, container, false);

        initLayout(view);

        new Thread(mRunnable).start();

        return view;
    }


    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, Thread.currentThread().getName());
            mdRequest();
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x001:
                    int position = (int) msg.obj;
                    updateListViewItem(position);
                    break;

            }
        }
    };

    //region mdRequest
    private void mdRequest() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            String file = getContext().getCacheDir().toString();

            CMdSpi mdSpi = new CMdSpi();
            mdSpi.setInterface(this);

            mdApi = CThostFtdcMdApi.CreateFtdcMdApi(file);
            mdApi.RegisterSpi(mdSpi);
            mdApi.RegisterFront(FRONT_ADDRESS);
            mdApi.Init();
        }
    }
    //endregion

    //region layoutOperation
    private void initLayout(View view) {

        mListView = view.findViewById(R.id.mContractList);

        for (int i = 0; i < INSTRUMENTS.length; i++) {
            HashMap<String, String> item = new HashMap<>();
            item.put("Name", INSTRUMENTS[i]);
            item.put("SubName", INSTRUMENT_NAMES[i]);
            item.put("LatestPrice", "--");
            item.put("Change", "--");

            listData.add(item);
        }

        commonAdapter = new CommonAdapter<HashMap<String, String>>(getContext(), listData, R.layout.listitem_contract) {
            @Override
            protected void convertView(View item, HashMap<String, String> map) {

                float change = 0;
                if (!map.get("Change").equals("--")) {
                    change = Float.parseFloat(map.get("Change"));
                }

                TextView tvName = CommonViewHolder.get(item, R.id.contractName);
                String tmp = "  " + map.get("Name");
                tvName.setText(tmp);

                TextView tvSubName = CommonViewHolder.get(item, R.id.contractSubName);
                tvSubName.setText(map.get("SubName"));

                TextView tvLatestPrice = CommonViewHolder.get(item, R.id.contractLatestPrice);
                tvLatestPrice.setText(map.get("LatestPrice"));

                TextView tvChange = CommonViewHolder.get(item, R.id.contractChange);
                tvChange.setText(map.get("Change"));

                if (change < 0) {
                    tvLatestPrice.setTextColor(getResources().getColor(R.color.colorAccent));
                    tvChange.setTextColor(getResources().getColor(R.color.colorAccent));

                }
                if (change > 0) {
                    tvLatestPrice.setTextColor(getResources().getColor(R.color.colorRed));
                    tvChange.setTextColor(getResources().getColor(R.color.colorRed));
                }
            }
        };

        mListView.setAdapter(commonAdapter);

        tv_Edit = view.findViewById(R.id.tvEdit);
        tv_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Coming soon...", Toast.LENGTH_LONG).show();
            }
        });

        ImageButton imgbtn_Search = view.findViewById(R.id.imgbtnSearch);
        imgbtn_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Coming soon...", Toast.LENGTH_LONG).show();
            }
        });

//        SwipeRefreshLayout mSwipeLayout = view.findViewById(R.id.mSwipeRefreshLayout);
//        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);
//
//        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                Toast.makeText(getContext(), "Refresh", Toast.LENGTH_LONG).show();
//            }
//        });
    }

    private void updateListViewItem(int position) {

        int firstVisiblePosition = mListView.getFirstVisiblePosition();
        int lastVisiblePosition = mListView.getLastVisiblePosition();

        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
            View view = mListView.getChildAt(position - firstVisiblePosition);
            commonAdapter.getView(position, view, mListView);
        }
    }
    //endregion

    //region CTP operation
    public void OnFrontConnected() {
        CThostFtdcReqUserLoginField loginField = new CThostFtdcReqUserLoginField();
        loginField.setBrokerID(BROKER_ID);
        loginField.setUserID(USER_ID);
        loginField.setPassword(PASSWORD);
        int iResult = mdApi.ReqUserLogin(loginField, ++iRequestId);
        Log.i(TAG, "--->发送用户登录请求: " + ((iResult == 0) ? "成功" : "失败"));
    }

    public void OnFrontDisconnected(int nReason) {

    }

    public void OnHeartBeatWarning(int nTimeLapse) {

    }


    public void OnRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin,
                               CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        if (bIsLast && pRspInfo.getErrorID() == 0) {
            Log.i(TAG, "--->当前交易日:" + pRspUserLogin.getTradingDay());

//            String[] ins = {"rb1810"};
            int iResult = mdApi.SubscribeMarketData(INSTRUMENTS, INSTRUMENTS.length);
            Log.i(TAG, "--->>> 发送行情订阅请求: " + ((iResult == 0) ? "成功" : "失败"));
        }
    }

    public void OnRspUserLogout(CThostFtdcUserLogoutField pUserLogout,
                                CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {

    }

    public void OnRspError(CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {

    }

    public void OnRspSubMarketData(CThostFtdcSpecificInstrumentField pSpecificInstrument,
                                   CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {

    }

    public void OnRspUnSubMarketData(CThostFtdcSpecificInstrumentField pSpecificInstrument,
                                     CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {

    }

    public void OnRspSubForQuoteRsp(CThostFtdcSpecificInstrumentField pSpecificInstrument,
                                    CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {

    }

    public void OnRspUnSubForQuoteRsp(CThostFtdcSpecificInstrumentField pSpecificInstrument,
                                      CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {

    }

    public void OnRtnDepthMarketData(CThostFtdcDepthMarketDataField pDepthMarketData) {
        Log.i(TAG,pDepthMarketData.getUpdateTime()+": "+pDepthMarketData.getInstrumentID()+ ", "+ pDepthMarketData.getLastPrice());

        int position;
        for (int i = 0; i < listData.size(); i++) {
            if (listData.get(i).get("Name").equals(pDepthMarketData.getInstrumentID())) {
                position = i;
                HashMap<String, String> item = listData.get(i);
                if (!item.get("LatestPrice").equals(String.valueOf(pDepthMarketData.getLastPrice()))) {
                    item.remove("LatestPrice");
                    item.put("LatestPrice", String.valueOf(pDepthMarketData.getLastPrice()));
                    item.remove("Change");
                    double change = pDepthMarketData.getLastPrice() - pDepthMarketData.getOpenPrice();
                    change = (Math.round(change*10))/10;
                    item.put("Change", String.valueOf(change));
                    listData.set(position, item);
                    Message msg = Message.obtain();
                    msg.what = 0x001;
                    msg.obj = position;
                    mHandler.sendMessage(msg);
                }
                break;
            }
        }
    }

    public void OnRtnForQuoteRsp(CThostFtdcForQuoteRspField pForQuoteRsp) {

    }
    //endregion

}
