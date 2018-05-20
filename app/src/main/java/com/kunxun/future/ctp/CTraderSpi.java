package com.kunxun.future.ctp;

import com.sfit.ctp.thosttraderapi.CThostFtdcInputOrderActionField;
import com.sfit.ctp.thosttraderapi.CThostFtdcInputOrderField;
import com.sfit.ctp.thosttraderapi.CThostFtdcInstrumentCommissionRateField;
import com.sfit.ctp.thosttraderapi.CThostFtdcInstrumentField;
import com.sfit.ctp.thosttraderapi.CThostFtdcInstrumentMarginRateField;
import com.sfit.ctp.thosttraderapi.CThostFtdcInvestorPositionField;
import com.sfit.ctp.thosttraderapi.CThostFtdcOrderField;
import com.sfit.ctp.thosttraderapi.CThostFtdcRspInfoField;
import com.sfit.ctp.thosttraderapi.CThostFtdcRspUserLoginField;
import com.sfit.ctp.thosttraderapi.CThostFtdcSettlementInfoConfirmField;
import com.sfit.ctp.thosttraderapi.CThostFtdcTradeField;
import com.sfit.ctp.thosttraderapi.CThostFtdcTraderSpi;
import com.sfit.ctp.thosttraderapi.CThostFtdcTradingAccountField;

public class CTraderSpi extends CThostFtdcTraderSpi {

    private ITraderSpiEvent iTraderSpiEvent;

    public void setInterface(ITraderSpiEvent i){
        this.iTraderSpiEvent = i;
    }

    @Override
    public void OnFrontConnected() {
        iTraderSpiEvent.OnFrontConnected();
    }

    @Override
    public void OnFrontDisconnected(int nReason) {
        iTraderSpiEvent.OnFrontDisconnected(nReason);
    }

    @Override
    public void OnHeartBeatWarning(int nTimeLapse) {
        iTraderSpiEvent.OnHeartBeatWarning(nTimeLapse);
    }

    @Override
    public void OnRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        iTraderSpiEvent.OnRspUserLogin(pRspUserLogin, pRspInfo, nRequestID, bIsLast);
    }

    @Override
    public void OnRspOrderInsert(CThostFtdcInputOrderField pInputOrder, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        iTraderSpiEvent.OnRspOrderInsert(pInputOrder, pRspInfo, nRequestID, bIsLast);
    }

    @Override
    public void OnRspOrderAction(CThostFtdcInputOrderActionField pInputOrderAction, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        iTraderSpiEvent.OnRspOrderAction(pInputOrderAction, pRspInfo, nRequestID, bIsLast);
    }

    @Override
    public void OnRspSettlementInfoConfirm(CThostFtdcSettlementInfoConfirmField pSettlementInfoConfirm, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        iTraderSpiEvent.OnRspSettlementInfoConfirm(pSettlementInfoConfirm, pRspInfo, nRequestID, bIsLast);
    }

    @Override
    public void OnRspQryInvestorPosition(CThostFtdcInvestorPositionField pInvestorPosition, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        iTraderSpiEvent.OnRspQryInvestorPosition(pInvestorPosition, pRspInfo, nRequestID, bIsLast);
    }

    @Override
    public void OnRspQryTradingAccount(CThostFtdcTradingAccountField pTradingAccount, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        iTraderSpiEvent.OnRspQryTradingAccount(pTradingAccount, pRspInfo, nRequestID, bIsLast);
    }

    @Override
    public void OnRspQryInstrumentMarginRate(CThostFtdcInstrumentMarginRateField pInstrumentMarginRate, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        iTraderSpiEvent.OnRspQryInstrumentMarginRate(pInstrumentMarginRate, pRspInfo, nRequestID, bIsLast);
    }

    @Override
    public void OnRspQryInstrumentCommissionRate(CThostFtdcInstrumentCommissionRateField pInstrumentCommissionRate, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        iTraderSpiEvent.OnRspQryInstrumentCommissionRate(pInstrumentCommissionRate, pRspInfo, nRequestID, bIsLast);
    }

    @Override
    public void OnRspQryInstrument(CThostFtdcInstrumentField pInstrument, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        iTraderSpiEvent.OnRspQryInstrument(pInstrument, pRspInfo, nRequestID, bIsLast);
    }

    @Override
    public void OnRspError(CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        iTraderSpiEvent.OnRspError(pRspInfo, nRequestID, bIsLast);
    }

    @Override
    public void OnRtnOrder(CThostFtdcOrderField pOrder) {
        iTraderSpiEvent.OnRtnOrder(pOrder);
    }

    @Override
    public void OnRtnTrade(CThostFtdcTradeField pTrade) {
        iTraderSpiEvent.OnRtnTrade(pTrade);
    }
}
