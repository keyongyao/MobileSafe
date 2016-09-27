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
import com.kk.mymobilesafe.service.BlackCallService;
import com.kk.mymobilesafe.service.BlackSMSService;
import com.kk.mymobilesafe.service.ShowLocationService;
import com.kk.mymobilesafe.utils.ServiceUtil;
import com.kk.mymobilesafe.utils.SharedPreferenceUtil;
import com.kk.mymobilesafe.view.SettingCheckBoxItemView;
import com.kk.mymobilesafe.view.SettingChooseView;


public class SettingCenterActivity extends Activity {
    /**
     * 设置自动更新
     */
    SettingCheckBoxItemView autoUpdateView;
    /**
     * 设置来电显示归属地
     */
    SettingCheckBoxItemView incommingShowLocation;
    /**
     * 设置归属地样式
     */
    SettingChooseView chooseLocationStyle;
    /**
     * 选择来电显示提示框位置
     */
    SettingChooseView choosePhoneAddrTipPosition;
    SettingCheckBoxItemView blackNum;
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
        choosePhoneAddrTipPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SetPositionActivity.class));
            }
        });
        blackNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 刷新UI
                blackNum.changeCheckBoxCheckedState();
                // 保存配置
                SharedPreferenceUtil.putBoolean(getApplicationContext(), Constant.SettingCenter.BLACKNUMOPEN, blackNum.isChecked());

                if (blackNum.isChecked()) {
                    startService(new Intent(getApplicationContext(), BlackSMSService.class));
                    startService(new Intent(getApplicationContext(), BlackCallService.class));
                } else {
                    if (ServiceUtil.checkRunning(mActivity, "com.kk.mymobilesafe.service.BlackSMSService"))
                        stopService(new Intent(getApplicationContext(), BlackSMSService.class));

                    if (ServiceUtil.checkRunning(mActivity, "com.kk.mymobilesafe.service.BlackCallService"))
                        stopService(new Intent(getApplicationContext(), BlackCallService.class));
                }
            }
        });
    }

    private void initUI() {
        // 自动更新
        autoUpdateView = (SettingCheckBoxItemView) findViewById(R.id.scbiv_autoupdate);
        boolean autoUpdate = SharedPreferenceUtil.getBoolean(this, Constant.Setting.AUTOUPDATE);
        autoUpdateView.initUI(autoUpdate);
        // 来电归属地显示
        incommingShowLocation = (SettingCheckBoxItemView) findViewById(R.id.scbiv_showLocation);
        boolean openShowLocation = SharedPreferenceUtil.getBoolean(getApplicationContext(), Constant.SettingCenter.INCOMMINGSHOWLOCATION);
        incommingShowLocation.initUI(openShowLocation);
        // 选择归属地样式
        chooseLocationStyle = (SettingChooseView) findViewById(R.id.scv_settingCenter_chooseStyle);
        //  refresh UI
        int styleID = SharedPreferenceUtil.getInt(getApplicationContext(), Constant.SettingCenter.CHOOSESTYLRID);
        chooseLocationStyle.changeSubTitleText(Constant.SettingCenter.STYLE[styleID]);
        //选择归属地显示位置
        choosePhoneAddrTipPosition = (SettingChooseView) findViewById(R.id.scv_settingCenter_setPhoneAddrTipPosition);
        // 黑名单开启
        boolean isOpenBlackNum = SharedPreferenceUtil.getBoolean(getApplicationContext(), Constant.SettingCenter.BLACKNUMOPEN);
        blackNum = (SettingCheckBoxItemView) findViewById(R.id.scbiv_balcknum);
        blackNum.initUI(isOpenBlackNum);

    }
}
