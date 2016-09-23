package com.kk.mymobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk.mymobilesafe.R;
import com.kk.mymobilesafe.constant.Constant;
import com.kk.mymobilesafe.utils.LogCat;
import com.kk.mymobilesafe.utils.MyDeviceManager;
import com.kk.mymobilesafe.utils.PhoneLocation;
import com.kk.mymobilesafe.utils.SharedPreferenceUtil;

public class PhoneGuardOverViewActivity extends Activity {
    private static final String TAG = "main";
    TextView tv_safeNumber, tv_stateMemo;
    ImageView iv_stateLock;
    Button btnReset;
    Activity mActivity;
    ///////////////////////////////////////////////////////////////////////////
    // 以下几个Textview仅仅作为开启 测试
    ///////////////////////////////////////////////////////////////////////////
    TextView tv_location, tv_playAlarm, tv_wipedata, tv_lockscreen, tv_activte_device_manager;
    /**
     * 自定义设备管理器
     */
    MyDeviceManager myDeviceManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_guard_over_view);
        mActivity = this;
        initUI();
        setListener();
        myDeviceManager = new MyDeviceManager(mActivity);

    }

    private void setListener() {
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PhoneGuard1stActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 仅仅测试功能
        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneLocation.getLocation(mActivity);
                LogCat.getSingleton().i(TAG, "请求获取经纬度");
            }
        });
        tv_playAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogCat.getSingleton().i(TAG, "请求播放警报");
                MediaPlayer player = MediaPlayer.create(getApplicationContext(), R.raw.ylzs);
                player.setLooping(true);
                player.start();

            }
        });
        tv_activte_device_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogCat.getSingleton().i(TAG, "请求激活我的设备管理器");
                myDeviceManager.activte();
            }
        });
        tv_wipedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogCat.getSingleton().i(TAG, "请求擦除数据");
                myDeviceManager.wipeData();
            }
        });
        tv_lockscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogCat.getSingleton().i(TAG, "请求锁屏");
                myDeviceManager.lockScreen();
            }
        });
    }

    private void initUI() {
        tv_safeNumber = (TextView) findViewById(R.id.tv_phonGuardOverView_safenumber);
        tv_safeNumber.setText(SharedPreferenceUtil.getString(this, Constant.PhoneGuard.SAFENUMBER));
        tv_stateMemo = (TextView) findViewById(R.id.tv_phonGuardOverView_lockstateMemo);

        iv_stateLock = (ImageView) findViewById(R.id.iv_phonGuardOverView_lockstate);
        // 默认是 上锁图片
        if (!SharedPreferenceUtil.getBoolean(this, Constant.PhoneGuard.OPENGUARD)) {
            iv_stateLock.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.unlock));
        }
        btnReset = (Button) findViewById(R.id.btn_phoneGuardOverView_reset);
        // todo 仅仅测试功能
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_playAlarm = (TextView) findViewById(R.id.tv_playAlarm);
        tv_wipedata = (TextView) findViewById(R.id.tv_wipedata);
        tv_lockscreen = (TextView) findViewById(R.id.tv_locakscreen);
        tv_activte_device_manager = (TextView) findViewById(R.id.tv_active_deviceManager);

    }
}
