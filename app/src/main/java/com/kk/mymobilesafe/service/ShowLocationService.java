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
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.mymobilesafe.R;
import com.kk.mymobilesafe.constant.Constant;
import com.kk.mymobilesafe.dao.QueryLocation;
import com.kk.mymobilesafe.signle.MySignal;
import com.kk.mymobilesafe.utils.LogCat;
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // 监视 电话的状态
        TelephonyManager service = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
        service.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE: {
                        LogCat.getSingleton().i(TAG, "CALL_STATE_IDLE");
                        reMoveMyToast();
                        break;
                    }
                    case TelephonyManager.CALL_STATE_OFFHOOK: {
                        LogCat.getSingleton().i(TAG, "CALL_STATE_OFFHOOK:" + incomingNumber);
                        Toast.makeText(ShowLocationService.this, incomingNumber, Toast.LENGTH_LONG).show();
                        showMyToast(incomingNumber);
                        break;
                    }
                    case TelephonyManager.CALL_STATE_RINGING: {
                        LogCat.getSingleton().i(TAG, "CALL_STATE_RINGING");
                        break;
                    }
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
        super.onCreate();
    }

    private void reMoveMyToast() {
        if (windowManager != null && textView != null) {
            windowManager.removeView(mytoast);
        }
    }

    private void showMyToast(String incomingNumber) {
        // 自定义Toast
        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        WindowManager.LayoutParams params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        params.gravity = Gravity.TOP + Gravity.LEFT;
        mytoast = View.inflate(getApplicationContext(), R.layout.mytoast, null);
        textView = (TextView) mytoast.findViewById(R.id.tv_myToast);
        // 读取用户配置的样式
        int sytleID = SharedPreferenceUtil.getInt(getApplicationContext(), Constant.SettingCenter.CHOOSESTYLRID);
        textView.setBackgroundResource(Constant.SettingCenter.STYLEID[sytleID]);
        QueryLocation.query(getApplicationContext(), incomingNumber, handler);
        windowManager = (WindowManager) getSystemService(Service.WINDOW_SERVICE);
        windowManager.addView(mytoast, params);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
