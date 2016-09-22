package com.kk.mymobilesafe.listener;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.kk.mymobilesafe.activity.SettingCenterActivity;
import com.kk.mymobilesafe.constant.Constant;
import com.kk.mymobilesafe.dialog.PhoneGuardPwdDialog;
import com.kk.mymobilesafe.utils.LogCat;
import com.kk.mymobilesafe.utils.SharedPreferenceUtil;

/**
 * 事件监视器
 * Created by Administrator on 2016/9/20.
 */
public class GridItemListener implements AdapterView.OnItemClickListener {
    private static final String TAG = "main";
    Activity mActivity;
    LogCat logCat;

    public GridItemListener(Activity mActivity) {
        this.mActivity = mActivity;
        logCat = LogCat.getSingleton();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String desc = Constant.GridData.descs[position];

        switch (desc) {
            case "手机防盗": {
                logCat.i(TAG, "GridItemListener.onItemClick: 手机防盗");
                String pwd = SharedPreferenceUtil.getString(mActivity.getApplicationContext(), Constant.PhoneGuard.GUARDPWD);
                if (TextUtils.isEmpty(pwd)) {
                    new PhoneGuardPwdDialog(mActivity).inputPwd();
                } else {
                    new PhoneGuardPwdDialog(mActivity).checkPwd();
                }
                break;
            }
            case "通讯卫士": {
                logCat.i(TAG, "GridItemListener.onItemClick: 通讯卫士");

                break;
            }
            case "软件管家": {
                logCat.i(TAG, "GridItemListener.onItemClick: 软件管家");

                break;
            }
            case "进程管理": {
                logCat.i(TAG, "GridItemListener.onItemClick: 进程管理");

                break;
            }
            case "流量统计": {
                logCat.i(TAG, "GridItemListener.onItemClick: 流量统计");

                break;
            }
            case "手机杀毒": {
                logCat.i(TAG, "GridItemListener.onItemClick: 手机杀毒");

                break;
            }
            case "缓存清理": {
                logCat.i(TAG, "GridItemListener.onItemClick: 缓存清理");

                break;
            }
            case "高级工具": {
                logCat.i(TAG, "GridItemListener.onItemClick: 高级工具");

                break;
            }
            case "设置中心": {
                logCat.i(TAG, "GridItemListener.onItemClick: 设置中心");
                Intent intent = new Intent(mActivity.getApplicationContext(), SettingCenterActivity.class);
                mActivity.startActivity(intent);
                break;
            }
        }
    }
}
