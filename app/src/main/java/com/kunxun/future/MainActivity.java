package com.kunxun.future;


import android.graphics.Typeface;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Fragment> mFragmentList = new ArrayList<>();
    private ViewPager mViewPager;
    private TextView tvContract, tvStrategy, tvTransaction, tvSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateLayout();

        FragmentAdapter mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
        mViewPager = findViewById(R.id.mViewPager);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setCurrentItem(0);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTextFont(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public class FragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList;

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            mFragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }

    private void updateLayout() {

        tvContract = findViewById(R.id.tvContract);
        tvContract.setTextColor(getResources().getColor(R.color.colorAccent));
        tvStrategy = findViewById(R.id.tvStrategy);
        tvTransaction = findViewById(R.id.tvTransaction);
        tvSetting = findViewById(R.id.tvSetting);

        tvContract.setOnClickListener(this);
        tvStrategy.setOnClickListener(this);
        tvTransaction.setOnClickListener(this);
        tvSetting.setOnClickListener(this);

        ContractFragment contractFragment = new ContractFragment();
        StrategyFragment strategyFragment = new StrategyFragment();
        TransactionFragment transactionFragment = new TransactionFragment();
        SettingFragment settingFragment = new SettingFragment();
        mFragmentList.add(contractFragment);
        mFragmentList.add(strategyFragment);
        mFragmentList.add(transactionFragment);
        mFragmentList.add(settingFragment);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvContract:
                mViewPager.setCurrentItem(0, true);
                break;
            case R.id.tvStrategy:
                mViewPager.setCurrentItem(1, true);
                break;
            case R.id.tvTransaction:
                mViewPager.setCurrentItem(2, true);
                break;
            case R.id.tvSetting:
                mViewPager.setCurrentItem(3, true);
                break;
        }
    }

    private void changeTextFont(int position) {
        switch (position) {
            case 0:
                tvContract.setTextColor(getResources().getColor(R.color.colorAccent));
                tvStrategy.setTextColor(getResources().getColor(R.color.colorPrimary));
                tvTransaction.setTextColor(getResources().getColor(R.color.colorPrimary));
                tvSetting.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 1:
                tvContract.setTextColor(getResources().getColor(R.color.colorPrimary));
                tvStrategy.setTextColor(getResources().getColor(R.color.colorAccent));
                tvTransaction.setTextColor(getResources().getColor(R.color.colorPrimary));
                tvSetting.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 2:
                tvContract.setTextColor(getResources().getColor(R.color.colorPrimary));
                tvStrategy.setTextColor(getResources().getColor(R.color.colorPrimary));
                tvTransaction.setTextColor(getResources().getColor(R.color.colorAccent));
                tvSetting.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 3:
                tvContract.setTextColor(getResources().getColor(R.color.colorPrimary));
                tvStrategy.setTextColor(getResources().getColor(R.color.colorPrimary));
                tvTransaction.setTextColor(getResources().getColor(R.color.colorPrimary));
                tvSetting.setTextColor(getResources().getColor(R.color.colorAccent));
                break;
        }
    }
}
