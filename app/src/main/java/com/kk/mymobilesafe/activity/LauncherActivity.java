package com.kk.mymobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kk.mymobilesafe.R;
import com.kk.mymobilesafe.constant.Constant;
import com.kk.mymobilesafe.handler.HandlerLuancher;
import com.kk.mymobilesafe.utils.LogCatUtil;
import com.kk.mymobilesafe.utils.SharedPreferenceUtil;
import com.kk.mymobilesafe.utils.VersionUtil;

public class LauncherActivity extends Activity {
    public VersionUtil mVersion;
    Handler mHandler;
    Activity mActivity;
    RelativeLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        //  TODO 开启日志猫
        LogCatUtil.getSingleton().setLog_open(true);
        mActivity = this;
        mHandler = new HandlerLuancher(mActivity);
        mVersion = new VersionUtil(mHandler, mActivity);

        initUI();

    }

    @Override
    protected void onStart() {
        super.onStart();
        AlphaAnimation animation = new AlphaAnimation(0, 1);
        animation.setDuration(3000);
        root.startAnimation(animation);
        if (SharedPreferenceUtil.getBoolean(mActivity.getApplicationContext(), Constant.Setting.AUTOUPDATE)) {
            update();
        } else {
            // TODO: 2016/9/22  调试 改小 任务启动阀值
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(mActivity.getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    mActivity.finish();
                }
            }, 0);
        }
    }


    // 检测更新
    private void update() {
        mVersion.checkVersion("http://192.168.199.216:8080/version.json");
    }

    private void initUI() {
        TextView tv_verisonCode = (TextView) findViewById(R.id.tv_version_code);
        tv_verisonCode.setText("现在版本：" + mVersion.getLocalVersionName());
        root = (RelativeLayout) findViewById(R.id.launcher_activity);
    }

}
