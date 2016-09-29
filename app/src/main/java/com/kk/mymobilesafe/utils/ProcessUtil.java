package com.kk.mymobilesafe.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Debug;

import com.kk.mymobilesafe.R;
import com.kk.mymobilesafe.bean.ProcessInfoBean;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 提供进程信息
 * Created by Administrator on 2016/9/28.
 */

public class ProcessUtil {
    Activity mActivity;
    ActivityManager mAm;
    PackageManager mPm;
    List<ActivityManager.RunningAppProcessInfo> mRunningAppProcesses;

    public ProcessUtil(Activity mActivity) {
        this.mActivity = mActivity;
        mAm = (ActivityManager) mActivity.getSystemService(Service.ACTIVITY_SERVICE);
        mPm = mActivity.getApplicationContext().getPackageManager();
        mRunningAppProcesses = mAm.getRunningAppProcesses();
    }

    /**
     * 杀死 进程信息集合 的进程
     *
     * @param activity 当前Activity
     * @param killList 进程信息集合
     */
    public static void killProcess(Activity activity, ArrayList<ProcessInfoBean> killList) {
        ActivityManager am = (ActivityManager) activity.getSystemService(Service.ACTIVITY_SERVICE);
        for (ProcessInfoBean bean : killList
                ) {
            if (bean.isCheck) {
                am.killBackgroundProcesses(bean.pkgName);
            }
        }
    }

    /**
     * @return 空闲内存
     */
    public long getAvailableMemory() {
        ActivityManager am = (ActivityManager) mActivity.getSystemService(Service.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    /**
     * @return 总内存
     */
    public long getTotalMemory() {
        long mTotal;
        // /proc/meminfo读出的内核信息进行解释
        String path = "/proc/meminfo";
        String content = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path), 8);
            String line;
            if ((line = br.readLine()) != null) {
                content = line;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // beginIndex
        int begin = content.indexOf(':');
        // endIndex
        int end = content.indexOf('k');
        // 截取字符串信息

        content = content.substring(begin + 1, end).trim();
        mTotal = Integer.parseInt(content);
        return mTotal * 1024;
    }

    /**
     * @return 所有进程信息集合
     */
    public ArrayList<ProcessInfoBean> getAllProcessInfo() {
        ArrayList<ProcessInfoBean> processInfoBeanArrayList = new ArrayList<>();
        for (ActivityManager.RunningAppProcessInfo pi : mRunningAppProcesses
                ) {
            ProcessInfoBean bean = new ProcessInfoBean();
            // 获取进程包名
            bean.pkgName = pi.processName;
            // 获取进程 消耗的内存
            Debug.MemoryInfo[] processMemoryInfo = mAm.getProcessMemoryInfo(new int[]{pi.pid});
            Debug.MemoryInfo memoryInfo = processMemoryInfo[0];
            bean.memSize = memoryInfo.getTotalSharedDirty() * 1024;
            try {
                // 获取 APP label 和 icon
                ApplicationInfo applicationInfo = mPm.getApplicationInfo(bean.pkgName, PackageManager.GET_META_DATA);
                bean.name = applicationInfo.loadLabel(mPm).toString();
                bean.icon = applicationInfo.loadIcon(mPm);
                // 是否为系统进程
                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                    bean.isSystem = true;
                } else {
                    bean.isSystem = false;
                }
            } catch (PackageManager.NameNotFoundException e) {
                //   PackageManager.getApplicationInfo()  的名字 没有找到
                bean.name = pi.processName;
                bean.icon = mActivity.getResources().getDrawable(R.mipmap.ic_launcher);
                bean.isSystem = true;
                e.printStackTrace();
            }
            processInfoBeanArrayList.add(bean);
        }
        return processInfoBeanArrayList;
    }

    /**
     * 分类进程集合  分为 系统  和 用户
     *
     * @param userProcessList
     * @param sysProcessList
     */
    public void loadClassfiyProcessInfoList(ArrayList<ProcessInfoBean> userProcessList, ArrayList<ProcessInfoBean> sysProcessList) {
        ArrayList<ProcessInfoBean> allProcessInfo = getAllProcessInfo();
        for (ProcessInfoBean bean : allProcessInfo
                ) {
            if (bean.isSystem) {
                sysProcessList.add(bean);
            } else {
                userProcessList.add(bean);
            }
        }
    }

    /**
     * @return 所有进程的数量
     */
    public int getAllProcessCount() {
        return mRunningAppProcesses.size();
    }

}
