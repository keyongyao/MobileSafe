package com.kk.mymobilesafe.utils;

import android.app.Activity;
import android.app.Service;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * 获取手机的经纬度
 * Created by Administrator on 2016/9/23.
 */

public class PhoneLocation {
    private static final String TAG = "main";

    /**
     * 获取经纬度
     *
     * @param activity
     */
    public static void getLocation(Activity activity) {
        LocationManager lm = (LocationManager) activity.getSystemService(Service.LOCATION_SERVICE);
        //2,在经纬度发生变化的时候回去触发的事件监听(定位(基站,网络,GPS)类型,minTime更新时间间隔,minDistance移动最少多少米的距离更新)
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 5, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LogCat.getSingleton().i(TAG, "latitude:longitude" + latitude + "<-->" + longitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });

    }


}
