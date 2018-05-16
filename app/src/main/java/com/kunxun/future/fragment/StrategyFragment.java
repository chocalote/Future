package com.kunxun.future.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.kunxun.future.R;


public class StrategyFragment extends Fragment {

    public TextView tvTendencyMemo;
    public RadioGroup rgOperation, rgDayTendency, rgHourTendency, rg5MinutesTendency, rgMinuteTendency;
    public RadioButton rbOpLongOpen, rbOpLongClose, rbOpShortOpen;
    public RadioButton rbDayTendencyUp, rbDayTendencyDown;
    public RadioButton rbHourTendencyUp, rbHourTendencyDown;
    public RadioButton rb5MinutesTendencyUp, rb5MinutesTendencyDown;
    public RadioButton rbMinuteTendencyGolden, rbMinuteTendencyDead;
    public Switch sthDayTendency, sthHourTendency, sth5MinutesTendency, sthMinuteTendency;
    public boolean tendencyStates[][] = {{true, true, true, true}, {true, true, true, true},
            {true, true, true, true}, {true, true, true, true}};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_strategy, container, false);

        initLayout(view);

        return view;
    }


    //region initLayout
    private void initLayout(final View view) {

        rgOperation = view.findViewById(R.id.rgOperation);
        rgDayTendency = view.findViewById(R.id.rgDayTendency);
        rgHourTendency = view.findViewById(R.id.rgHourTendency);
        rg5MinutesTendency = view.findViewById(R.id.rg5MinutesTendency);
        rgMinuteTendency = view.findViewById(R.id.rgMinuteTendency);

        rbOpLongOpen = view.findViewById(R.id.rbOpLongOpen);
        rbOpLongClose = view.findViewById(R.id.rbOpLongClose);
        rbOpShortOpen = view.findViewById(R.id.rbOpShortOpen);

        rbDayTendencyUp = view.findViewById(R.id.rbDayTendencyUp);
        rbDayTendencyDown = view.findViewById(R.id.rbDayTendencyDown);

        rbHourTendencyUp = view.findViewById(R.id.rbHourTendencyUp);
        rbHourTendencyDown = view.findViewById(R.id.rbHourTendencyDown);

        rb5MinutesTendencyUp = view.findViewById(R.id.rb5MinutesTendencyUp);
        rb5MinutesTendencyDown = view.findViewById(R.id.rb5MinutesTendencyDown);

        rbMinuteTendencyGolden = view.findViewById(R.id.rbMinuteTendencyGolden);
        rbMinuteTendencyDead = view.findViewById(R.id.rbMinuteTendencyDead);

        sthDayTendency = view.findViewById(R.id.sthDayTendency);
        sthHourTendency = view.findViewById(R.id.sthHourTendency);
        sth5MinutesTendency = view.findViewById(R.id.sth5MinutesTendency);
        sthMinuteTendency = view.findViewById(R.id.sthMinuteTendency);

        tvTendencyMemo = view.findViewById(R.id.tvTendencyMemo);
        tvTendencyMemo.setText(getTendencyMemo(view));

        rgOperation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int row = 0;

                if (checkedId == rbOpLongOpen.getId()) {
                    rgDayTendency.check(rbDayTendencyUp.getId());
                    rgHourTendency.check(rbHourTendencyUp.getId());
                    rg5MinutesTendency.check(rb5MinutesTendencyUp.getId());
                    rgMinuteTendency.check(rbMinuteTendencyGolden.getId());

                } else if (checkedId == rbOpLongClose.getId()) {
                    rgDayTendency.check(rbDayTendencyUp.getId());
                    rgHourTendency.check(rbHourTendencyUp.getId());
                    rg5MinutesTendency.check(rb5MinutesTendencyUp.getId());
                    rgMinuteTendency.check(rbMinuteTendencyDead.getId());
                    row = 1;

                } else if (checkedId == rbOpShortOpen.getId()) {
                    rgDayTendency.check(rbDayTendencyDown.getId());
                    rgHourTendency.check(rbHourTendencyDown.getId());
                    rg5MinutesTendency.check(rb5MinutesTendencyDown.getId());
                    rgMinuteTendency.check(rbMinuteTendencyDead.getId());
                    row = 2;

                } else {
                    rgDayTendency.check(rbDayTendencyDown.getId());
                    rgHourTendency.check(rbHourTendencyDown.getId());
                    rg5MinutesTendency.check(rb5MinutesTendencyDown.getId());
                    rgMinuteTendency.check(rbMinuteTendencyGolden.getId());
                    row = 3;
                }

                sthDayTendency.setChecked(tendencyStates[row][0]);
                sthHourTendency.setChecked(tendencyStates[row][1]);
                sth5MinutesTendency.setChecked(tendencyStates[row][2]);
                sthMinuteTendency.setChecked(tendencyStates[row][3]);

                tvTendencyMemo.setText(getTendencyMemo(view));
            }
        });

        View.OnClickListener switchClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int row = 0, column = 0;
                switch (rgOperation.getCheckedRadioButtonId()) {
                    case R.id.rbOpLongOpen:
                        row = 0;
                        break;
                    case R.id.rbOpLongClose:
                        row = 1;
                        break;
                    case R.id.rbOpShortOpen:
                        row = 2;
                        break;
                    case R.id.rbOpShortClose:
                        row = 3;
                        break;
                }

                switch (v.getId()) {
                    case R.id.sthDayTendency:
                        column = 0;
                        break;
                    case R.id.sthHourTendency:
                        column = 1;
                        break;
                    case R.id.sth5MinutesTendency:
                        column = 2;
                        break;
                    case R.id.sthMinuteTendency:
                        column = 3;
                        break;
                }

                tendencyStates[row][column] = ((Switch) view.findViewById(v.getId())).isChecked();
                tvTendencyMemo.setText(getTendencyMemo(view));
            }
        };

        sthDayTendency.setOnClickListener(switchClickListener);
        sthHourTendency.setOnClickListener(switchClickListener);
        sth5MinutesTendency.setOnClickListener(switchClickListener);
        sthMinuteTendency.setOnClickListener(switchClickListener);

    }
    //endregion

    //region TextView.setText
    private String getTendencyMemo(View view) {
        String memo = ((RadioButton) view.findViewById(rgOperation.getCheckedRadioButtonId())).getText() + "策略：";
        memo += sthDayTendency.isChecked() ? "\n" + getString(R.string.strategy_day)
                + ((RadioButton) view.findViewById(rgDayTendency.getCheckedRadioButtonId())).getText() : "";
        memo += sthHourTendency.isChecked() ? "\n" + getString(R.string.strategy_hour)
                + ((RadioButton) view.findViewById(rgHourTendency.getCheckedRadioButtonId())).getText() : "";
        memo += sth5MinutesTendency.isChecked() ? "\n" + getString(R.string.strategy_5min)
                + ((RadioButton) view.findViewById(rg5MinutesTendency.getCheckedRadioButtonId())).getText() : "";
        memo += sthMinuteTendency.isChecked() ? "\n" + getString(R.string.strategy_min)
                + ((RadioButton) view.findViewById(rgMinuteTendency.getCheckedRadioButtonId())).getText() : "";

        return String.format(getString(R.string.tendency_memo), memo);
    }
    //endregion


}
