package com.kunxun.future;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.kunxun.future.fragment.ContractFragment;
import com.kunxun.future.fragment.FragmentAdapter;
import com.kunxun.future.fragment.LoginFragment;
import com.kunxun.future.fragment.SettingFragment;
import com.kunxun.future.fragment.StrategyFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(this);

        List<String> tabIndicators = new ArrayList<>();
        List<Fragment> tabFragments = new ArrayList<>();

        tabIndicators.add(getResources().getString(R.string.string_contract));
        tabIndicators.add(getResources().getString(R.string.string_strategy));
        tabIndicators.add(getResources().getString(R.string.string_transaction));
        tabIndicators.add(getResources().getString(R.string.string_setting));

        ContractFragment contractFragment = new ContractFragment();
        StrategyFragment strategyFragment = new StrategyFragment();
        LoginFragment loginFragment = new LoginFragment();
        SettingFragment settingFragment = new SettingFragment();

        tabFragments.add(contractFragment);
        tabFragments.add(strategyFragment);
        tabFragments.add(loginFragment);
        tabFragments.add(settingFragment);

        FragmentAdapter mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), tabFragments);
        ViewPager mViewPager = findViewById(R.id.mViewPager);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setCurrentItem(0);

        TabLayout mTabLayout = findViewById(R.id.tbLayout);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.colorPrimary), ContextCompat.getColor(this, R.color.colorAccent));
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorAccent));
        ViewCompat.setElevation(mTabLayout, 10);

        mTabLayout.setupWithViewPager(mViewPager);

        for (int i = 0; i < tabIndicators.size(); i++) {
            Objects.requireNonNull(mTabLayout.getTabAt(i)).setText(tabIndicators.get(i));
        }
    }

    private static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
