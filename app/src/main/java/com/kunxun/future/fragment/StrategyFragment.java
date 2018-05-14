package com.kunxun.future.fragment;

        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.design.widget.TabLayout;
        import android.support.v4.app.Fragment;
        import android.support.v4.content.ContextCompat;
        import android.support.v4.view.ViewCompat;
        import android.support.v4.view.ViewPager;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import com.kunxun.future.R;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.Objects;

public class StrategyFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_strategy, container, false);
        updateLayout(view);
        return view;
    }

    private void updateLayout(View view) {

        TabLayout tbStrategy = view.findViewById(R.id.tbStrategy);
        ViewPager vpStrategy = view.findViewById(R.id.vpStrategy);

        LongFragment longFragment = new LongFragment();
        ShortFragment shortFragment = new ShortFragment();

        List<Fragment> tbFragment = new ArrayList<>();
        tbFragment.add(longFragment);
        tbFragment.add(shortFragment);

        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getChildFragmentManager(), tbFragment);
        vpStrategy.setOffscreenPageLimit(2);
        vpStrategy.setAdapter(mFragmentAdapter);
        vpStrategy.setCurrentItem(0);

        tbStrategy.setTabMode(TabLayout.MODE_FIXED);
        tbStrategy.setTabTextColors(ContextCompat.getColor(getContext(), R.color.colorPrimary), ContextCompat.getColor(getContext(), R.color.colorAccent));
        tbStrategy.setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        ViewCompat.setElevation(tbStrategy, 10);

        tbStrategy.setupWithViewPager(vpStrategy);

        Objects.requireNonNull(tbStrategy.getTabAt(0)).setText(getString(R.string.string_strategy_long));
        Objects.requireNonNull(tbStrategy.getTabAt(1)).setText(getString(R.string.string_strategy_short));
    }
}
