package com.kk.mymobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kk.mymobilesafe.R;

public class PhoneGuard1stActivity extends PhoneGuardBasicActivity {
    Button btnNextpage;

    @Override
    void nextPage() {
        Toast.makeText(PhoneGuard1stActivity.this, "进入下一个页面", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), PhoneGuard2ndActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    void prevousPage() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_guard1st);
        initUI();
        setListener();
    }

    private void setListener() {
        btnNextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage();
            }
        });
    }

    private void initUI() {
        btnNextpage = (Button) findViewById(R.id.btn_PhoneGuard1st_next);

    }
}
