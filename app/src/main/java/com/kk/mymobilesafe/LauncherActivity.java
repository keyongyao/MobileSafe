package com.kk.mymobilesafe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.mymobilesafe.activity.HomeActivity;
import com.kk.mymobilesafe.constant.Constant;
import com.kk.mymobilesafe.handler.HandlerLuancher;
import com.kk.mymobilesafe.service.BlackCallService;
import com.kk.mymobilesafe.service.BlackSMSService;
import com.kk.mymobilesafe.service.ShowLocationService;
import com.kk.mymobilesafe.utils.CopyFileUtil;
import com.kk.mymobilesafe.utils.LogCatUtil;
import com.kk.mymobilesafe.utils.SharedPreferenceUtil;
import com.kk.mymobilesafe.utils.VersionUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
        mActivity=this;
        mHandler=new HandlerLuancher(mActivity);
        mVersion = new VersionUtil(mHandler, mActivity);
        initUI();
        openService();

    }

    private void openService() {
        // 打开  来电显示归属地的服务
        boolean openincommingShowLocation = SharedPreferenceUtil.getBoolean(getApplicationContext(), Constant.SettingCenter.INCOMMINGSHOWLOCATION);
        if (openincommingShowLocation) {
            initPhoneDB();
            startService(new Intent(getApplicationContext(), ShowLocationService.class));
        }
        // 自动更新服务配置
        if (SharedPreferenceUtil.getBoolean(mActivity.getApplicationContext(), Constant.Setting.AUTOUPDATE)) {
            update();
        } else {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(mActivity.getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    mActivity.finish();
                }
            }, 2000);
        }
        boolean isOpenBlackNum = SharedPreferenceUtil.getBoolean(this, Constant.SettingCenter.BLACKNUMOPEN);
        startService(new Intent(getApplicationContext(), BlackSMSService.class));
        startService(new Intent(getApplicationContext(), BlackCallService.class));

    }

    private void initPhoneDB() {
        AssetManager assetManager = getAssets();
        File file = new File(getFilesDir(), "address.db");
        if (file.exists()) {
            return;
        }
        try {
            InputStream inputStream = assetManager.open("address.db");
            FileOutputStream fileOutputStream = openFileOutput("address.db", Context.MODE_PRIVATE);
            boolean copyResult = CopyFileUtil.copy(inputStream, fileOutputStream);
            if (copyResult) {
                Toast.makeText(this, "初始化地址数据库成功", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        AlphaAnimation animation = new AlphaAnimation(0, 1);
        animation.setDuration(3000);
        root.startAnimation(animation);

    }


    // 检测更新
    private void update() {
        mVersion.checkVersion("http://192.168.199.216:8080/version.json");
    }

    private void initUI() {
        TextView tv_verisonCode= (TextView) findViewById(R.id.tv_version_code);
        tv_verisonCode.setText("现在版本："+mVersion.getLocalVersionName());
        root = (RelativeLayout) findViewById(R.id.launcher_activity);
    }

}
