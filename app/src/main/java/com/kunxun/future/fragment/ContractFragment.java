package com.kunxun.future.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kunxun.future.InstrumentActivity;
import com.kunxun.future.ctp.CDepthMarketData;
import com.kunxun.future.ctp.MdService;
import com.kunxun.future.R;
import com.kunxun.future.adapter.CommonAdapter;
import com.kunxun.future.adapter.CommonViewHolder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContractFragment extends Fragment {

    private static final String PREFERENCES = "future";

    private String[] INSTRUMENTS = {"rb1810", "cu1807", "sc1809"};
    private String[] INSTRUMENT_NAMES = {"螺纹1810", "沪铜1807", "原油1809"};
    private List<HashMap<String, String>> listData = new ArrayList<>();

    private ListView mListView;
    private CommonAdapter commonAdapter;
    private SwipeRefreshLayout mSwipeLayout;

    private MdBroadcastReceiver mReceiver;
    public static final String ACTION_UPDATE_UI = "com.kunxun.future.updateUI";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(getActivity(), MdService.class);
        getActivity().startService(intent);
    }

    @Override
    public void onStart() {
        mReceiver = new MdBroadcastReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_UPDATE_UI);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, filter);

        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Lily", "contractFragment onDestroy");
        getActivity().stopService(new Intent(getActivity(), MdService.class));
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contract, container, false);

        setInstruments(INSTRUMENTS);
        initLayout(view);

        return view;
    }

    public class MdBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.i("Lily", "onReceive");

            CDepthMarketData data = (CDepthMarketData)intent.getSerializableExtra("DepthMarketData");

            int position = getPosition(data.instrumentId);

            if (!listData.get(position).get("LatestPrice").equals(String.valueOf(data.lastPrice))) {
                listData.get(position).remove("LatestPrice");
                listData.get(position).put("LatestPrice", String.valueOf(data.lastPrice));
                listData.get(position).remove("Change");
                listData.get(position).put("Change", String.valueOf(data.lastPrice-data.openPrice));
                updateListViewItem(position);
            }
        }
    }

    private int getPosition(String ins){
        int ret = 0;
        for (int i=0;i<INSTRUMENTS.length;i++)
        {
            if (INSTRUMENTS[i].equals(ins)) {
                ret = i;
                break;
            }
        }
        return ret;
    }


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

        //region CommonAdapter convertView
        commonAdapter = new CommonAdapter<HashMap<String, String>>(getContext(), listData, R.layout.listitem_contract) {
            @Override
            protected void convertView(View item, HashMap<String, String> map) {

                double change = 0;
                if (!map.get("Change").equals("--")) {
                    change = Double.parseDouble(map.get("Change"));

                    double lastPrice = Double.parseDouble(map.get("LatestPrice"));
                    if (change > lastPrice * 0.5 || change < -lastPrice * 0.5) {
                        change = 0;
                    }
                }

                TextView tvName = CommonViewHolder.get(item, R.id.contractName);
                String tmp = "  " + map.get("Name");
                tvName.setText(tmp);

                TextView tvSubName = CommonViewHolder.get(item, R.id.contractSubName);
                tvSubName.setText(map.get("SubName"));

                TextView tvLatestPrice = CommonViewHolder.get(item, R.id.contractLatestPrice);
                tvLatestPrice.setText(map.get("LatestPrice"));

                TextView tvChange = CommonViewHolder.get(item, R.id.contractChange);
                tvChange.setText(new DecimalFormat("0.0").format(change));

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
        //endregion

        mListView.setAdapter(commonAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), InstrumentActivity.class);
                intent.putExtra("Instrument", listData.get(position).get("Name") + ", " + listData.get(position).get("SubName"));
                startActivity(intent);
            }
        });


        TextView tv_Edit = view.findViewById(R.id.tvEdit);
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

        mSwipeLayout = view.findViewById(R.id.mSwipeRefreshLayout);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Intent intent = new Intent(getActivity(), MdService.class);
                getActivity().stopService(intent);

                getActivity().startService(intent);

                if (mSwipeLayout.isRefreshing()) {
                    mSwipeLayout.setRefreshing(false);
                }
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

    private void setInstruments(String[] ins) {
        SharedPreferences mPrefs = getContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt("InstrumentsCount", ins.length);
        for (int i = 0; i < ins.length; i++) {
            editor.putString("Instrument" + String.valueOf(i), ins[i]);
        }
        editor.apply();
    }
    //endregion


}
