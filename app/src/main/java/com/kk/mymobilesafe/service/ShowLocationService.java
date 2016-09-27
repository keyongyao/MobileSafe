package com.kk.mymobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.mymobilesafe.R;
import com.kk.mymobilesafe.constant.Constant;
import com.kk.mymobilesafe.dao.QueryLocationDao;
import com.kk.mymobilesafe.signle.MySignal;
import com.kk.mymobilesafe.utils.LogCatUtil;
import com.kk.mymobilesafe.utils.SharedPreferenceUtil;

/**
 * 在来电显示电话的归属地
 * Created by Administrator on 2016/9/24.
 */

public class ShowLocationService extends Service {
    private static final String TAG = "main";
    /**
     * 来电归属地文字
     */
    TextView textView;

    WindowManager windowManager;
    /**
     * 来电归属地窗体
     */
    View mytoast;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MySignal.ATool.QUERYRESULT) {
                textView.setText((String) msg.obj);
            }
        }
    };
    PhoneStateListener stateListener;
    TelephonyManager service;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // 监视 电话的状态
        service = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);

        service.listen((stateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE: {
                        LogCatUtil.getSingleton().i(TAG, "CALL_STATE_IDLE");
                        reMoveMyToast();
                        break;
                    }
                    case TelephonyManager.CALL_STATE_OFFHOOK: {
                        LogCatUtil.getSingleton().i(TAG, "CALL_STATE_OFFHOOK:" + incomingNumber);
                        Toast.makeText(ShowLocationService.this, incomingNumber, Toast.LENGTH_LONG).show();
                        showMyToast(incomingNumber);
                        break;
                    }
                    case TelephonyManager.CALL_STATE_RINGING: {
                        LogCatUtil.getSingleton().i(TAG, "CALL_STATE_RINGING");
                        break;
                    }
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        }), PhoneStateListener.LISTEN_CALL_STATE);
        super.onCreate();

        // 土司的触摸事件
    }

    private void reMoveMyToast() {
        if (windowManager != null && textView != null && mytoast != null) {
            windowManager.removeView(mytoast);
        }
    }

    private void showMyToast(String incomingNumber) {
        // 自定义Toast
        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        final WindowManager.LayoutParams params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        //              | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;  使其可触摸
        params.gravity = Gravity.TOP + Gravity.LEFT;
        mytoast = View.inflate(getApplicationContext(), R.layout.mytoast, null);
        textView = (TextView) mytoast.findViewById(R.id.tv_myToast);
        // 读取用户配置的样式
        int sytleID = SharedPreferenceUtil.getInt(getApplicationContext(), Constant.SettingCenter.CHOOSESTYLRID);
        textView.setBackgroundResource(Constant.SettingCenter.STYLEID[sytleID]);
        QueryLocationDao.query(getApplicationContext(), incomingNumber, handler);
        windowManager = (WindowManager) getSystemService(Service.WINDOW_SERVICE);
        params.x = SharedPreferenceUtil.getInt(getApplicationContext(), Constant.SettingCenter.LOCATIONX);
        params.y = SharedPreferenceUtil.getInt(getApplicationContext(), Constant.SettingCenter.LOCATIONY);

        windowManager.addView(mytoast, params);
        // 设置 触摸事件
        mytoast.setOnTouchListener(new View.OnTouchListener() {
            // 事件的起始坐标
            int startY;
            int startX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        // 点击时 的坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        // 移动后的坐标
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();
                        // 实际要移动的坐标差
                        int disX = moveX - startX;
                        int disY = moveY - startY;

                        // 控件应该移动到的位置
                        params.x = params.x + disX;
                        params.y = params.y + disY;

                        // 不让控件跑到屏幕外边去 返回 不更新


                        // 设置 控件的位置

                        windowManager.updateViewLayout(mytoast, params);

                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        // 保存 控件的当前位置
                        SharedPreferenceUtil.putInt(getApplicationContext(), Constant.SettingCenter.LOCATIONX, params.x);
                        SharedPreferenceUtil.putInt(getApplicationContext(), Constant.SettingCenter.LOCATIONY, params.y);
                        break;
                    }
                }
                return true;
            }
        });

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    // 不注销监听事件会一直监听下去，导致即使关闭服务，也显示来电窗体。
    @Override
    public void onDestroy() {
        super.onDestroy();
        service.listen(stateListener, PhoneStateListener.LISTEN_NONE);
    }
}
