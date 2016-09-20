package com.kk.mymobilesafe;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.mymobilesafe.dialog.UpdateDialog;
import com.kk.mymobilesafe.handler.HandlerLuancher;
import com.kk.mymobilesafe.signle.MySignal;
import com.kk.mymobilesafe.utils.LogCat;
import com.kk.mymobilesafe.utils.Version;

public class LauncherActivity extends AppCompatActivity {
 public    Version mVersion;
    Handler mHandler;
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        // 开启日志猫
        LogCat.getSingleton().setLog_open(true);
        mActivity=this;
        mHandler=new HandlerLuancher(mActivity);
        mVersion  =new Version(mHandler,mActivity);

        initUI();
        update();

    }
    // 检测更新
    private void update() {
        mVersion.checkVersion("http://192.168.199.216:8080/version.json");
    }

    private void initUI() {
        TextView tv_verisonCode= (TextView) findViewById(R.id.tv_version_code);
        tv_verisonCode.setText("现在版本："+mVersion.getLocalVersionName());
    }

}
