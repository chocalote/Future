package com.kunxun.future;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ContractFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contract, container, false);
        updateLayout(view);
        return view;
    }

    private void updateLayout(View view){
        List<String> stringList = new ArrayList<>();
        for (int i =0; i<5;i++)
        {
            stringList.add(String.valueOf(i));
        }
        ListView mListView = view.findViewById(R.id.mContractList);
        ArrayAdapter listViewAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,stringList);
        mListView.setAdapter(listViewAdapter);

        SwipeRefreshLayout mSwipeLayout = view.findViewById(R.id.mSwipeRefreshLayout);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });


    }
}
