package com.kk.mymobilesafe.activity;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

import com.kk.mymobilesafe.R;
import com.kk.mymobilesafe.constant.Constant;
import com.kk.mymobilesafe.utils.SharedPreferenceUtil;
import com.kk.mymobilesafe.view.SettingCheckBoxItemView;

public class PhoneGuard2ndActivity extends AppCompatActivity {
    SettingCheckBoxItemView checkBoxItemView;
    Button btnNextPage, btnPreviouPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_guard2nd);
        initUI();
        setListener();
    }

    private void setListener() {
        btnNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PhoneGuard3rdActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnPreviouPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PhoneGuard1stActivity.class);
                startActivity(intent);
                finish();
            }
        });
        checkBoxItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxItemView.changeCheckBoxCheckedState();
                // 保存用户的 配置
                SharedPreferenceUtil.putBoolean(getApplicationContext(), Constant.PhoneGuard.BINDINGSIM, checkBoxItemView.isChecked());
                TelephonyManager manager = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
                String simSerialNumber = manager.getSimSerialNumber();
                SharedPreferenceUtil.putString(getApplicationContext(), Constant.PhoneGuard.SIMSERIALNUMBER, simSerialNumber);
            }
        });
    }

    private void initUI() {
        checkBoxItemView = (SettingCheckBoxItemView) findViewById(R.id.scbiv_bindSIMcard);
        boolean bindingSIM = SharedPreferenceUtil.getBoolean(this, Constant.PhoneGuard.BINDINGSIM);
        checkBoxItemView.initUI(bindingSIM);
        btnNextPage = (Button) findViewById(R.id.btn_PhoneGuard2st_next);
        btnPreviouPage = (Button) findViewById(R.id.btn_PhoneGuard2st_previous);
    }

}
