package com.kk.mymobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kk.mymobilesafe.R;
import com.kk.mymobilesafe.constant.Constant;
import com.kk.mymobilesafe.utils.SharedPreferenceUtil;

public class PhoneGuard3rdActivity extends AppCompatActivity {
    EditText etInputSafeNumber;
    Button btnNextPage, btnPreviousPage, btnChooseSafeNUmber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_guard3rd);
        initUI();
        setListener();
    }

    private void setListener() {
        btnPreviousPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PhoneGuard2ndActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save 输入的手机号
                String contactNumber = etInputSafeNumber.getText().toString().trim();
                SharedPreferenceUtil.putString(getApplicationContext(), Constant.PhoneGuard.SAFENUMBER, contactNumber);
                Intent intent = new Intent(getApplicationContext(), PhoneGuard4thActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnChooseSafeNUmber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016/9/22  完成选择联系人的功能
                Toast.makeText(PhoneGuard3rdActivity.this, "功能呢个尚待完成", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUI() {
        String contactNumber = SharedPreferenceUtil.getString(this, Constant.PhoneGuard.SAFENUMBER);
        etInputSafeNumber = (EditText) findViewById(R.id.et_phoneGuard3rd_inputSafeNumber);
        if (!TextUtils.isEmpty(contactNumber)) {
            etInputSafeNumber.setText(contactNumber);
        }
        btnPreviousPage = (Button) findViewById(R.id.btn_PhoneGuard3st_previous);
        btnNextPage = (Button) findViewById(R.id.btn_PhoneGuard3st_next);
        btnChooseSafeNUmber = (Button) findViewById(R.id.btn_phoneGuard3rd_chooseContact);
    }
}
