package com.kunxun.future.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.kunxun.future.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContractFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contract, container, false);
        updateLayout(view);
        return view;
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
        SimpleAdapter listViewAdapter = new SimpleAdapter(getActivity(), listData, R.layout.listitem_contract,
                new String[]{"Name", "LatestPrice", "Change"}, new int[]{R.id.contractName, R.id.contractLatestPrice, R.id.contractChange});


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
}
