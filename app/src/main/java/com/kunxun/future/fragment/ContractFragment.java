package com.kunxun.future.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrinterId;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

    static {
        System.loadLibrary("thostmduserapi");
        System.loadLibrary("thostmduserapi_wrap");
    }

    public static CThostFtdcMdApi mdApi;
    private int iRequestId = 0;
    private static final String FRONT_ADDRESS = "tcp://180.168.146.187:10010";
    private static final String BROKER_ID = "9999";
    private static final String USER_ID = "021131";
    private static final String PASSWORD = "chen8885257";

    private String[] INSTRUMENTS = {"rb1810", "cu1807", "sc1809"};
    private String[] INSTRUMENTNAMES = {"螺纹1810", "沪铜1807", "原油1809"};
    private List<HashMap<String, String>> listData = new ArrayList<>();

    private ListView mListView;
    private CommonAdapter commonAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contract, container, false);

//        mdRequest();
        initLayout(view);

        return view;
    }


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
            item.put("SubName", INSTRUMENTNAMES[i]);
            item.put("LatestPrice", "--");
            item.put("Change", "--");

            listData.add(item);
        }

        commonAdapter = new CommonAdapter<HashMap<String, String>>(getContext(), listData, R.layout.listitem_contract) {
            @Override
            protected void convertView(View item, HashMap<String, String> map) {

                int change = 0;
                if (map.get("Change") != "--") {
                    change = Integer.parseInt(map.get("Change"));
                }

                TextView tvName = CommonViewHolder.get(item, R.id.contractName);
                tvName.setText("  " + map.get("Name"));
                tvName.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                TextView tvSubName = CommonViewHolder.get(item, R.id.contractSubName);
                tvSubName.setText(map.get("SubName"));

                TextView tvLatestPrice = CommonViewHolder.get(item, R.id.contractLatestPrice);
                tvLatestPrice.setText(map.get("LatestPrice"));

                TextView tvChange = CommonViewHolder.get(item, R.id.contractChange);
                tvChange.setText(map.get("Change"));

                if (change < 0) {
                    tvLatestPrice.setTextColor(Color.GREEN);
                    tvChange.setTextColor(Color.GREEN);

                }
                if (change > 0) {
                    tvLatestPrice.setTextColor(Color.RED);
                    tvChange.setTextColor(Color.RED);
                }
            }
        };

        mListView.setAdapter(commonAdapter);

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
        Log.i("Lily", "--->发送用户登录请求: " + ((iResult == 0) ? "成功" : "失败"));
    }

    public void OnFrontDisconnected(int nReason) {

    }

    public void OnHeartBeatWarning(int nTimeLapse) {

    }


    public void OnRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin,
                               CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        if (bIsLast && pRspInfo.getErrorID() == 0) {
            Log.i("Lily", "--->当前交易日:" + pRspUserLogin.getTradingDay());

            String[] ins = {"rb1810"};
            int iResult = mdApi.SubscribeMarketData(ins, ins.length);
            Log.i("Lily", "--->>> 发送行情订阅请求: " + ((iResult == 0) ? "成功" : "失败"));
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
        int position = 0;
        for (int i = 0; i < listData.size(); i++) {
            if (listData.get(i).get("Name").equals(pDepthMarketData.getInstrumentID())) {
                position = i;
                HashMap<String, String> item = listData.get(i);
                item.remove("LatestPrice");
                item.put("LatestPrice", String.valueOf(pDepthMarketData.getLastPrice()));
                item.remove("Change");
                item.put("Change", String.valueOf(pDepthMarketData.getLastPrice() - pDepthMarketData.getOpenPrice()));
                listData.set(position, item);
                updateListViewItem(position);
                break;
            }
        }
    }

    public void OnRtnForQuoteRsp(CThostFtdcForQuoteRspField pForQuoteRsp) {

    }
    //endregion

}
