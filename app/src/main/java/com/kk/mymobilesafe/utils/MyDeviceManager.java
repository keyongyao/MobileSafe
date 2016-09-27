package com.kk.mymobilesafe.utils;

import android.app.Activity;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;

import com.kk.mymobilesafe.recevier.DeviceAdminSampleRecevier;

/**
 * 设备管理器功能呢
 * Created by Administrator on 2016/9/23.
 */

public class MyDeviceManager {
    private static final String TAG = "main";
    Activity mActivity;
    ComponentName mDeviceAdminSample;
    DevicePolicyManager mDevice;

    public MyDeviceManager(Activity activity) {
        this.mActivity = activity;
        mDeviceAdminSample = new ComponentName(mActivity.getApplication(), DeviceAdminSampleRecevier.class);
        mDevice = (DevicePolicyManager) mActivity.getSystemService(Service.DEVICE_POLICY_SERVICE);

    }


    /**
     * 开启设备管理器的activity
     */
    public void activte() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "设备管理器");
        mActivity.startActivity(intent);
    }

    /**
     * 锁屏
     */
    public void lockScreen() {
        if (mDevice.isAdminActive(mDeviceAdminSample)) {
            mDevice.lockNow();
        }
    }

    /**
     * 清楚数据
     */
    public void wipeData() {
        if (mDevice.isAdminActive(mDeviceAdminSample)) {
            mDevice.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
        }

    }
}

