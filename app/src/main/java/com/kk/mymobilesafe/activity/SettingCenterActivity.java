package com.kk.mymobilesafe.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kk.mymobilesafe.R;
import com.kk.mymobilesafe.constant.Constant;
import com.kk.mymobilesafe.utils.SharedPreferenceUtil;
import com.kk.mymobilesafe.view.SettingCheckBoxItemView;

public class SettingCenterActivity extends AppCompatActivity {
    SettingCheckBoxItemView autoUpdateView;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting__center);
        mContext = this;
        initUI();
        setListener();
    }

    private void setListener() {
        autoUpdateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoUpdateView.changeCheckBoxCheckedState();

                SharedPreferenceUtil.putBoolean(mContext, Constant.Setting.AUTOUPDATE, autoUpdateView.isChecked());

            }
        });
    }

    private void initUI() {
        autoUpdateView = (SettingCheckBoxItemView) findViewById(R.id.scbiv_autoupdate);
        boolean autoUpdate = SharedPreferenceUtil.getBoolean(this, Constant.Setting.AUTOUPDATE);
        autoUpdateView.initUI(autoUpdate);
    }
}
