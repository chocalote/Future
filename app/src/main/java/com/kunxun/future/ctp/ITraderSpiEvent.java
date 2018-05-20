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
import com.sfit.ctp.thosttraderapi.CThostFtdcTradingAccountField;

public interface ITraderSpiEvent {


    ///当客户端与交易后台建立起通信连接时（还未登录前），该方法被调用。
    public abstract void OnFrontConnected();

    ///登录请求响应
    public abstract void OnRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin, CThostFtdcRspInfoField pRspInfo, int nRequestID,  boolean bIsLast);

    ///投资者结算结果确认响应
    public abstract void OnRspSettlementInfoConfirm(CThostFtdcSettlementInfoConfirmField pSettlementInfoConfirm, CThostFtdcRspInfoField  pRspInfo, int nRequestID, boolean bIsLast);

    ///请求查询合约响应
    public abstract void OnRspQryInstrument(CThostFtdcInstrumentField pInstrument, CThostFtdcRspInfoField  pRspInfo, int nRequestID, boolean bIsLast);

    ///请求查询资金账户响应
    public abstract void OnRspQryTradingAccount(CThostFtdcTradingAccountField pTradingAccount, CThostFtdcRspInfoField  pRspInfo, int nRequestID, boolean bIsLast);

    ///请求查询投资者持仓响应
    public abstract void OnRspQryInvestorPosition(CThostFtdcInvestorPositionField pInvestorPosition, CThostFtdcRspInfoField  pRspInfo, int nRequestID, boolean bIsLast);

    ///报单录入请求响应
    public abstract void OnRspOrderInsert(CThostFtdcInputOrderField pInputOrder, CThostFtdcRspInfoField  pRspInfo, int nRequestID, boolean bIsLast);

    ///报单操作请求响应
    public abstract void OnRspOrderAction(CThostFtdcInputOrderActionField pInputOrderAction, CThostFtdcRspInfoField  pRspInfo, int nRequestID, boolean bIsLast);

    ///错误应答
    public abstract void OnRspError(CThostFtdcRspInfoField  pRspInfo, int nRequestID,  boolean bIsLast);

    ///当客户端与交易后台通信连接断开时，该方法被调用。当发生这个情况后，API会自动重新连接，客户端可不做处理。
    public abstract void OnFrontDisconnected(int nReason);

    ///心跳超时警告。当长时间未收到报文时，该方法被调用。
    public abstract void OnHeartBeatWarning(int nTimeLapse);

    ///报单通知
    public abstract void OnRtnOrder(CThostFtdcOrderField pOrder);

    ///成交通知
    public abstract void OnRtnTrade(CThostFtdcTradeField pTrade);

    ///请求查询合约保证金率响应
    public abstract void OnRspQryInstrumentMarginRate(CThostFtdcInstrumentMarginRateField pInstrumentMarginRate, CThostFtdcRspInfoField  pRspInfo, int nRequestID, boolean bIsLast);

    ///请求查询合约手续费率响应
    public abstract void OnRspQryInstrumentCommissionRate(CThostFtdcInstrumentCommissionRateField pInstrumentCommissionRate, CThostFtdcRspInfoField  pRspInfo, int nRequestID, boolean bIsLast);

}
