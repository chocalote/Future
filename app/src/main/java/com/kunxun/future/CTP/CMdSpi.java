package com.kunxun.future.CTP;


import com.sfit.ctp.thostmduserapi.CThostFtdcDepthMarketDataField;
import com.sfit.ctp.thostmduserapi.CThostFtdcForQuoteRspField;
import com.sfit.ctp.thostmduserapi.CThostFtdcMdSpi;
import com.sfit.ctp.thostmduserapi.CThostFtdcRspInfoField;
import com.sfit.ctp.thostmduserapi.CThostFtdcRspUserLoginField;
import com.sfit.ctp.thostmduserapi.CThostFtdcSpecificInstrumentField;
import com.sfit.ctp.thostmduserapi.CThostFtdcUserLogoutField;

import java.util.List;

public class CMdSpi extends CThostFtdcMdSpi {

    public IMdSpiEvent iMdSpiEvent;


    public void setInterface(IMdSpiEvent i)
    {
        this.iMdSpiEvent = i;
    }

    @Override
    public void OnFrontConnected() {
        iMdSpiEvent.OnFrontConnected();
    }

    @Override
    public void OnFrontDisconnected(int nReason) {
        iMdSpiEvent.OnFrontDisconnected(nReason);
    }

    @Override
    public void OnHeartBeatWarning(int nTimeLapse) {
        iMdSpiEvent.OnHeartBeatWarning(nTimeLapse);
    }

    @Override
    public void OnRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin,
                               CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        iMdSpiEvent.OnRspUserLogin(pRspUserLogin, pRspInfo, nRequestID, bIsLast);
    }

    @Override
    public void OnRspUserLogout(CThostFtdcUserLogoutField pUserLogout,
                                CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        iMdSpiEvent.OnRspUserLogout(pUserLogout, pRspInfo, nRequestID, bIsLast);
    }

    @Override
    public void OnRspError(CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        iMdSpiEvent.OnRspError(pRspInfo, nRequestID, bIsLast);
    }

    @Override
    public void OnRspSubMarketData(CThostFtdcSpecificInstrumentField pSpecificInstrument,
                                   CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        iMdSpiEvent.OnRspSubMarketData(pSpecificInstrument, pRspInfo, nRequestID, bIsLast);
    }

    @Override
    public void OnRspUnSubMarketData(CThostFtdcSpecificInstrumentField pSpecificInstrument,
                                     CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        iMdSpiEvent.OnRspUnSubMarketData(pSpecificInstrument, pRspInfo, nRequestID, bIsLast);
    }

    @Override
    public void OnRspSubForQuoteRsp(CThostFtdcSpecificInstrumentField pSpecificInstrument,
                                    CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        iMdSpiEvent.OnRspSubForQuoteRsp(pSpecificInstrument, pRspInfo, nRequestID, bIsLast);
    }

    @Override
    public void OnRspUnSubForQuoteRsp(CThostFtdcSpecificInstrumentField pSpecificInstrument,
                                      CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        iMdSpiEvent.OnRspUnSubForQuoteRsp(pSpecificInstrument, pRspInfo, nRequestID, bIsLast);
    }

    @Override
    public void OnRtnForQuoteRsp(CThostFtdcForQuoteRspField pForQuoteRsp) {
        iMdSpiEvent.OnRtnForQuoteRsp(pForQuoteRsp);
    }

    @Override
    public void OnRtnDepthMarketData(CThostFtdcDepthMarketDataField pDepthMarketData) {
        iMdSpiEvent.OnRtnDepthMarketData(pDepthMarketData);
    }

}
