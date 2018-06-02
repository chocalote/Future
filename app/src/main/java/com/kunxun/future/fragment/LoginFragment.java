package com.kunxun.future.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kunxun.future.TransactionActivity;
import com.kunxun.future.ctp.TraderService;
import com.kunxun.future.R;
import com.kunxun.future.utils.CodeUtils;


public class LoginFragment extends Fragment {


    private static final String TAG = "Lily";
    private static final String PREFERENCES = "future";

    //    private RadioGroup rgServer;
    private ImageView imgValidateCode;
    private EditText etPassword, etValidateCode;
    private AutoCompleteTextView actvUserId;
    private CheckBox ckVisible;

    private String FRONT_ADDRESS = "tcp://180.168.212.76:41205";
    private String userId, password, validateCode;
    public static final String ACTION_LOGIN = "com.kunxun.future.login";

    private CodeUtils mCodeUtils;
    private Bitmap mBitmap;

    private TraderBroadcastReceiver mReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        initLayout(view);

        return view;
    }


    @Override
    public void onStart() {

        mReceiver = new TraderBroadcastReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_LOGIN);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, filter);

        super.onStart();
    }

    @Override
    public void onDestroy() {

        getActivity().stopService(new Intent(getActivity(), TraderService.class));
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    public class TraderBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            int iResult = intent.getIntExtra("login_flag", -1);
            if (iResult == 0) {
                Log.i(TAG, "Login success");

                CheckBox ckRemember = getView().findViewById(R.id.ckRemember);
                if (ckRemember.isChecked()) {
                    setUserIdSharedPrefs();
                }

                startActivity(new Intent(getActivity(), TransactionActivity.class));

//                FragmentManager fm = getFragmentManager();
//                TransactionFragment fragment = new TransactionFragment();
//                fm.beginTransaction().replace(R.id.id_fragment, fragment).commit();
            }
        }
    }

    //region Layout Operation
    private void initLayout(View view) {
        RadioGroup rgServer = view.findViewById(R.id.rgServer);
        if (rgServer.getCheckedRadioButtonId() == R.id.rbServer10010) {
            FRONT_ADDRESS = "tcp://27.115.78.155:41205";
        }

        Button btnLogin = view.findViewById(R.id.btnLogin);
        imgValidateCode = view.findViewById(R.id.imgValidateCode);
        mCodeUtils = CodeUtils.getInstance();
        mBitmap = mCodeUtils.createBitmap();
        imgValidateCode.setImageBitmap(mBitmap);

        etPassword = view.findViewById(R.id.etPassword);
        etValidateCode = view.findViewById(R.id.etValidateCode);

        actvUserId = view.findViewById(R.id.actvUserId);
        String[] hints = new String[]{getUserIdSharedPrefs()};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, hints);
        actvUserId.setAdapter(adapter);

        ckVisible = view.findViewById(R.id.ckVisible);

        btnLogin.setOnClickListener(mClickListener);
        imgValidateCode.setOnClickListener(mClickListener);
        ckVisible.setOnClickListener(mClickListener);

    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.imgValidateCode:
                    Toast.makeText(getContext(), "看不清楚，再来一次", Toast.LENGTH_SHORT).show();
                    mCodeUtils = CodeUtils.getInstance();
                    mBitmap = mCodeUtils.createBitmap();
                    imgValidateCode.setImageBitmap(mBitmap);
                    break;

                case R.id.btnLogin:
                    userId = actvUserId.getText().toString().trim();
                    password = etPassword.getText().toString().trim();
                    validateCode = etValidateCode.getText().toString().trim();

                    if (validateEditText()) {
                        Intent intent = new Intent(getActivity(), TraderService.class);
                        intent.putExtra("front_address", FRONT_ADDRESS);
                        intent.putExtra("user_id", userId);
                        intent.putExtra("password", password);
                        getActivity().startService(intent);
                    }

                    break;

                case R.id.ckVisible:
                    if (ckVisible.isChecked()) {
                        etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    } else {
                        etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    }
                    break;

            }
        }
    };

    private boolean validateEditText() {
        boolean ret = false;
        if (userId == null || TextUtils.isEmpty(userId)) {
            Toast.makeText(getContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
        } else if (password == null || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
        } else if (validateCode == null || TextUtils.isEmpty(validateCode)) {
            Toast.makeText(getContext(), "验证码不能为空", Toast.LENGTH_SHORT).show();
        } else if (!validateCode.equalsIgnoreCase(mCodeUtils.getCode())) {
            Toast.makeText(getContext(), "验证码错误，请重新输入", Toast.LENGTH_SHORT).show();
        } else {
            ret = true;
        }
        return ret;
    }

    private void setUserIdSharedPrefs() {
        SharedPreferences mPrefs = getContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString("UserId", userId).commit();
    }

    private String getUserIdSharedPrefs() {
        SharedPreferences mPrefs = getContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return mPrefs.getString("UserId", "");
    }


    //endregion

}
