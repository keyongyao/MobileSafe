package com.kk.mymobilesafe.constant;

import com.kk.mymobilesafe.R;

/**
 * 应用上下文的常量值 <br>
 * 仅仅保存 业务逻辑的常量，不能包含UI的常量<br>
 * Created by Administrator on 2016/9/20.
 */
public class Constant {
    /**
     * 功能九宫格的常量
     */
    public static class GridData {
        public final static String[] descs = {"手机防盗", "通讯卫士", "软件管家", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};
        public final static int[] icons = {R.drawable.home_safe, R.drawable.home_callmsgsafe, R.drawable.home_apps, R.drawable.home_taskmanager,
                R.drawable.home_netmanager, R.drawable.home_trojan, R.drawable.home_sysoptimize, R.drawable.home_tools,
                R.drawable.home_settings};
    }

    /**
     * 设置中心的常量
     */
    public static class Setting {
        public final static String AUTOUPDATE = "autoupdate";
    }

    /**
     * 手机防盗模块常量
     */
    public static class PhoneGuard {
        public static final String BINDINGSIM = "bindingSIM";
        public static final String GUARDPWD = "guardPwd";
        public static final String SAFENUMBER = "safeNumber";
        public static final String OPENGUARD = "isOPenGuard";
        public static final String SIMSERIALNUMBER = "simSerialNumber";
        public static final String ALLSETTINGDONE = "allsettingdone";
        public static final String HADSET = "hadset";
    }

    public static class SettingCenter {
        public static final String INCOMMINGSHOWLOCATION = "openShowLocation";
        public static final String[] STYLE = {"蓝色", "灰色", "青色", "橙色", "透明"};
        public static final int[] STYLEID = {R.drawable.call_locate_blue,
                R.drawable.call_locate_gray,
                R.drawable.call_locate_green,
                R.drawable.call_locate_orange,
                R.drawable.call_locate_white};
        public static final String CHOOSESTYLRID = "chooseStyleID";
        public static final String LOCATIONX = "locationX";
        public static final String LOCATIONY = "locationY";
        public static final String BLACKNUMOPEN = "balckNumOpen";
    }



}
