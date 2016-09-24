package com.kk.mymobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kk.mymobilesafe.R;
import com.kk.mymobilesafe.constant.Constant;
import com.kk.mymobilesafe.service.ShowLocationService;
import com.kk.mymobilesafe.utils.ServiceUtil;
import com.kk.mymobilesafe.utils.SharedPreferenceUtil;
import com.kk.mymobilesafe.view.SettingCheckBoxItemView;
import com.kk.mymobilesafe.view.SettingChooseView;


public class SettingCenterActivity extends Activity {

    SettingCheckBoxItemView autoUpdateView, incommingShowLocation;
    SettingChooseView chooseLocationStyle;
    Context mContext;
    Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting__center);
        mContext = getApplicationContext();
        mActivity = this;
        initUI();
        setListener();
    }

    private void setListener() {
        // 自动跟新
        autoUpdateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoUpdateView.changeCheckBoxCheckedState();

                SharedPreferenceUtil.putBoolean(mContext, Constant.Setting.AUTOUPDATE,
                        autoUpdateView.isChecked());

            }
        });
        // 来电显示归属地
        incommingShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incommingShowLocation.changeCheckBoxCheckedState();
                SharedPreferenceUtil.putBoolean(getApplicationContext(), Constant.SettingCenter.INCOMMINGSHOWLOCATION,
                        incommingShowLocation.isChecked());
                // 开启 关闭 来电归属地服务
                if (incommingShowLocation.isChecked()) {
                    Intent intent = new Intent(getApplicationContext(), ShowLocationService.class);
                    startService(intent);
                } else {
                    boolean running = ServiceUtil.checkRunning(mActivity, "com.kk.mymobilesafe.service.ShowLocationService");
                    if (running) {
                        Intent intent = new Intent(getApplicationContext(), ShowLocationService.class);
                        stopService(intent);
                    }
                }
            }

        });
        chooseLocationStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("请选择来电显示样式");
                builder.setIcon(R.drawable.draw);
                builder.setNegativeButton("取消", null);
                builder.setSingleChoiceItems(Constant.SettingCenter.STYLE, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 保存配置 刷新UI
                        SharedPreferenceUtil.putInt(getApplicationContext(), Constant.SettingCenter.CHOOSESTYLRID, which);
                        chooseLocationStyle.changeSubTitleText(Constant.SettingCenter.STYLE[which]);
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    private void initUI() {
        autoUpdateView = (SettingCheckBoxItemView) findViewById(R.id.scbiv_autoupdate);
        boolean autoUpdate = SharedPreferenceUtil.getBoolean(this, Constant.Setting.AUTOUPDATE);
        autoUpdateView.initUI(autoUpdate);
        incommingShowLocation = (SettingCheckBoxItemView) findViewById(R.id.scbiv_showLocation);
        boolean openShowLocation = SharedPreferenceUtil.getBoolean(getApplicationContext(), Constant.SettingCenter.INCOMMINGSHOWLOCATION);
        incommingShowLocation.initUI(openShowLocation);
        chooseLocationStyle = (SettingChooseView) findViewById(R.id.scv_settingCenter_chooseStyle);
        //  refresh UI
        int styleID = SharedPreferenceUtil.getInt(getApplicationContext(), Constant.SettingCenter.CHOOSESTYLRID);
        chooseLocationStyle.changeSubTitleText(Constant.SettingCenter.STYLE[styleID]);

    }
}
