package com.kk.mymobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.mymobilesafe.R;
import com.kk.mymobilesafe.constant.Constant;
import com.kk.mymobilesafe.utils.SharedPreferenceUtil;

public class PhoneGuard4thActivity extends AppCompatActivity {
    Button btnFinishPage, btnPreviousPage;
    CheckBox cbIsOpenGuard;
    TextView tvMemo;
    Activity mActivity;
    LinearLayout ll_checkbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_guard4th);
        initUI();
        setListener();
    }

    private void setListener() {
        btnPreviousPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PhoneGuard3rdActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnFinishPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PhoneGuardOverViewActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ll_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  设置 checkbox 状态和  保存配置
                cbIsOpenGuard.setChecked(!cbIsOpenGuard.isChecked());
                SharedPreferenceUtil.putBoolean(getApplicationContext(), Constant.PhoneGuard.OPENGUARD, cbIsOpenGuard.isChecked());
                if (cbIsOpenGuard.isChecked()) {
                    Toast.makeText(PhoneGuard4thActivity.this, "已打开", Toast.LENGTH_SHORT).show();
                    tvMemo.setText("你已开启防盗保护");

                } else {
                    Toast.makeText(PhoneGuard4thActivity.this, "已关闭", Toast.LENGTH_SHORT).show();
                    tvMemo.setText("你没开启防盗保护");

                }
            }
        });

    }

    private void initUI() {
        btnPreviousPage = (Button) findViewById(R.id.btn_PhoneGuard4st_previous);
        btnFinishPage = (Button) findViewById(R.id.btn_PhoneGuard4st_finish);
        cbIsOpenGuard = (CheckBox) findViewById(R.id.cb_phonguard4th_openGuard);
        ll_checkbox = (LinearLayout) findViewById(R.id.ll_phoneguard4th_checkbox);
        // 阻止checkbox 获取点击事件
        cbIsOpenGuard.setFocusable(false);
        cbIsOpenGuard.setClickable(false);
        cbIsOpenGuard.setFocusableInTouchMode(false);
        tvMemo = (TextView) findViewById(R.id.tv_phonGuard4th_memo);

        boolean isOpenGuard = SharedPreferenceUtil.getBoolean(getApplicationContext(), Constant.PhoneGuard.OPENGUARD);
        cbIsOpenGuard.setChecked(isOpenGuard);
        if (isOpenGuard) {
            tvMemo.setText("你已开启防盗保护");
        }
    }
}
