package com.kunxun.future.ctp;

import com.sfit.ctp.thostmduserapi.CThostFtdcDepthMarketDataField;
import com.sfit.ctp.thostmduserapi.CThostFtdcForQuoteRspField;
import com.sfit.ctp.thostmduserapi.CThostFtdcRspInfoField;
import com.sfit.ctp.thostmduserapi.CThostFtdcRspUserLoginField;
import com.sfit.ctp.thostmduserapi.CThostFtdcSpecificInstrumentField;
import com.sfit.ctp.thostmduserapi.CThostFtdcUserLogoutField;

public interface IMdSpiEvent {

    public abstract void OnFrontConnected();

    public abstract void OnFrontDisconnected(int nReason);

    public abstract void OnHeartBeatWarning(int nTimeLapse);

    public abstract void OnRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin,
                                        CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast);

    public abstract void OnRspUserLogout(CThostFtdcUserLogoutField pUserLogout,
                                         CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast);

    public abstract void OnRspError(CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast);

    public abstract void OnRspSubMarketData(CThostFtdcSpecificInstrumentField pSpecificInstrument,
                                            CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast);

    public abstract void OnRspUnSubMarketData(CThostFtdcSpecificInstrumentField pSpecificInstrument,
                                              CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast);

    public abstract void OnRspSubForQuoteRsp(CThostFtdcSpecificInstrumentField pSpecificInstrument,
                                    CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast);
    public abstract void OnRspUnSubForQuoteRsp(CThostFtdcSpecificInstrumentField pSpecificInstrument,
                                      CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast);

    public abstract void OnRtnDepthMarketData(CThostFtdcDepthMarketDataField pDepthMarketData);

    public abstract void OnRtnForQuoteRsp(CThostFtdcForQuoteRspField pForQuoteRsp);

}
