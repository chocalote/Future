package com.kunxun.future;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.kunxun.future.ctp.CDepthMarketData;
import com.kunxun.future.ctp.MdService;
import com.kunxun.future.fragment.ContractFragment;

import java.text.DecimalFormat;


public class InstrumentActivity extends AppCompatActivity {

    private String instrumentId, instrumentName;
    private MdBroadcastReceiver mReceiver;
    private TextView tvLastPrice, tvLongPrice, tvShortPrice, tvChange,
            tvChangePercent, tvLongPosition, tvShortPosition,tvPosition, tvPositionAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrument);

        String extra = getIntent().getStringExtra("Instrument");
        String [] exs = extra.split(", ");
        instrumentId = exs[0];
        instrumentName = exs[1];

        Intent intent = new Intent(this, MdService.class);
        bindService(intent,conn,Context.BIND_AUTO_CREATE);
        initLayout();
    }

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MdService.MdBinder mdBinder = (MdService.MdBinder)service;
            mdBinder.callChangeInstrument(instrumentId);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {


        }
    };

    private void initLayout()
    {
        TextView tvInsId = findViewById(R.id.tvInsId);
        TextView tvInsName = findViewById(R.id.tvInsName);
        tvLastPrice = findViewById(R.id.tvLastPrice);
        tvLongPrice = findViewById(R.id.tvLongPrice);
        tvLongPosition = findViewById(R.id.tvLongPosition);
        tvShortPrice = findViewById(R.id.tvShortPrice);
        tvShortPosition = findViewById(R.id.tvShortPosition);
        tvChange = findViewById(R.id.tvChange);
        tvChangePercent = findViewById(R.id.tvChangePercent);
        tvPosition = findViewById(R.id.tvPosition);
        tvPositionAdd =findViewById(R.id.tvPositionAdd);

        tvInsId.setText(instrumentId);
        tvInsName.setText(instrumentName);

    }

    @Override
    protected void onStart() {
        mReceiver = new MdBroadcastReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ContractFragment.ACTION_UPDATE_UI);

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filter);
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    public class MdBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.i("Lily", "InstrumentActivity onReceive");

            CDepthMarketData data = (CDepthMarketData)intent.getSerializableExtra("DepthMarketData");
            updateLayout(data);
        }
    }

    private void updateLayout(CDepthMarketData data) {

        double change = data.lastPrice- data.openPrice;

        if (change > 0 ){
            tvLastPrice.setTextColor(getResources().getColor(R.color.colorRed));
            tvChange.setTextColor(getResources().getColor(R.color.colorRed));
            tvChangePercent.setTextColor(getResources().getColor(R.color.colorRed));
            tvPosition.setTextColor(getResources().getColor(R.color.colorRed));
            tvPositionAdd.setTextColor(getResources().getColor(R.color.colorRed));
        }
        else {
            tvLastPrice.setTextColor(getResources().getColor(R.color.colorAccent));
            tvChange.setTextColor(getResources().getColor(R.color.colorAccent));
            tvChangePercent.setTextColor(getResources().getColor(R.color.colorAccent));
            tvPosition.setTextColor(getResources().getColor(R.color.colorAccent));
            tvPositionAdd.setTextColor(getResources().getColor(R.color.colorAccent));
        }

        tvLastPrice.setText(String.valueOf(data.lastPrice));
        tvChange.setText(String.valueOf(change));
        double changePercent = ((data.lastPrice - data.openPrice) / data.openPrice) * 100;
        String cp = String.valueOf(new DecimalFormat("0.00").format(changePercent)) + "%";
        tvChangePercent.setText(cp);

        tvLongPrice.setText(String.valueOf(data.bidPrice1));
        tvLongPosition.setText(String.valueOf(data.bidVolume1));
        if(data.bidPrice1 >= data.openPrice)
        {
            tvLongPrice.setTextColor(getResources().getColor(R.color.colorRed));
            tvLongPosition.setTextColor(getResources().getColor(R.color.colorRed));
        }
        else {
            tvLongPrice.setTextColor(getResources().getColor(R.color.colorAccent));
            tvLongPosition.setTextColor(getResources().getColor(R.color.colorAccent));
            tvShortPrice.setTextColor(getResources().getColor(R.color.colorAccent));
            tvShortPosition.setTextColor(getResources().getColor(R.color.colorAccent));
        }

        tvShortPrice.setText(String.valueOf(data.askPrice1));
        tvShortPosition.setText(String.valueOf(data.askVolume1));
        if (data.askPrice1 >= data.openPrice)
        {
            tvLongPrice.setTextColor(getResources().getColor(R.color.colorRed));
            tvLongPosition.setTextColor(getResources().getColor(R.color.colorRed));
            tvShortPrice.setTextColor(getResources().getColor(R.color.colorRed));
            tvShortPosition.setTextColor(getResources().getColor(R.color.colorRed));
        }
        else {
            tvShortPrice.setTextColor(getResources().getColor(R.color.colorAccent));
            tvShortPosition.setTextColor(getResources().getColor(R.color.colorAccent));
        }


        tvPosition.setText(String.valueOf(data.openInterest));
        tvPositionAdd.setText(String.valueOf(data.interestChange));
    }
}
