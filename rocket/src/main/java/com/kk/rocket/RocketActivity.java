package com.kk.rocket;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.List;

public class RocketActivity extends Activity {
    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocket);
        initUI();
        setListener();
    }

    private void setListener() {
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    startService(new Intent(getApplicationContext(), RocketService.class));
                    Log.i("main", "RocketActivity.onCheckedChanged: 开启服务");
                    finish();
                } else {
                    ActivityManager ac = (ActivityManager) getSystemService(Service.ACTIVITY_SERVICE);
                    List<ActivityManager.RunningServiceInfo> runningServices = ac.getRunningServices(500);
                    for (ActivityManager.RunningServiceInfo rs : runningServices
                            ) {
                        /*if (rs.service.getClassName().equals("com.kk.rocket.RocketService")){
                            stopService(new Intent(getApplicationContext(),RocketService.class));
                            Log.i("main", "RocketActivity.onCheckedChanged: 关闭服务");*/

                        if ("com.kk.rocket.RocketService".equals(rs.service.getClassName())) {
                            stopService(new Intent(getApplicationContext(), RocketService.class));
                            Log.i("main", "RocketActivity.onCheckedChanged: 关闭服务");
                        }
                    }
                }
            }
        });
    }

    private void initUI() {
        aSwitch = (Switch) findViewById(R.id.sw_openclose);
        ActivityManager ac = (ActivityManager) getSystemService(Service.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = ac.getRunningServices(500);
        for (ActivityManager.RunningServiceInfo rs : runningServices
                ) {
            if (rs.service.getClassName().equals("com.kk.rocket.RocketService")) {
                aSwitch.setChecked(true);
            }
        }
    }

}
