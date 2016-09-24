package com.kk.mymobilesafe.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/9/24.
 */

public class ServiceUtil {
    /**
     * @param activity
     * @param clazz    服务的类名
     * @return true: running
     */
    public static boolean checkRunning(Activity activity, String clazz) {
        ActivityManager service = (ActivityManager) activity.getSystemService(Service.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = service.getRunningServices(500);
        for (ActivityManager.RunningServiceInfo ss : runningServices
                ) {
            if (ss.service.getClassName().equals(clazz)) {
                return true;
            }
        }

        return false;
    }
}
