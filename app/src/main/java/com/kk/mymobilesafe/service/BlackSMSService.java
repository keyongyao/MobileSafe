package com.kk.mymobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.kk.mymobilesafe.recevier.BlockSMSRecevier;
import com.kk.mymobilesafe.utils.LogCatUtil;

/**
 * 黑名单服务
 * Created by Administrator on 2016/9/26.
 */

public class BlackSMSService extends Service {
    private static final String TAG = "main";
    BlockSMSRecevier blockSMS;
    IntentFilter filter;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        blockSMS = new BlockSMSRecevier();
        filter = new IntentFilter();
        filter.addAction("android.provier.Telephony.SMS_RECEIVED");
        filter.setPriority(1000);
        registerReceiver(blockSMS, filter);
        LogCatUtil.getSingleton().i("main", "registerReceiver(blockSMS,filter);");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(blockSMS);
        LogCatUtil.getSingleton().i("main", "unregisterReceiver(blockSMS);;");

    }
}
